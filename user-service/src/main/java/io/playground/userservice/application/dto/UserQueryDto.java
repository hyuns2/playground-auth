package io.playground.userservice.application.dto;

import io.playground.userservice.domain.User;
import io.playground.userservice.domain.UserRole;
import lombok.Builder;

import java.util.List;

public class UserQueryDto {
    @Builder
    public record UserInfo(
            Long userId,
            String password,
            User.UserStatus status,
            List<UserRole.RoleType> roles
    ) {
    }

    @Builder
    public record UserAccessInfo(
            User.UserStatus status,
            List<UserRole.RoleType> roles
    ) {
    }
}
