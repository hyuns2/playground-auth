package io.playground.userservice.infrastructure.jpa.entity;

import io.playground.userservice.domain.UserOAuth;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
        name = "user_oauths",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_provider_providerId",
                        columnNames = {"provider", "providerId"}
                )
        }
)
public class UserOAuthEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserEntity user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserOAuth.ProviderType provider;

    @Column(nullable = false)
    private String providerId;

    @Column(nullable = false)
    private String email;

    public static UserOAuthEntity from(UserOAuth userOAuth,
                                       UserEntity user) {
        return new UserOAuthEntity(
                userOAuth.getId(),
                user,
                userOAuth.getProvider(),
                userOAuth.getProviderId(),
                userOAuth.getEmail()
        );
    }

    public UserOAuth toDomain() {
        return UserOAuth.of(
                id,
                user.getId(),
                provider,
                providerId,
                email
        );
    }
}
