package com.swcamp9th.springsecuritypratice.member.command.domain.repository;

import com.swcamp9th.springsecuritypratice.member.command.domain.aggregate.entity.RefreshToken;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenRepositoryCustomImpl implements RefreshTokenRepositoryCustom {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String HASH_KEY = "jwtToken";

    @Override
    public RefreshToken findByAccessToken(String accessToken) {
        // Redis에서 모든 엔트리를 가져온 후 필터링
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(HASH_KEY);

        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            RefreshToken token = (RefreshToken) entry.getValue();
            if (token.getAccessToken().equals(accessToken)) {
                return token;
            }
        }
        return null; // 찾지 못한 경우 null 반환
    }
}
