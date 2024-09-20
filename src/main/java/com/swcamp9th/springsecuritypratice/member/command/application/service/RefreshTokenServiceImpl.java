package com.swcamp9th.springsecuritypratice.member.command.application.service;

import com.swcamp9th.springsecuritypratice.member.command.domain.aggregate.entity.RefreshToken;
import com.swcamp9th.springsecuritypratice.member.command.domain.repository.RefreshTokenRepository;
import com.swcamp9th.springsecuritypratice.security.JwtUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService{

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, JwtUtil jwtUtil) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public void saveTokenInfo(String email, String accessToken, String refreshToken) {
        RefreshToken token = new RefreshToken(email, accessToken, refreshToken);
        refreshTokenRepository.save(token);

        log.info("저장된 값 : " + refreshTokenRepository.findById(email));

    }

    @Override
    @Transactional
    public void removeRefreshToken(String accessToken) {
        RefreshToken token = refreshTokenRepository.findByAccessToken(accessToken)
            .orElseThrow(IllegalArgumentException::new);

        refreshTokenRepository.delete(token);
    }

    @Override
    @Transactional
    public String refreshRefreshToken(String token) {
        token = token.replace("Bearer ", "");
        log.info("access token입니다" + token);

        RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(token).orElseThrow();
//        RefreshToken refreshToken = refreshTokenRepository.findById(email).orElseThrow();
        log.info("Refresh token: {}", refreshToken);
        String newAccessToken = null;
        String newRefreshToken = null;

        // RefreshToken이 존재하고 유효하다면 실행
        if (refreshToken != null && jwtUtil.validateToken(refreshToken.getRefreshToken())) {

            // RefreshToken 객체를 꺼내온다.
            RefreshToken resultToken = refreshToken;

            // 권한과 아이디를 추출해 새로운 액세스토큰을 만든다.
            newAccessToken = jwtUtil.generateAccessToken(resultToken.getId());
            newRefreshToken = jwtUtil.generateRefreshToken(resultToken.getId());


            // 액세스 토큰의 값을 수정해준다.
            resultToken.updateAccessToken(newAccessToken);
            resultToken.updateRefreshToken(newRefreshToken);
            refreshTokenRepository.save(resultToken);

        }

        return newAccessToken;
    }
}
