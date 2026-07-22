package com.hireflow.controller;

import com.hireflow.dto.request.ChangePasswordRequest;
import com.hireflow.dto.request.ForgotPasswordRequest;
import com.hireflow.dto.request.LoginRequest;
import com.hireflow.dto.request.LogoutRequest;
import com.hireflow.dto.request.RefreshTokenRequest;
import com.hireflow.dto.request.RegisterRequest;
import com.hireflow.dto.request.ResetPasswordRequest;
import com.hireflow.dto.response.ApiResponse;
import com.hireflow.dto.response.AuthResponse;
import com.hireflow.security.UserPrincipal;
import com.hireflow.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(
        name = "Authentication",
        description = "Endpoints for user registration, login, JWT authentication, password management and logout."
)
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new candidate or recruiter account and returns access and refresh tokens."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Registration successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failed", content = @Content)
    })
    @PostMapping("/register")
    public AuthResponse register(
            @Valid @RequestBody RegisterRequest request,
            @Parameter(hidden = true)
            @RequestHeader(value = "User-Agent", required = false) String userAgent) {

        return authService.register(request, userAgent);
    }

    @Operation(
            summary = "Login",
            description = "Authenticates a user and returns JWT access and refresh tokens."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation failed", content = @Content)
    })
    @PostMapping("/login")
    public AuthResponse login(
            @Valid @RequestBody LoginRequest request,
            @Parameter(hidden = true)
            @RequestHeader(value = "User-Agent", required = false) String userAgent) {

        return authService.login(request, userAgent);
    }

    @Operation(
            summary = "Refresh access token",
            description = "Generates a new JWT access token using a valid refresh token."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Refresh token is invalid or expired", content = @Content)
    })
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(
            @Valid @RequestBody RefreshTokenRequest request,
            @Parameter(hidden = true)
            @RequestHeader(value = "User-Agent", required = false) String userAgent) {

        AuthResponse response = authService.refreshToken(request, userAgent);

        return ResponseEntity.ok(
                ApiResponse.<AuthResponse>builder()
                        .success(true)
                        .message("Token refreshed successfully.")
                        .data(response)
                        .build()
        );
    }

    @Operation(
            summary = "Logout",
            description = "Logs out the current device by invalidating the supplied refresh token."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Logged out successfully")
    })
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @Valid @RequestBody LogoutRequest request) {

        authService.logout(request);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Logged out successfully.")
                        .build()
        );
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Logout from all devices",
            description = "Invalidates every active refresh token associated with the authenticated user."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Logged out from all devices successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PostMapping("/logout-all")
    public ResponseEntity<ApiResponse<Void>> logoutAllDevices(
            @AuthenticationPrincipal UserPrincipal principal) {

        authService.logoutAllDevices(principal.getUser().getEmail());

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Logged out from all devices successfully.")
                        .build()
        );
    }

    @Operation(
            summary = "Forgot password",
            description = "Sends a password reset email if the account exists."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password reset email processed")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        authService.forgotPassword(request);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("If an account exists for this email, a password reset link has been sent.")
                        .build()
        );
    }

    @Operation(
            summary = "Reset password",
            description = "Resets the password using a valid password reset token."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password reset successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid or expired reset token", content = @Content)
    })
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        authService.resetPassword(request);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Password has been reset successfully. Please login again.")
                        .build()
        );
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
            summary = "Change password",
            description = "Changes the password of the authenticated user."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        authService.changePassword(principal.getUser().getEmail(), request);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Password changed successfully.")
                        .build()
        );
    }
}