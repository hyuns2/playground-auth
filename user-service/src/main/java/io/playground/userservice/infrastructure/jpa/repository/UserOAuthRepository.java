package io.playground.userservice.infrastructure.jpa.repository;

import io.playground.userservice.domain.UserOAuth;
import io.playground.userservice.infrastructure.jpa.entity.UserOAuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserOAuthRepository extends JpaRepository<UserOAuthEntity, Long> {
    Optional<UserOAuthEntity> findByProviderAndProviderId(UserOAuth.ProviderType provider,
                                                          String providerId);

    @Query("""
        SELECT e.provider
            FROM UserOAuthEntity e
        WHERE e.user.id = :userId
    """)
    List<UserOAuth.ProviderType> findProvidersByUserId(Long userId);
}
