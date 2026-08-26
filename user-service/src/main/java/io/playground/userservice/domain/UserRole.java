package io.playground.userservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRole {
    private Long id;

    private Long userId;

    private RoleType role;

    public enum RoleType {
        USER, ADMIN
    }

    public static UserRole of(Long id,
                              Long userId,
                              RoleType roleType) {
        return new UserRole(
                id,
                userId,
                roleType
        );
    }
}
