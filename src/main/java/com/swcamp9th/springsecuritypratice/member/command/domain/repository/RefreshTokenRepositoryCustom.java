package com.swcamp9th.springsecuritypratice.member.command.domain.repository;

import com.swcamp9th.springsecuritypratice.member.command.domain.aggregate.entity.RefreshToken;
import org.springframework.stereotype.Repository;

public interface RefreshTokenRepositoryCustom {
    RefreshToken findByAccessToken(String accessToken);
}
