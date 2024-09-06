package com.swcamp9th.springsecuritypratice.member.command.application.service;

import com.swcamp9th.springsecuritypratice.member.command.application.dto.req.ReqSignupDTO;

public interface MemberService {

    void registMember(ReqSignupDTO reqSignupDTO);
}
