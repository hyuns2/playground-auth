package io.playground.userservice.infrastructure.security.dto;

import io.playground.userservice.infrastructure.jwt.model.AuthPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public record OAuthUser(
        AuthPrincipal authPrincipal
) implements OAuth2User {
    public static OAuthUser of(AuthPrincipal authPrincipal) {
        return new OAuthUser(authPrincipal);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authPrincipal.getRoles().stream()
                .map(role -> (GrantedAuthority) role::name)
                .toList();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public String getName() {
        return authPrincipal.getUserId()
                .toString();
    }
}
