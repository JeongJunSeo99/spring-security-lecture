package com.swcamp9th.springsecuritypratice.member.command.domain.repository;

import com.swcamp9th.springsecuritypratice.member.command.domain.aggregate.RoleMemberPk;
import com.swcamp9th.springsecuritypratice.member.command.domain.aggregate.entity.RoleMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleMember, RoleMemberPk> {

}
