package com.hireflow.service.impl;

import com.hireflow.dto.request.LoginRequest;
import com.hireflow.dto.request.RegisterRequest;
import com.hireflow.dto.response.AuthResponse;
import com.hireflow.entity.User;
import com.hireflow.entity.enums.Role;
import com.hireflow.exception.BadRequestException;
import com.hireflow.repository.UserRepository;
import com.hireflow.security.JwtService;
import com.hireflow.security.UserPrincipal;
import com.hireflow.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {

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

        return AuthResponse.builder()
                .accessToken(jwtService.generateAccessToken(principal))
                .refreshToken(jwtService.generateRefreshToken(principal))
                .message("Registration successful.")
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {

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

        return AuthResponse.builder()
                .accessToken(jwtService.generateAccessToken(principal))
                .refreshToken(jwtService.generateRefreshToken(principal))
                .message("Login successful.")
                .build();
    }
}