package com.r2s.mobile_store.application.service;


import com.r2s.mobile_store.application.dto.user.AuthenticationDTO;
import com.r2s.mobile_store.application.dto.user.UserDTO;
import com.r2s.mobile_store.application.dto.user.UserLoginDTO;
import com.r2s.mobile_store.application.dto.user.UserRegistrationDTO;
import com.r2s.mobile_store.domain.service.RoleService;
import com.r2s.mobile_store.domain.service.UserService;
import com.r2s.mobile_store.domain.models.Role;
import com.r2s.mobile_store.domain.models.User;
import com.r2s.mobile_store.infrastructure.security.JwtTokenUtil;
import com.r2s.mobile_store.presentation.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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
        Role role=roleService.findByName(createUserRequest.getRole());

        User user=userMapper.convertUserRegistrationDTOToUser(createUserRequest,role);

        UserDTO createUserResponse=userMapper.convertUserToCreateUserResponse(userService.registration(user));

        return createUserResponse;
    }

    public AuthenticationDTO signIn(UserLoginDTO signinRequest) {
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
