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

    private Environment env;
    private JwtUtil jwtUtil;
    private RefreshTokenService refreshTokenService;

    public AuthenticationFilter(AuthenticationManager authenticationManager
                              , Environment environment
                              , JwtUtil jwtUtil
                              , RefreshTokenService refreshTokenService)
    {
        super(authenticationManager);
        this.env = environment;
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

//        log.info("로그인 성공하고 security가 관리하는 principalr객체(authResult) : {}", authResult);
//
//        /* 설명. 로그인 이후 관리되고 있는 Authentication 갹체를 활용해 JWT Token 만들기 */
//        log.info("secret key : {}", env.getProperty("token.secret"));

        CustomUser customUser = (CustomUser) authResult.getPrincipal();

        String accessToken = jwtUtil.generateAccessToken(customUser.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(customUser.getEmail());

        log.info("로그인 시, Refresh token: " + refreshToken);
//        /* 설명. 재료들로 토큰 만들기(JWT Token 라이브러리 추가(3가지) 하기 */
//        Claims claims = Jwts.claims().setSubject(customUser.getUsername());
//
//        /* 필기. 비공개 클레임 추가하는 과정*/
//        claims.put("auth", customUser.getAuthorities().stream().map(role -> role.getAuthority())
//                                                               .collect(Collectors.toList()));
//        claims.put("email", customUser.getEmail());
//        claims.put("userUniqueId", customUser.getMemberUniqueId());
//
//        String token = Jwts.builder()
//            .setClaims(claims)
//            .setExpiration(new Date(System.currentTimeMillis()
//                + Long.parseLong(env.getProperty("token.expiration_time"))))
//            .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
//            .compact();

        response.addHeader("token", accessToken);
        refreshTokenService.saveTokenInfo(customUser.getEmail(), accessToken, refreshToken);
    }
}
