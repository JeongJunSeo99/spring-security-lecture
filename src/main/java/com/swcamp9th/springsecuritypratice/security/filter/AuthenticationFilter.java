package com.swcamp9th.springsecuritypratice.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swcamp9th.springsecuritypratice.member.command.application.dto.req.RequestLoginDTO;
import com.swcamp9th.springsecuritypratice.member.command.application.service.RefreshTokenService;
import com.swcamp9th.springsecuritypratice.security.CustomUser;
import com.swcamp9th.springsecuritypratice.security.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private JwtUtil jwtUtil;
    private RefreshTokenService refreshTokenService;

    public AuthenticationFilter(AuthenticationManager authenticationManager
                              , JwtUtil jwtUtil
                              , RefreshTokenService refreshTokenService)
    {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    /* 설명. 로그인 시도 시 동작하는 기능 (POST /login 요청 시) */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request
                                              , HttpServletResponse response)
                                                throws AuthenticationException
    {
        try {
            RequestLoginDTO reqBody = new ObjectMapper()
                                         .readValue(request.getInputStream(), RequestLoginDTO.class);

            /* 설명. 토큰에 아이디, 비밀번호, 권한 주기 */
            return getAuthenticationManager()
                  .authenticate(new UsernamePasswordAuthenticationToken(
                                    reqBody.getEmail(), reqBody.getPassword(), new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /* 설명. 로그인 성공 시 JWT 토큰 생성하는 기능 */
    @Override
    protected void successfulAuthentication(HttpServletRequest request
                                          , HttpServletResponse response
                                          , FilterChain chain
                                          , Authentication authResult)
                                            throws IOException, ServletException
    {

        CustomUser customUser = (CustomUser) authResult.getPrincipal();

        String accessToken = jwtUtil.generateAccessToken(customUser.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(customUser.getEmail());

        log.info("로그인 시, Refresh token: " + refreshToken);

        response.addHeader("accessToken", accessToken);
//        response.addHeader("refreshToken", refreshToken);
        refreshTokenService.saveTokenInfo(customUser.getEmail(), accessToken, refreshToken);
    }
}
