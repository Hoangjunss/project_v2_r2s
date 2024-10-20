package com.r2s.mobile_store.controller;


import com.r2s.mobile_store.application.dto.user.AuthenticationDTO;
import com.r2s.mobile_store.application.dto.user.UserDTO;
import com.r2s.mobile_store.application.dto.user.UserLoginDTO;
import com.r2s.mobile_store.application.dto.user.UserRegistrationDTO;
import com.r2s.mobile_store.application.service.UserApplicationService;
import com.r2s.mobile_store.infrastructure.security.JwtTokenUtil;
import com.r2s.mobile_store.infrastructure.security.OurUserDetailsService;
import com.r2s.mobile_store.presentation.controller.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Value("${server.servlet.context-path}")
    private String prefix;

    @MockBean
    private UserApplicationService userService;
    @MockBean
    private OurUserDetailsService ourUserDetailsService;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    private UserRegistrationDTO createUserRequest;
    private UserLoginDTO signInRequest;
    private AuthenticationDTO authenticationDTO;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        createUserRequest = new UserRegistrationDTO();

        createUserRequest.setUsername("user123");

        createUserRequest.setEmail("user123@gmail.com");

        createUserRequest.setPassword("password");

        createUserRequest.setFullname("User Test");

        createUserRequest.setRole("USER");

        signInRequest = new UserLoginDTO();

        signInRequest.setName("user123");

        signInRequest.setPassword("password");
        userDTO = new UserDTO();
        userDTO.setId(1);

        userDTO.setUsername("user123");

        userDTO.setEmail("user123@gmail.com");

        userDTO.setFullname("User Test");

        userDTO.setRole("USER");
        authenticationDTO=new AuthenticationDTO();
        authenticationDTO.setRefreshToken("12233444");
        authenticationDTO.setToken("122233344");
    }

    @Test
    void registration_shouldReturn201() throws Exception {
        // Giả lập phản hồi khi gọi userService.registration


        when(userService.registration(any(UserRegistrationDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post(prefix+"/user/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDTO.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userDTO.getId())) // Kiểm tra id
                .andExpect(jsonPath("$.username").value(userDTO.getUsername())) // Kiểm tra username
                .andExpect(jsonPath("$.email").value(userDTO.getEmail())) // Kiểm tra email
                .andExpect(jsonPath("$.fullname").value(userDTO.getFullname())) // Kiểm tra fullname
                .andExpect(jsonPath("$.role").value(userDTO.getRole())); // Kiểm tra role

        verify(userService, times(1)).registration(any(UserRegistrationDTO.class));
    }


    @Test
    void signIn_shouldReturn200() throws Exception {
        // Giả lập phản hồi khi gọi userService.signIn


        when(userService.signIn(any(UserLoginDTO.class)))
                .thenReturn(authenticationDTO);

        mockMvc.perform(post(prefix+"/user/signin")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signInRequest.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(authenticationDTO.getToken()))
                .andExpect(jsonPath("$.refreshToken").value(authenticationDTO.getRefreshToken()));

        verify(userService, times(1)).signIn(any(UserLoginDTO.class));
    }

    @Test
    void refreshtoken_shouldReturn200() throws Exception {
        // Giả lập phản hồi khi gọi userService.generateRefreshToken


        when(userService.generateRefreshToken(anyString()))
                .thenReturn(authenticationDTO);

        mockMvc.perform(post(prefix+"/user/refreshtoken")
                        .with(csrf())
                        .param("token", "refresh-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(authenticationDTO.getToken()))
                .andExpect(jsonPath("$.refreshToken").value(authenticationDTO.getRefreshToken()));

        verify(userService, times(1)).generateRefreshToken(anyString());
    }

   
}
