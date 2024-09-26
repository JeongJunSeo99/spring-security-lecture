package com.swcamp9th.springsecuritypratice.member.command.domain.repository;

import com.swcamp9th.springsecuritypratice.member.command.domain.aggregate.entity.Member;
import com.swcamp9th.springsecuritypratice.security.oauth2.SocialType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);

    Optional<Member> findBySocialTypeAndSocialId(SocialType socialType, String id);
}
