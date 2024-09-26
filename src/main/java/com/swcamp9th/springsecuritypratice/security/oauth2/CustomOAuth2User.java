package com.swcamp9th.springsecuritypratice.security.oauth2;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;
import static com.fasterxml.jackson.databind.type.LogicalType.Map;

import com.swcamp9th.springsecuritypratice.member.command.domain.aggregate.RoleType;
import com.swcamp9th.springsecuritypratice.member.command.domain.aggregate.entity.RoleMember;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

/**
 * DefaultOAuth2User를 상속하고, email과 role 필드를 추가로 가진다.
 */
@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private String email;
    private List<RoleType> role;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
        Map<String, Object> attributes, String nameAttributeKey,
        String email, List<RoleType> role) {
        super(authorities, attributes, nameAttributeKey);
        this.email = email;
        this.role = role;
    }
}



