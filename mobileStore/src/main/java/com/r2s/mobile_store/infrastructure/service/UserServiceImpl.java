package com.r2s.mobile_store.infrastructure.service;



import com.r2s.mobile_store.domain.models.User;
import com.r2s.mobile_store.domain.repository.UserRepository;

import com.r2s.mobile_store.domain.service.UserService;
import com.r2s.mobile_store.infrastructure.exception.CustomException;
import com.r2s.mobile_store.infrastructure.exception.CustomJwtException;
import com.r2s.mobile_store.infrastructure.exception.Error;
import com.r2s.mobile_store.infrastructure.security.JwtTokenUtil;
import com.r2s.mobile_store.infrastructure.security.OurUserDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OurUserDetailsService ourUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public User registration(User user) {


        if (usernameExists(user.getUsername())) {
            throw new CustomException(Error.USER_ALREADY_EXISTS);
        }



        user.setId(getGenerationId());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User signIn(User user) {


        String name = user.getUsername().trim().toLowerCase();

        if (!usernameExists(name)) {
            throw new CustomJwtException(Error.USER_NOT_FOUND);
        }

        User userFind = userRepository.findByUsername(name).orElseThrow();
        if (!passwordEncoder.matches(user.getPassword(), userFind.getPassword())) {
            throw new CustomJwtException(Error.INVALID_CREDENTIALS);
        }

        return userFind;
    }

    @Override
    public UserDetails generateRefreshToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new CustomJwtException(Error.TOKEN_REQUIRED);
        }

        String username = jwtTokenUtil.extractUsernameToken(token);
        if (username == null || !usernameExists(username)) {
            throw new CustomJwtException(Error.USER_NOT_FOUND_IN_TOKEN);
        }

        return ourUserDetailsService.loadUserByUsername(username);
    }

    private boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public Integer getGenerationId() {
        UUID uuid = UUID.randomUUID();
        return (int) (uuid.getMostSignificantBits() & 0xFFFFFFFFL);
    }
}