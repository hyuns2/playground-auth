package io.playground.userservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserOAuth {
    private Long id;

    private Long userId;

    private ProviderType provider;

    private String providerId;

    private String email;

    public enum ProviderType {
        GOOGLE, NAVER
    }

    public static UserOAuth of(Long id,
                               Long userId,
                               ProviderType provider,
                               String providerId,
                               String email) {
        return new UserOAuth(
                id,
                userId,
                provider,
                providerId,
                email
        );
    }
}
