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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/* 필기. UsernamePasswordAuthenticationFilter 이전에 실행. WebSecurity에서 설정
 *       즉, token이 유효하면 사용자 인증, 인가 필터 실행 X 
 */
/* 설명. OncePerRequestFilter를 상속받아 doFilterInternal을 오버라이딩 한다 (한 번만 실행되는 필터) */
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter( JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /* 설명. Request Header에 있는 token이 유효한지 판별 및 인증 (Authentication 객체로 관리) */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("accessToken");

        /* 설명. JWT 토큰이 Request Header에 있는 경우 */
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            log.info("jwt필터에서 파싱한 accessToken 값 " + token);

            if(jwtUtil.validateToken(token)){
                Authentication authentication = jwtUtil.getAuthentication(token);

                /* 설명. 사용자가 가져온 토큰이 유효한지 체크 되면 springSecurity한테 관리하라고 context에 담음
                *       즉, 인증이 완료된 것으로 처리됨. -> 이후 security filter들 실행 안됨
                *  */
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        /* 설명. 위의 if문으로 인증된 Authentication 객체가 principal 객체로 관리되지 않는다면 다음 필터 실행 */
        filterChain.doFilter(request, response); // UsernamePasswordAuthenticationFilter 실행
    }


}
