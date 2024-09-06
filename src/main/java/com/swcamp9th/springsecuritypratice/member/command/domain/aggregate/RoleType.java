package com.swcamp9th.springsecuritypratice.member.command.domain.aggregate;

public enum RoleType {
    ADMIN("ROLE_ADMIN", 1)
    , USER("ROLE_USER", 2);

    private final String type;
    private final Integer roleId;

    RoleType(String type, int roleId) {
        this.type = type;
        this.roleId = roleId;
    }

    public String getType() {
        return type;
    }

    public int getRoleId() {
        return roleId;
    }
}
