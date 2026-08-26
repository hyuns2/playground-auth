package io.playground.userservice.infrastructure.security.dto;

import io.playground.userservice.domain.UserOAuth;

import java.util.Map;

public record GoogleUserInfo(
        Map<String, Object> attributes
) implements OAuthUserInfo {
    @Override
    public UserOAuth.ProviderType getProvider() {
        return UserOAuth.ProviderType.GOOGLE;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    public static GoogleUserInfo of(Map<String, Object> attributes) {
        return new GoogleUserInfo(attributes);
    }
}
