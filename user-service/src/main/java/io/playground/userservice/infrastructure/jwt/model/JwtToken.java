package io.playground.userservice.infrastructure.jwt.model;

import lombok.*;

@Getter
@AllArgsConstructor
public class JwtToken {
    private String grantType;

    private String accessToken;

    private String refreshToken;

    public static JwtToken of(String grantType,
                              String accessToken,
                              String refreshToken) {
        return new JwtToken(
                grantType,
                accessToken,
                refreshToken
        );
    }
}
