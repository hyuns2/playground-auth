package io.playground.userservice.infrastructure.jwt.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class JwtConfig {
    private final JwtProperties jwtProperties;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecretKey getKey() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(
                        jwtProperties.getSecretKey()
                )
        );
    }
}
