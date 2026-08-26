package io.playground.userservice.infrastructure.jpa.adapter;

import io.playground.userservice.application.port.UserOAuthPersistencePort;
import io.playground.userservice.domain.UserOAuth;
import io.playground.userservice.infrastructure.jpa.entity.UserOAuthEntity;
import io.playground.userservice.infrastructure.jpa.repository.UserOAuthRepository;
import io.playground.userservice.infrastructure.jpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserOAuthPersistenceAdapter implements UserOAuthPersistencePort {
    private final UserOAuthRepository userOAuthRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<UserOAuth> findByProviderAndProviderId(UserOAuth.ProviderType provider,
                                                           String providerId) {
        return userOAuthRepository.findByProviderAndProviderId(
                provider, providerId
        ).map(UserOAuthEntity::toDomain);
    }

    @Override
    public List<UserOAuth.ProviderType> findProvidersByUserId(Long userId) {
        return userOAuthRepository.findProvidersByUserId(userId);
    }

    @Override
    public UserOAuth save(UserOAuth userOAuth) {
        return userOAuthRepository.save(
                UserOAuthEntity.from(
                        userOAuth,
                        userRepository.getReferenceById(
                                userOAuth.getUserId()
                        )
                )
        ).toDomain();
    }

}
