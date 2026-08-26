package io.playground.userservice.infrastructure.jwt.model;

import io.playground.userservice.domain.User;
import io.playground.userservice.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AuthPrincipal {
    private Long userId;

    private String deviceId;

    private User.UserStatus status;

    private List<UserRole.RoleType> roles;

    public static AuthPrincipal of(Long userId,
                                   String deviceId,
                                   User.UserStatus status,
                                   List<UserRole.RoleType> roles) {
        return new AuthPrincipal(
                userId,
                deviceId,
                status,
                roles
        );
    }

    public void updateDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void renewRoles(List<UserRole.RoleType> roles) {
        this.roles = roles;
    }
}
