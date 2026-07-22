package com.hireflow.service.impl;

import com.hireflow.config.JwtProperties;
import com.hireflow.entity.RefreshToken;
import com.hireflow.entity.User;
import com.hireflow.exception.UnauthorizedException;
import com.hireflow.repository.RefreshTokenRepository;
import com.hireflow.security.SecureTokenGenerator;
import com.hireflow.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(User user, String deviceInfo) {

        RefreshToken refreshToken = RefreshToken.builder()
                .token(SecureTokenGenerator.generate())
                .user(user)
                .expiryDate(LocalDateTime.now().plusSeconds(jwtProperties.getRefreshExpiration() / 1000))
                .revoked(false)
                .deviceInfo(deviceInfo)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {

        if (Boolean.TRUE.equals(token.getRevoked())) {
            throw new UnauthorizedException("Refresh token has been revoked. Please login again.");
        }

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new UnauthorizedException("Refresh token has expired. Please login again.");
        }

        return token;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    @Transactional
    public void revokeToken(String token) {

        refreshTokenRepository.findByToken(token).ifPresent(existing -> {
            existing.setRevoked(true);
            refreshTokenRepository.save(existing);
        });
    }

    @Override
    @Transactional
    public void revokeAllUserTokens(User user) {

        List<RefreshToken> tokens = refreshTokenRepository.findAllByUserAndRevokedFalse(user);

        tokens.forEach(t -> t.setRevoked(true));

        refreshTokenRepository.saveAll(tokens);
    }
}