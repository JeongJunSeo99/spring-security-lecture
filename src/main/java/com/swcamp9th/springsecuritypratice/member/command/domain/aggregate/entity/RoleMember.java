package com.swcamp9th.springsecuritypratice.member.command.domain.aggregate.entity;

import com.swcamp9th.springsecuritypratice.member.command.domain.aggregate.RoleMemberPk;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_member_role")
@IdClass(RoleMemberPk.class)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleMember {

    @Id
    @Column(name = "member_id")
    private Integer memberId;

    @Id
    @Column(name = "role_id")
    private Integer roleId;
}
