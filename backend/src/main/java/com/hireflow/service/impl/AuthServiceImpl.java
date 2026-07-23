package com.hireflow.service.impl;

import com.hireflow.config.JwtProperties;
import com.hireflow.dto.request.ChangePasswordRequest;
import com.hireflow.dto.request.ForgotPasswordRequest;
import com.hireflow.dto.request.LoginRequest;
import com.hireflow.dto.request.LogoutRequest;
import com.hireflow.dto.request.RefreshTokenRequest;
import com.hireflow.dto.request.RegisterRequest;
import com.hireflow.dto.request.ResetPasswordRequest;
import com.hireflow.dto.response.AuthResponse;
import com.hireflow.entity.PasswordResetToken;
import com.hireflow.entity.RefreshToken;
import com.hireflow.entity.User;
import com.hireflow.entity.enums.Role;
import com.hireflow.exception.BadRequestException;
import com.hireflow.exception.ResourceNotFoundException;
import com.hireflow.exception.UnauthorizedException;
import com.hireflow.repository.PasswordResetTokenRepository;
import com.hireflow.repository.UserRepository;
import com.hireflow.security.JwtService;
import com.hireflow.security.SecureTokenGenerator;
import com.hireflow.security.UserPrincipal;
import com.hireflow.service.AuthService;
import com.hireflow.service.EmailService;
import com.hireflow.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hireflow.mapper.UserMapper;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final RefreshTokenService refreshTokenService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request, String deviceInfo) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered.");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() == null ? Role.CANDIDATE : request.getRole())
                .enabled(true)
                .build();

        user = userRepository.save(user);

        UserPrincipal principal = new UserPrincipal(user);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user, deviceInfo);

        emailService.sendWelcomeEmail(user.getEmail(), user.getFirstName());

        return AuthResponse.builder()
                .accessToken(jwtService.generateAccessToken(principal))
                .refreshToken(refreshToken.getToken())
                .user(userMapper.toResponse(user))
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request, String deviceInfo) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new BadRequestException("Invalid email or password."));

        UserPrincipal principal = new UserPrincipal(user);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user, deviceInfo);

        return AuthResponse.builder()
                .accessToken(jwtService.generateAccessToken(principal))
                .refreshToken(refreshToken.getToken())
                .user(userMapper.toResponse(user))
                .build();
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request, String deviceInfo) {

        RefreshToken existingToken = refreshTokenService.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token."));

        refreshTokenService.verifyExpiration(existingToken);

        User user = existingToken.getUser();

        // Rotation: the presented token is single-use. Revoke it, then issue a new one.
        refreshTokenService.revokeToken(existingToken.getToken());

        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user, deviceInfo);

        UserPrincipal principal = new UserPrincipal(user);

        return AuthResponse.builder()
                .accessToken(jwtService.generateAccessToken(principal))
                .refreshToken(newRefreshToken.getToken())
                .user(userMapper.toResponse(user))
                .build();
    }

    @Override
    public void logout(LogoutRequest request) {
        refreshTokenService.revokeToken(request.getRefreshToken());
    }

    @Override
    public void logoutAllDevices(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        refreshTokenService.revokeAllUserTokens(user);
    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {

        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {

            passwordResetTokenRepository.deleteAllByUser(user);

            PasswordResetToken resetToken = PasswordResetToken.builder()
                    .token(SecureTokenGenerator.generate())
                    .user(user)
                    .expiryDate(LocalDateTime.now()
                            .plusSeconds(jwtProperties.getPasswordResetExpiration() / 1000))
                    .used(false)
                    .build();

            passwordResetTokenRepository.save(resetToken);

            emailService.sendPasswordResetEmail(user.getEmail(), user.getFirstName(), resetToken.getToken());
        });

        // Intentionally the same outcome whether or not the email exists (prevents account enumeration).
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new BadRequestException("Invalid or expired reset token."));

        if (Boolean.TRUE.equals(resetToken.getUsed())
                || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Invalid or expired reset token.");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);

        // Force re-login everywhere after a password reset.
        refreshTokenService.revokeAllUserTokens(user);
    }

    @Override
    @Transactional
    public void changePassword(String email, ChangePasswordRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Old password is incorrect.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Force re-login on every other device after a password change.
        refreshTokenService.revokeAllUserTokens(user);
    }
}