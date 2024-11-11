package com.r2s.mobile_store.service;

import com.r2s.mobile_store.application.dto.user.*;
import com.r2s.mobile_store.application.service.UserApplicationService;
import com.r2s.mobile_store.domain.service.*;
import com.r2s.mobile_store.domain.models.*;
import com.r2s.mobile_store.infrastructure.exception.*;
import com.r2s.mobile_store.infrastructure.exception.Error;
import com.r2s.mobile_store.infrastructure.security.JwtTokenUtil;
import com.r2s.mobile_store.presentation.mapper.UserMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserApplicationServiceTest {

    @Mock private UserService userService;
    @Mock private UserMapper userMapper;
    @Mock private JwtTokenUtil jwtTokenUtil;
    @Mock private RoleService roleService;
    @InjectMocks private UserApplicationService userApplicationService;

    private UserRegistrationDTO userRegistrationDTO;
    private UserLoginDTO userLoginDTO;
    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        // Thiết lập UserRegistrationDTO
        userRegistrationDTO = new UserRegistrationDTO();
        userRegistrationDTO.setUsername("testuser");
        userRegistrationDTO.setEmail("test@example.com");
        userRegistrationDTO.setPassword("password123");
        userRegistrationDTO.setRole("USER");

        // Thiết lập UserLoginDTO
        userLoginDTO = new UserLoginDTO();
        userLoginDTO.setName("testuser");
        userLoginDTO.setPassword("password123");

        // Thiết lập Role
        role = new Role();
        role.setName("USER");

        // Thiết lập User
        user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
    }


    @Test
    void registration_shouldRegisterUserSuccessfully() {
        when(roleService.findByName(any())).thenReturn(role);
        when(userMapper.convertUserRegistrationDTOToUser(any(), any())).thenReturn(user);
        when(userService.registration(any())).thenReturn(user);
        when(userMapper.convertUserToCreateUserResponse(any())).thenReturn(new UserDTO());

        UserDTO result = userApplicationService.registration(userRegistrationDTO);

        assertNotNull(result);
        verify(roleService, times(1)).findByName(any());
        verify(userService, times(1)).registration(any());
    }


    @Test
    void registration_shouldThrowExceptionWhenUsernameIsMissing() {
        userRegistrationDTO.setUsername(null);

        CustomException exception = assertThrows(CustomException.class, () -> userApplicationService.registration(userRegistrationDTO));

        assertEquals(Collections.singletonList(Error.USERNAME_REQUIRED), exception.getErrors());
    }

    @Test
    void registration_shouldThrowExceptionWhenUsernameIsEmpty() {
        userRegistrationDTO.setUsername("");

        CustomException exception = assertThrows(CustomException.class, () -> userApplicationService.registration(userRegistrationDTO));

        assertEquals(Collections.singletonList(Error.USERNAME_REQUIRED), exception.getErrors());
    }

    @Test
    void registration_shouldThrowExceptionWhenUsernameTooLong() {
        userRegistrationDTO.setUsername("a".repeat(26));

        CustomException exception = assertThrows(CustomException.class, () -> userApplicationService.registration(userRegistrationDTO));

        assertEquals(Collections.singletonList(Error.USERNAME_TOO_LONG), exception.getErrors());
    }

    @Test
    void registration_shouldThrowExceptionWhenEmailIsMissing() {
        userRegistrationDTO.setEmail(null);

        CustomException exception = assertThrows(CustomException.class, () -> userApplicationService.registration(userRegistrationDTO));

        assertEquals(Collections.singletonList(Error.EMAIL_REQUIRED), exception.getErrors());
    }

    @Test
    void registration_shouldThrowExceptionWhenEmailIsEmpty() {
        userRegistrationDTO.setEmail("");

        CustomException exception = assertThrows(CustomException.class, () -> userApplicationService.registration(userRegistrationDTO));

        assertEquals(Collections.singletonList(Error.EMAIL_REQUIRED), exception.getErrors());
    }

    @Test
    void registration_shouldThrowExceptionWhenEmailInvalidFormat() {
        userRegistrationDTO.setEmail("invalid-email-format");

        CustomException exception = assertThrows(CustomException.class, () -> userApplicationService.registration(userRegistrationDTO));

        assertEquals(Collections.singletonList(Error.INVALID_EMAIL_FORMAT), exception.getErrors());
    }

    @Test
    void registration_shouldThrowExceptionWhenPasswordIsMissing() {
        userRegistrationDTO.setPassword(null);

        CustomException exception = assertThrows(CustomException.class, () -> userApplicationService.registration(userRegistrationDTO));

        assertEquals(Collections.singletonList(Error.PASSWORD_REQUIRED), exception.getErrors());
    }

    @Test
    void registration_shouldThrowExceptionWhenPasswordIsEmpty() {
        userRegistrationDTO.setPassword("");

        CustomException exception = assertThrows(CustomException.class, () -> userApplicationService.registration(userRegistrationDTO));

        assertEquals(Collections.singletonList(Error.PASSWORD_REQUIRED), exception.getErrors());
    }

    @Test
    void registration_shouldThrowExceptionWhenPasswordTooShort() {
        userRegistrationDTO.setPassword("123");

        CustomException exception = assertThrows(CustomException.class, () -> userApplicationService.registration(userRegistrationDTO));

        assertEquals(Collections.singletonList(Error.PASSWORD_TOO_SHORT), exception.getErrors());
    }


    @Test
    void signIn_shouldSignInUserSuccessfully() {
        when(userMapper.convertAuthenticationToUser(any())).thenReturn(user);

        when(userService.signIn(any(User.class))).thenReturn(user);

        when(jwtTokenUtil.generateToken(any())).thenReturn("mockedJwtToken");
        when(jwtTokenUtil.generateRefreshToken(any())).thenReturn("mockedRefreshToken");

        AuthenticationDTO result = userApplicationService.signIn(userLoginDTO);

        assertNotNull(result);
        assertEquals("mockedJwtToken", result.getToken());
        assertEquals("mockedRefreshToken", result.getRefreshToken());
    }

    @Test
    void signIn_shouldThrowExceptionWhenUsernameIsMissing() {
        userLoginDTO.setName(null);

        CustomException exception = assertThrows(CustomException.class, () -> userApplicationService.signIn(userLoginDTO));

        assertEquals(Collections.singletonList(Error.USERNAME_REQUIRED), exception.getErrors());
    }
    @Test
    void signIn_shouldThrowExceptionWhenPasswordIsMissing() {
        // Đặt mật khẩu là null để mô phỏng trường hợp thiếu mật khẩu
        userLoginDTO.setPassword(null);

        CustomException exception = assertThrows(CustomException.class, () -> userApplicationService.signIn(userLoginDTO));

        assertEquals(Collections.singletonList(Error.PASSWORD_REQUIRED), exception.getErrors());
    }


    @Test
    void generateRefreshToken_shouldGenerateTokenSuccessfully() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userService.generateRefreshToken(any())).thenReturn(userDetails);
        when(jwtTokenUtil.generateToken(any())).thenReturn("newJwtToken");
        when(jwtTokenUtil.generateRefreshToken(any())).thenReturn("newRefreshToken");

        AuthenticationDTO result = userApplicationService.generateRefreshToken("mockToken");

        assertNotNull(result);
        assertEquals("newJwtToken", result.getToken());
        assertEquals("newRefreshToken", result.getRefreshToken());
    }


}
