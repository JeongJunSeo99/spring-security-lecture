package com.swcamp9th.springsecuritypratice.member.command.domain.repository;

import com.swcamp9th.springsecuritypratice.member.command.domain.aggregate.RoleMemberPk;
import com.swcamp9th.springsecuritypratice.member.command.domain.aggregate.entity.RoleMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleMember, RoleMemberPk> {

    List<RoleMember> findByMemberId(Integer memberId);
}
