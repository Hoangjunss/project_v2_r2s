package com.r2s.mobile_store.presentation.mapper;

import com.r2s.mobile_store.application.dto.user.UserDTO;
import com.r2s.mobile_store.application.dto.user.UserLoginDTO;
import com.r2s.mobile_store.application.dto.user.UserRegistrationDTO;
import com.r2s.mobile_store.domain.models.Role;
import com.r2s.mobile_store.domain.models.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    @Autowired
    private ModelMapper modelMapper;
    public User convertUserRegistrationDTOToUser(UserRegistrationDTO createUserRequest, Role role){
        User user= User.builder()
                .email(createUserRequest.getEmail())
                .fullname(createUserRequest.getFullname())
                .password(createUserRequest.getPassword())
                .username(createUserRequest.getUsername())
                .role(role).build();
        return user;
    }
    public UserDTO convertUserToCreateUserResponse(User user){
       UserDTO createUserResponse= UserDTO.builder()
               .id(user.getId())
               .email(user.getEmail())
               .fullname(user.getFullname())
               .username(user.getUsername())
               .role(user.getRole().getName())
               .build();
        return createUserResponse;
    }
    public User convertAuthenticationToUser(UserLoginDTO authenticationRequest){
       User user= User.builder()
               .username(authenticationRequest.getName())
               .password(authenticationRequest.getPassword())
               .build();
        return user;
    }
}
