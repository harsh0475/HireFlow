package com.hireflow.service;

import com.hireflow.dto.request.ChangePasswordRequest;
import com.hireflow.dto.request.ForgotPasswordRequest;
import com.hireflow.dto.request.LoginRequest;
import com.hireflow.dto.request.LogoutRequest;
import com.hireflow.dto.request.RefreshTokenRequest;
import com.hireflow.dto.request.RegisterRequest;
import com.hireflow.dto.request.ResetPasswordRequest;
import com.hireflow.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request, String deviceInfo);

    AuthResponse login(LoginRequest request, String deviceInfo);

    AuthResponse refreshToken(RefreshTokenRequest request, String deviceInfo);

    void logout(LogoutRequest request);

    void logoutAllDevices(String email);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

    void changePassword(String email, ChangePasswordRequest request);
}