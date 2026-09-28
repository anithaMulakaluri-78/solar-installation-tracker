package com.solartracker.service;

import com.solartracker.dto.request.LoginRequest;
import com.solartracker.dto.response.LoginResponse;
import com.solartracker.dto.response.UserResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    UserResponse getCurrentUser(String username);
}
