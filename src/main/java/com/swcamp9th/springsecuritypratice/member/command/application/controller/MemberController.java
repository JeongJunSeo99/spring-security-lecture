package com.swcamp9th.springsecuritypratice.member.command.application.controller;

import com.swcamp9th.springsecuritypratice.common.ResponseMessage;
import com.swcamp9th.springsecuritypratice.member.command.application.dto.req.ReqSignupDTO;
import com.swcamp9th.springsecuritypratice.member.command.application.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("api/users")
public class MemberController {

    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
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

}
