package com.swcamp9th.springsecuritypratice.security;

import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class CustomUser implements UserDetails {
    private final int id;
    private final String email;
    private final String password;
    private final String memberUniqueId;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUser(int id, String email, String password, String memberUniqueId,
        Collection<? extends GrantedAuthority> authorities) {
        super();
        this.id = id;
        this.email = email;
        this.password = password;
        this.memberUniqueId = memberUniqueId;
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
