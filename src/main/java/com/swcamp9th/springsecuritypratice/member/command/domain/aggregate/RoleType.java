package com.swcamp9th.springsecuritypratice.member.command.domain.aggregate;

public enum RoleType {
    ADMIN("ROLE_ADMIN", 1)
    , CUSTOMER("ROLE_CUSTOMER", 2)
    , COMPANY("ROLE_COMPANY", 3)
    ;

    private final String type;
    private final int roleId;

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
