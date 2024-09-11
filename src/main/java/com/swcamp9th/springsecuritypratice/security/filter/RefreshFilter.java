package com.swcamp9th.springsecuritypratice.security.filter;

import com.swcamp9th.springsecuritypratice.member.command.application.service.RefreshTokenService;
import com.swcamp9th.springsecuritypratice.common.CustomHeaderRequestWrapper;
import com.swcamp9th.springsecuritypratice.security.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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

        String authorizationHeader = request.getHeader("accessToken");
        CustomHeaderRequestWrapper wrappedRequest = new CustomHeaderRequestWrapper(request);

        /* 설명. JWT 토큰이 Request Header에 있는 경우 */
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String refreshToken = request.getHeader("refreshToken").substring(7);

            if (jwtUtil.isTokenExpired(token)) {
                log.info("리프레쉬 토큰 호출되는중입니다아아아아아아아");
                List<String> tokens = refreshTokenService.refreshRefreshToken(refreshToken);

                if (tokens != null && !tokens.isEmpty()) {

                    // Request와 Response Header에 새로운 AccessToken 추가
                    wrappedRequest.addHeader("accessToken", "Bearer " + tokens.get(0));
                    wrappedRequest.addHeader("refreshToken", "Bearer " + tokens.get(1));

                    log.info("리프레쉬 필터에서 새롭게 정의된 accessToken 값 : " + tokens.get(0));
                    log.info("리프레쉬 필터에서 새롭게 정의된 refreshToken 값 : " + tokens.get(1));

                    response.setHeader("accessToken", tokens.get(0));
                    response.setHeader("refreshToken", tokens.get(1));
                    
                    log.info("req, res 갈아끼워짐");
                }
            }
        }

        filterChain.doFilter(wrappedRequest, response);
    }
}
