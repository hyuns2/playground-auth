package io.playground.userservice.infrastructure.jpa.adapter;

import io.playground.userservice.application.dto.UserQueryDto;
import io.playground.userservice.application.port.UserRolePersistencePort;
import io.playground.userservice.domain.UserRole;
import io.playground.userservice.infrastructure.jpa.entity.UserEntity;
import io.playground.userservice.infrastructure.jpa.entity.UserRoleEntity;
import io.playground.userservice.infrastructure.jpa.repository.UserRepository;
import io.playground.userservice.infrastructure.jpa.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRolePersistenceAdapter implements UserRolePersistencePort {
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;

    @Override
    public List<UserRole.RoleType> findRolesByUserId(Long userId) {
        return userRoleRepository.findRolesByUserId(userId);
    }

    @Override
    public Optional<UserQueryDto.UserInfo> findUserInfoByEmail(String email) {
        List<UserRoleEntity> userRoles = userRoleRepository
                .findAllByUserEmail(email);

        UserEntity user;
        try {
            user = userRoles.getFirst().getUser();
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }

        return user != null ?
                Optional.of(
                        UserQueryDto.UserInfo.builder()
                                .userId(user.getId())
                                .password(user.getPassword())
                                .status(user.getStatus())
                                .roles(
                                        userRoles.stream()
                                                .map(UserRoleEntity::getRole)
                                                .toList()
                                )
                                .build()
                ) :
                Optional.empty();
    }

    @Override
    public Optional<UserQueryDto.UserAccessInfo> findUserAccessInfoById(Long userId) {
        List<UserRoleEntity> userRoles = userRoleRepository
                .findAllByUserId(userId);

        UserEntity user;
        try {
            user = userRoles.getFirst().getUser();
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }

        return user != null ?
                Optional.of(
                        UserQueryDto.UserAccessInfo.builder()
                                .status(user.getStatus())
                                .roles(
                                        userRoles.stream()
                                                .map(UserRoleEntity::getRole)
                                                .toList()
                                )
                                .build()
                ) :
                Optional.empty();
    }

    @Override
    public void save(UserRole userRole) {
        userRoleRepository.save(
                UserRoleEntity.from(
                        userRole,
                        userRepository.getReferenceById(
                                userRole.getUserId()
                        )
                )
        );
    }
}
