package io.playground.userservice.application.port;

import io.playground.userservice.domain.UserOAuth;

import java.util.List;
import java.util.Optional;

public interface UserOAuthPersistencePort {
    Optional<UserOAuth> findByProviderAndProviderId(UserOAuth.ProviderType provider,
                                                    String providerId);

    List<UserOAuth.ProviderType> findProvidersByUserId(Long userId);

    UserOAuth save(UserOAuth userOAuth);
}
