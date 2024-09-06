package com.swcamp9th.springsecuritypratice.member.command.domain.aggregate;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class RoleMemberPk implements Serializable {
    private int memberId;
    private int roleId;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        RoleMemberPk that = (RoleMemberPk) object;
        return memberId == that.memberId && roleId == that.roleId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, roleId);
    }
}
