package com.hireflow.service;

import com.hireflow.entity.RefreshToken;
import com.hireflow.entity.User;

import java.util.Optional;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user, String deviceInfo);

    RefreshToken verifyExpiration(RefreshToken token);

    Optional<RefreshToken> findByToken(String token);

    void revokeToken(String token);

    void revokeAllUserTokens(User user);
}