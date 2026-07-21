package com.hireflow.service;

import com.hireflow.dto.request.LoginRequest;
import com.hireflow.dto.request.RegisterRequest;
import com.hireflow.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}