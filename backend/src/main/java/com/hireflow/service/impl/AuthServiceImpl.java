package com.hireflow.service.impl;

import com.hireflow.dto.request.LoginRequest;
import com.hireflow.dto.request.RegisterRequest;
import com.hireflow.dto.response.AuthResponse;
import com.hireflow.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public AuthResponse register(RegisterRequest request) {

        return AuthResponse.builder()
                .message("Registration API Coming Soon")
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        return AuthResponse.builder()
                .message("Login API Coming Soon")
                .build();
    }
}