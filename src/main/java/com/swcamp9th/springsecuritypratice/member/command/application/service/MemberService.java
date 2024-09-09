package com.swcamp9th.springsecuritypratice.member.command.application.service;

import com.swcamp9th.springsecuritypratice.member.command.application.dto.req.ReqSignupDTO;
import com.swcamp9th.springsecuritypratice.member.command.domain.aggregate.entity.Member;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberService extends UserDetailsService {

    void registMember(ReqSignupDTO reqSignupDTO);

    Member findMember(String email);
}
