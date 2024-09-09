package com.swcamp9th.springsecuritypratice.member.command.domain.aggregate;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RoleMemberPk implements Serializable {
    private int memberId;
    private int roleId;
}
