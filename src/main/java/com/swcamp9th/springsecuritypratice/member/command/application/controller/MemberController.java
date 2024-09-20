package com.swcamp9th.springsecuritypratice.member.command.application.controller;

import com.swcamp9th.springsecuritypratice.common.ResponseMessage;
import com.swcamp9th.springsecuritypratice.member.command.application.dto.req.ReqSignupDTO;
import com.swcamp9th.springsecuritypratice.member.command.application.service.MemberService;
import com.swcamp9th.springsecuritypratice.member.command.application.service.RefreshTokenService;
import com.swcamp9th.springsecuritypratice.member.command.domain.aggregate.entity.RefreshToken;
import com.swcamp9th.springsecuritypratice.member.command.domain.repository.RefreshTokenRepository;
import com.swcamp9th.springsecuritypratice.security.JwtUtil;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("api/users")
public class MemberController {

    private RefreshTokenService tokenService;
    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService
                          , RefreshTokenService tokenService) {
        this.memberService = memberService;
        this.tokenService = tokenService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseMessage> registUser(@RequestBody ReqSignupDTO newUser){

        memberService.registMember(newUser);

        return ResponseEntity
            .ok()
            .body(
                ResponseMessage.builder()
                    .httpStatus(HttpStatus.CREATED.value())
                    .message("회원 가입 성공")
                    .result(null)
                    .build()
            );
    }

    @PostMapping("token/logout")
    public ResponseEntity<ResponseMessage> logout(@RequestHeader("Authorization") final String accessToken) {

        // 엑세스 토큰으로 현재 Redis 정보 삭제
        tokenService.removeRefreshToken(accessToken);

        return ResponseEntity
            .ok()
            .body(
                ResponseMessage.builder()
                    .httpStatus(HttpStatus.CREATED.value())
                    .message("로그아웃 성공")
                    .result(null)
                    .build()
            );
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<ResponseMessage> refresh(@RequestHeader("Authorization") final String accessToken) {

        String newAccessToken = tokenService.refreshRefreshToken(accessToken);

        return ResponseEntity
            .ok()
            .body(
                ResponseMessage.builder()
                    .httpStatus(HttpStatus.CREATED.value())
                    .message("access token 재발급")
                    .result(newAccessToken)
                    .build()
            );
    }

    @GetMapping("/test")
    public ResponseEntity<ResponseMessage> test(){
        return ResponseEntity
            .ok()
            .body(
                ResponseMessage.builder()
                    .httpStatus(HttpStatus.CREATED.value())
                    .message("테스트")
                    .result(null)
                    .build()
            );
    }

}
