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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

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
        user.setEmail("testuser@example.com");
        user.setPassword("password123");
    }

    @Test
    void registration_shouldThrowExceptionWhenUserAlreadyExists() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        CustomException exception = assertThrows(CustomException.class, () -> userService.registration(user));

        assertEquals(singletonList(Error.USER_ALREADY_EXISTS), exception.getErrors());
    }

    @Test
    void registration_shouldSaveUserWhenValid() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.registration(user);

        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void signIn_shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        CustomJwtException exception = assertThrows(CustomJwtException.class, () -> userService.signIn(user));

        assertEquals(Error.USER_NOT_FOUND, exception.getError());
    }

    @Test
    void signIn_shouldThrowExceptionWhenInvalidCredentials() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false); // Password mismatch

        CustomJwtException exception = assertThrows(CustomJwtException.class, () -> userService.signIn(user));

        assertEquals(Error.INVALID_CREDENTIALS, exception.getError());
    }



    @Test
    void generateRefreshToken_shouldThrowExceptionWhenTokenNotProvided() {
        String token = null; // Token not provided

        CustomJwtException exception = assertThrows(CustomJwtException.class, () -> userService.generateRefreshToken(token));

        assertEquals(Error.TOKEN_REQUIRED, exception.getError());
    }

    @Test
    void generateRefreshToken_shouldThrowExceptionWhenUserNotFoundInToken() {
        String token = "validToken";
        when(jwtTokenUtil.extractUsernameToken(token)).thenReturn("nonExistentUser");
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        CustomJwtException exception = assertThrows(CustomJwtException.class, () -> userService.generateRefreshToken(token));

        assertEquals(Error.USER_NOT_FOUND_IN_TOKEN, exception.getError());
    }

    @Test
    void generateRefreshToken_shouldReturnUserDetailsWhenValidToken() {
        String token = "validToken";
        String username = user.getUsername();
        when(jwtTokenUtil.extractUsernameToken(token)).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(ourUserDetailsService.loadUserByUsername(username)).thenReturn(mock(UserDetails.class));

        UserDetails userDetails = userService.generateRefreshToken(token);

        assertNotNull(userDetails);
        verify(ourUserDetailsService, times(1)).loadUserByUsername(username);
    }
}
