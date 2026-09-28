package com.solartracker.service.impl;

import com.solartracker.dto.request.LoginRequest;
import com.solartracker.dto.response.LoginResponse;
import com.solartracker.dto.response.UserResponse;
import com.solartracker.entity.User;
import com.solartracker.exception.ResourceNotFoundException;
import com.solartracker.repository.UserRepository;
import com.solartracker.security.JwtTokenProvider;
import com.solartracker.security.UserPrincipal;
import com.solartracker.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public LoginResponse login(LoginRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(principal.getUsername(), principal.getRoleNames());

        return LoginResponse.builder()
                .accessToken(token)
                .expiresIn(jwtTokenProvider.getExpirationMs() / 1000)
                .user(toUserResponse(principal))
                .build();
    }

    @Override
    public UserResponse getCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .roles(user.getRoles().stream().map(r -> r.getName()).toList())
                .build();
    }

    private UserResponse toUserResponse(UserPrincipal principal) {
        return UserResponse.builder()
                .id(principal.getId())
                .username(principal.getUsername())
                .email(principal.getEmail())
                .roles((List<String>) principal.getRoleNames())
                .build();
    }
}
