package io.playground.userservice.infrastructure.security.dto;

import io.playground.userservice.domain.UserOAuth;

public interface OAuthUserInfo {
    UserOAuth.ProviderType getProvider();
    String getProviderId();
    String getEmail();
    String getName();
}
