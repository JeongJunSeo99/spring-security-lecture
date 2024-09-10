package com.swcamp9th.springsecuritypratice.security.filter;

import com.swcamp9th.springsecuritypratice.member.command.application.service.RefreshTokenService;
import com.swcamp9th.springsecuritypratice.security.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class RefreshFilter  extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public RefreshFilter(JwtUtil jwtUtil, RefreshTokenService refreshTokenService) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        /* 설명. JWT 토큰이 Request Header에 있는 경우 */
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            if (jwtUtil.isTokenExpired(token)) {
                log.info("리프레쉬 토큰 호출되는중입니다아아아아아아아");
                String newAccessToken = refreshTokenService.refreshRefreshToken(token);
                log.info("새로운 accessToken입니다" + newAccessToken);
                if (newAccessToken != null && !newAccessToken.isEmpty()) {

                    // Request와 Response Header에 새로운 AccessToken 추가
                    request.setAttribute("Authorization", "Bearer " + newAccessToken);
                    response.addHeader("token", newAccessToken);
//                    response.setHeader("Authorization", "Bearer " + newAccessToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
