package com.swcamp9th.springsecuritypratice.security;


import com.swcamp9th.springsecuritypratice.member.command.application.service.MemberService;
import com.swcamp9th.springsecuritypratice.member.command.application.service.RefreshTokenService;
import com.swcamp9th.springsecuritypratice.security.filter.AuthenticationFilter;
import com.swcamp9th.springsecuritypratice.security.filter.JwtFilter;
import com.swcamp9th.springsecuritypratice.security.filter.RefreshFilter;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurity { // extends 방식은 22년부터 막힘

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private MemberService memberService;
    private Environment env;
    private JwtUtil jwtUtil;
    private RefreshTokenService refreshTokenService;

    @Autowired
    public WebSecurity(BCryptPasswordEncoder bCryptPasswordEncoder, MemberService memberService,
        Environment env, JwtUtil jwtUtil, RefreshTokenService refreshTokenService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.memberService = memberService;
        this.env = env;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    /* 설명. Authoriazation(인가) 메소드 (인증 필터 추가) */
    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

        /* 설명. 로그인 시 추가할 authenticationManager(인증 매니저) 만들기 */
        AuthenticationManagerBuilder authenticationManagerBuilder =
            http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(memberService)
                .passwordEncoder(bCryptPasswordEncoder);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        /* 설명. csrf 비활성화
        *       - Token 발행(클라이언트가 책임 부담). 즉, cross site request forgery 공격에 책임 안 진다 
        *         발행되어 클라이언가 보유한 기술로 보안 이수에 책임 해야 함
        * */
        http.csrf((csrf) -> csrf.disable())
            .authorizeHttpRequests((authz) ->
                authz.requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
                  .anyRequest().authenticated()
             )

             /* 설명. authenticationManager 등록 (UserDetails를 상속받는 Service 계층 + BCrypt 암호화) */
             .authenticationManager(authenticationManager)

             /* 설명. session 방식을 사용하지 않음 (JWT Token 방식 사용 시 설정할 내용) */
             .sessionManagement((session)
                -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .addFilter(getAuthenticationFilter(authenticationManager))
            .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new RefreshFilter(jwtUtil, refreshTokenService), JwtFilter.class);
        return http.build();
    }

    private Filter getAuthenticationFilter(AuthenticationManager authenticationManager) {
        return new AuthenticationFilter(authenticationManager, env, jwtUtil, refreshTokenService);
    }

}
