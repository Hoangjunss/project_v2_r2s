package com.r2s.mobile_store.service;

import com.r2s.mobile_store.domain.models.User;
import com.r2s.mobile_store.domain.repository.UserRepository;
import com.r2s.mobile_store.infrastructure.exception.CustomException;
import com.r2s.mobile_store.infrastructure.exception.CustomJwtException;
import com.r2s.mobile_store.infrastructure.exception.Error;
import com.r2s.mobile_store.infrastructure.security.JwtTokenUtil;
import com.r2s.mobile_store.infrastructure.security.OurUserDetailsService;
import com.r2s.mobile_store.infrastructure.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private OurUserDetailsService ourUserDetailsService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(UUID.randomUUID().variant());
        user.setUsername("testuser");
        user.setPassword("password123");
    }

    @Test
    void registration_shouldSaveUserWhenUsernameNotExists() {
        // Giả lập hành vi của các phụ thuộc
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.registration(user);

        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registration_shouldThrowExceptionWhenUsernameExists() {
        // Giả lập trường hợp username đã tồn tại
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        CustomException exception = assertThrows(CustomException.class, () -> userService.registration(user));

        assertEquals(Error.USER_ALREADY_EXISTS, exception.getError());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void signIn_shouldReturnUserWhenCredentialsAreValid() {
        // Giả lập hành vi khi tên người dùng tồn tại và mật khẩu khớp
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(user.getPassword(), user.getPassword())).thenReturn(true);

        User result = userService.signIn(user);

        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        verify(userRepository, atLeastOnce()).findByUsername(user.getUsername());  // Cho phép ít nhất một lần gọi
    }

    @Test
    void signIn_shouldThrowExceptionWhenUserNotFound() {
        // Giả lập trường hợp tên người dùng không tồn tại
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        CustomJwtException exception = assertThrows(CustomJwtException.class, () -> userService.signIn(user));

        assertEquals(Error.USER_NOT_FOUND, exception.getError());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void signIn_shouldThrowExceptionWhenPasswordDoesNotMatch() {
        // Giả lập hành vi khi tên người dùng tồn tại nhưng mật khẩu không khớp
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        CustomJwtException exception = assertThrows(CustomJwtException.class, () -> userService.signIn(user));

        assertEquals(Error.NOT_FOUND, exception.getError());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
    }

    @Test
    void generateRefreshToken_shouldReturnUserDetailsWhenTokenIsValid() {
        // Giả lập hành vi của `jwtTokenUtil` và `ourUserDetailsService`
        String token = "validToken";
        when(jwtTokenUtil.extractUsernameToken(token)).thenReturn(user.getUsername());
        when(ourUserDetailsService.loadUserByUsername(user.getUsername())).thenReturn(mock(UserDetails.class));

        UserDetails result = userService.generateRefreshToken(token);

        assertNotNull(result);
        verify(jwtTokenUtil, times(1)).extractUsernameToken(token);
        verify(ourUserDetailsService, times(1)).loadUserByUsername(user.getUsername());
    }
}
