package com.r2s.mobile_store.presentation.controller;


import com.r2s.mobile_store.application.dto.user.AuthenticationDTO;
import com.r2s.mobile_store.application.dto.user.UserLoginDTO;
import com.r2s.mobile_store.application.dto.user.UserRegistrationDTO;
import com.r2s.mobile_store.application.service.UserApplicationService;
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
    public ResponseEntity<?> registration(@RequestBody UserRegistrationDTO createUserRequest) {
        log.info("user :{}",createUserRequest.toString());
       return new ResponseEntity<>(userService.registration(createUserRequest), HttpStatus.CREATED);
    }
    @PostMapping("/signin")
    public ResponseEntity<AuthenticationDTO> signIn(
            @RequestBody UserLoginDTO signInRequest) {
        AuthenticationDTO authenticationResponse = userService.signIn(signInRequest);


        return ResponseEntity.ok(authenticationResponse);
    }
    @PostMapping("/refreshtoken")
    public ResponseEntity<AuthenticationDTO> refreshtoken(
            @RequestParam String token) {
        AuthenticationDTO authenticationResponse = userService.generateRefreshToken(token);


        return ResponseEntity.ok(authenticationResponse);
    }
    @PostMapping("/1")
    public ResponseEntity<String> h(
            ) {



        return ResponseEntity.ok("oki");
    }

}
