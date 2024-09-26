package com.swcamp9th.springsecuritypratice.security.oauth2.handler;

import com.swcamp9th.springsecuritypratice.member.command.domain.aggregate.RoleType;
import com.swcamp9th.springsecuritypratice.member.command.domain.repository.MemberRepository;
import com.swcamp9th.springsecuritypratice.security.JwtUtil;
import com.swcamp9th.springsecuritypratice.security.oauth2.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        try {
            // CustomOAuth2UserService의 loadUser에서 생성한 객체 반환
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            // User의 Role이 GUEST일 경우 처음 요청한 회원이므로 회원가입 페이지로 리다이렉트
//
            for (RoleType role : oAuth2User.getRole()) {
                if (role == RoleType.GUEST) {
                    HttpSession session = request.getSession();
                    session.setAttribute("tempEmail", oAuth2User.getEmail());

                    String redirectUrl = "http://localhost:5173/signup";
                    response.sendRedirect(redirectUrl);
                } else {
                    loginSuccess(response, oAuth2User);
                    // 로그인 성공 후 프론트엔드의 메인 페이지로 리다이렉트
                    response.sendRedirect("http://localhost:5173/main");

                }
            }

        } catch (Exception e) {
            throw e;
        }

    }

    // TODO : 소셜 로그인 시에도 무조건 토큰 생성하지 말고 JWT 인증 필터처럼 RefreshToken 유/무에 따라 다르게 처리해보기
    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
        String accessToken = jwtUtil.generateAccessToken(oAuth2User.getEmail());
        response.setHeader("accessToken", accessToken);
    }
}