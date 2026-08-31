package io.playground.securitycore.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.playground.securitycore.jwt.resolver.AuthPrincipalResolver;
import io.playground.securitycore.jwt.resolver.HeaderResolver;
import io.playground.securitycore.jwt.resolver.JwtTokenResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.InsufficientAuthenticationException;

import javax.crypto.SecretKey;

@Configuration(proxyBeanMethods = false)
public class JwtConfig {
    @Bean
    @ConditionalOnMissingBean
    public SecretKey getKey(@Value("${auth.jwt.secret-key}") String secretKey) {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64
                        .decode(secretKey)
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtTokenParser jwtTokenParser(SecretKey key,
                                         @Value("${auth.jwt.grant-type}") String grantType) {
        return new JwtTokenParser(
                key,
                grantType
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthPrincipalResolver authPrincipalResolver(@Value("${auth.mode}") String resolverType,
                                                       JwtTokenParser jwtTokenParser) {
        return switch (resolverType.toLowerCase()) {
            case "x-header" -> new HeaderResolver();
            case "jwt-token" -> new JwtTokenResolver(
                    jwtTokenParser
            );
            default -> throw new InsufficientAuthenticationException(
                    "INVALID_AUTH_MODE"
            );
        };
    }
}
