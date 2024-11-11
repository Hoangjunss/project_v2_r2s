package com.r2s.mobile_store.application.service;


import com.r2s.mobile_store.application.dto.user.AuthenticationDTO;
import com.r2s.mobile_store.application.dto.user.UserDTO;
import com.r2s.mobile_store.application.dto.user.UserLoginDTO;
import com.r2s.mobile_store.application.dto.user.UserRegistrationDTO;
import com.r2s.mobile_store.domain.service.RoleService;
import com.r2s.mobile_store.domain.service.UserService;
import com.r2s.mobile_store.domain.models.Role;
import com.r2s.mobile_store.domain.models.User;
import com.r2s.mobile_store.infrastructure.exception.CustomException;
import com.r2s.mobile_store.infrastructure.exception.CustomJwtException;
import com.r2s.mobile_store.infrastructure.exception.Error;
import com.r2s.mobile_store.infrastructure.security.JwtTokenUtil;
import com.r2s.mobile_store.presentation.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserApplicationService  {


   @Autowired
   private UserService userService;
   @Autowired
   private UserMapper userMapper;
   @Autowired
   private JwtTokenUtil jwtTokenUtil;
   @Autowired
   private RoleService roleService;

    public UserDTO registration(UserRegistrationDTO createUserRequest) {
        List<Error> errors = new ArrayList<>();
        if (createUserRequest.getUsername() == null || createUserRequest.getUsername().isEmpty()) {
            errors.add(Error.USERNAME_REQUIRED);
        } else if (createUserRequest.getUsername().length() > 25) {
            errors.add(Error.USERNAME_TOO_LONG);
        }

        if (createUserRequest.getEmail() == null || createUserRequest.getEmail().isEmpty()) {
            errors.add(Error.EMAIL_REQUIRED);
        } else if (!createUserRequest.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.add(Error.INVALID_EMAIL_FORMAT);
        }

        if (createUserRequest.getPassword() == null || createUserRequest.getPassword().isEmpty()) {
            errors.add(Error.PASSWORD_REQUIRED);
        } else if (createUserRequest.getPassword().length() < 6) {
            errors.add(Error.PASSWORD_TOO_SHORT);
        }

        // Throw exception if errors exist
        if (!errors.isEmpty()) {
            throw new CustomException(errors);
        }

        Role role=roleService.findByName(createUserRequest.getRole());

        User user=userMapper.convertUserRegistrationDTOToUser(createUserRequest,role);

        UserDTO createUserResponse=userMapper.convertUserToCreateUserResponse(userService.registration(user));

        return createUserResponse;
    }

    public AuthenticationDTO signIn(UserLoginDTO signinRequest) {
        List<Error> errors = new ArrayList<>();

        if (signinRequest.getName() == null || signinRequest.getName().isEmpty()) {
           errors.add(Error.USERNAME_REQUIRED);
        }

        if (signinRequest.getPassword() == null || signinRequest.getPassword().isEmpty()) {
            errors.add(Error.PASSWORD_REQUIRED);
        }
        if (!errors.isEmpty()) {
            throw new CustomException(errors);
        }
        User user=userMapper.convertAuthenticationToUser(signinRequest);

        UserDetails userDetails=userService.signIn(user);

        String jwtToken=jwtTokenUtil.generateToken(userDetails);

        String refreshToken=jwtTokenUtil.generateRefreshToken(userDetails);

      return AuthenticationDTO.builder().token(jwtToken).refreshToken(refreshToken).build();

    }



    public AuthenticationDTO generateRefreshToken(String token) {
           UserDetails user=userService.generateRefreshToken(token);
           String jwtToken=jwtTokenUtil.generateToken(user);
           String refreshToken=jwtTokenUtil.generateRefreshToken(user);

        return  AuthenticationDTO.builder().token(jwtToken).refreshToken(refreshToken).build();
    }





}
