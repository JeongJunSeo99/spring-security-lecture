package com.swcamp9th.springsecuritypratice.member.command.domain.repository;

import com.swcamp9th.springsecuritypratice.member.command.domain.aggregate.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
