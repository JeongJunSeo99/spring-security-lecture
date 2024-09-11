package com.swcamp9th.springsecuritypratice.member.command.application.service;

import java.util.List;

public interface RefreshTokenService {
    void saveTokenInfo(String email, String accessToken, String refreshToken );
    void removeRefreshToken(String accessToken);

    List<String> refreshRefreshToken(String token);
}
