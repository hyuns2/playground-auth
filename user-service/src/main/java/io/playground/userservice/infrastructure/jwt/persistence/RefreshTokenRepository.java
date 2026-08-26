package io.playground.userservice.infrastructure.jwt.persistence;

import io.playground.userservice.infrastructure.jwt.config.JwtProperties;
import io.playground.userservice.infrastructure.jwt.model.AuthPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {
    private final StringRedisTemplate redisTemplate;
    private final JwtProperties jwtProperties;

    private String getKeyName(AuthPrincipal authPrincipal) {
        return "refresh:" +
                ":user:" + authPrincipal.getUserId() +
                ":device:" + authPrincipal.getDeviceId();
    }

    public void saveOrUpdate(AuthPrincipal authPrincipal,
                             String refreshToken) {
        redisTemplate.opsForValue().set(
                getKeyName(authPrincipal),
                refreshToken,
                Duration.ofMillis(jwtProperties.getRefreshExp())
        );
    }

    public Optional<String> findByAuthPrincipal(AuthPrincipal authPrincipal) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(
                        getKeyName(authPrincipal)
                )
        );
    }

    public void delete(AuthPrincipal authPrincipal) {
        redisTemplate.delete(
                getKeyName(authPrincipal)
        );
    }
}
