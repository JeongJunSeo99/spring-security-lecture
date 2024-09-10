package com.swcamp9th.springsecuritypratice.member.command.application.service;

public interface RefreshTokenService {
    void saveTokenInfo(String email, String accessToken, String refreshToken );
    void removeRefreshToken(String accessToken);

    String refreshRefreshToken(String accessToken);
}
