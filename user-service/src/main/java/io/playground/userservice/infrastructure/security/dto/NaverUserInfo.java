package io.playground.userservice.infrastructure.security.dto;

import io.playground.userservice.domain.UserOAuth;

import java.util.Map;

public record NaverUserInfo(
        Map<String, Object> attributes
) implements OAuthUserInfo {
    @Override
    public UserOAuth.ProviderType getProvider() {
        return UserOAuth.ProviderType.NAVER;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @SuppressWarnings("unchecked")
    public static NaverUserInfo of(Map<String, Object> attributes) {
        return new NaverUserInfo(
                (Map<String, Object>) attributes.get("response")
        );
    }
}
