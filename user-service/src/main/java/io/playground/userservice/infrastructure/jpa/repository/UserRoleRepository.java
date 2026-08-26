package io.playground.userservice.infrastructure.jpa.repository;

import io.playground.userservice.domain.UserRole;
import io.playground.userservice.infrastructure.jpa.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
    @Query("""
        select ur.role
            from UserRoleEntity ur
        where ur.user.id = :userId
    """)
    List<UserRole.RoleType> findRolesByUserId(Long userId);

    @Query("""
        select e
            from UserRoleEntity e
        join fetch e.user u
        where u.email = :email
    """)
    List<UserRoleEntity> findAllByUserEmail(String email);

    @EntityGraph(attributePaths = {"user"})
    List<UserRoleEntity> findAllByUserId(Long userId);
}
