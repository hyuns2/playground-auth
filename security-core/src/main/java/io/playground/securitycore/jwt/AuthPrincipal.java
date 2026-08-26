package io.playground.securitycore.jwt;

import java.util.List;

public record AuthPrincipal(
        Long userId,
        String deviceId,
        String status,
        List<String> roles

) {
    public static AuthPrincipal of(Long userId,
                                   String deviceId,
                                   String status,
                                   List<String> roles) {
            return new AuthPrincipal(
                    userId,
                    deviceId,
                    status,
                    roles
            );
    }
}
