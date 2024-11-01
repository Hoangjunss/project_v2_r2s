package com.r2s.mobile_store.presentation.controller;


import com.r2s.mobile_store.application.dto.user.AuthenticationDTO;
import com.r2s.mobile_store.application.dto.user.UserDTO;
import com.r2s.mobile_store.application.dto.user.UserLoginDTO;
import com.r2s.mobile_store.application.dto.user.UserRegistrationDTO;
import com.r2s.mobile_store.application.service.UserApplicationService;
import com.r2s.mobile_store.infrastructure.exception.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserApplicationService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserDTO>> registration(@RequestBody UserRegistrationDTO createUserRequest,
                                                             HttpServletRequest request) {
        log.info("User registration request: {}", createUserRequest.toString());

        UserDTO registeredUser = userService.registration(createUserRequest);
        ApiResponse<UserDTO> response = new ApiResponse<>(
                "success",
                "User registered successfully",
                registeredUser,
                null,
                request.getRequestURI()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<AuthenticationDTO>> signIn(
            @RequestBody UserLoginDTO signInRequest, HttpServletRequest request) {
        AuthenticationDTO authenticationResponse = userService.signIn(signInRequest);

        ApiResponse<AuthenticationDTO> response = new ApiResponse<>(
                "success",
                "User signed in successfully",
                authenticationResponse,
                null,
                request.getRequestURI()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<ApiResponse<AuthenticationDTO>> refreshtoken(
            @RequestParam String token, HttpServletRequest request) {
        AuthenticationDTO authenticationResponse = userService.generateRefreshToken(token);

        ApiResponse<AuthenticationDTO> response = new ApiResponse<>(
                "success",
                "Token refreshed successfully",
                authenticationResponse,
                null,
                request.getRequestURI()
        );

        return ResponseEntity.ok(response);
    }


}