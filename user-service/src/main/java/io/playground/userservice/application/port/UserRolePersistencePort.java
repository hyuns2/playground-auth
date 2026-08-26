package io.playground.userservice.application.port;

import io.playground.userservice.application.dto.UserQueryDto;
import io.playground.userservice.domain.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRolePersistencePort {
    List<UserRole.RoleType> findRolesByUserId(Long userId);

    Optional<UserQueryDto.UserInfo> findUserInfoByEmail(String email);

    Optional<UserQueryDto.UserAccessInfo> findUserAccessInfoById(Long userId);

    void save(UserRole userRole);
}
