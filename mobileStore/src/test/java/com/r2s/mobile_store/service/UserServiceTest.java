package com.r2s.mobile_store.service;

import com.r2s.mobile_store.domain.models.User;
import com.r2s.mobile_store.domain.repository.UserRepository;
import com.r2s.mobile_store.domain.service.CartService;
import com.r2s.mobile_store.infrastructure.exception.CustomException;
import com.r2s.mobile_store.infrastructure.exception.CustomJwtException;
import com.r2s.mobile_store.infrastructure.security.JwtTokenUtil;
import com.r2s.mobile_store.infrastructure.security.OurUserDetailsService;
import com.r2s.mobile_store.infrastructure.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.r2s.mobile_store.infrastructure.exception.Error;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartService cartService;

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
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setPassword("password");
    }
    @Test
    void registration_ShouldReturnSavedUser_WhenValid() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.registration(user);

        assertNotNull(savedUser);

        assertEquals(user.getUsername(), savedUser.getUsername());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registration_ShouldThrowException_WhenUsernameAlreadyExists() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.registration(user);
        });

        assertEquals(Error.USER_ALREADY_EXISTS, exception.getError());

        verify(userRepository, never()).save(any(User.class));
    }

    // Test signIn method


    @Test
    void signIn_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        CustomJwtException exception = assertThrows(CustomJwtException.class, () -> {
            userService.signIn(user);
        });

        assertEquals(Error.USER_NOT_FOUND, exception.getError());
    }

    @Test
    void signIn_ShouldThrowException_WhenPasswordDoesNotMatch() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        CustomJwtException exception = assertThrows(CustomJwtException.class, () -> {
            userService.signIn(user);
        });

        assertEquals(Error.NOT_FOUND, exception.getError());
    }

}
