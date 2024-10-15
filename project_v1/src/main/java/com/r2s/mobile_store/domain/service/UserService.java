package com.r2s.mobile_store.domain.service;


import com.r2s.mobile_store.domain.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User registration(User user);
    User signIn(User user);
    UserDetails generateRefreshToken(String token);
}
