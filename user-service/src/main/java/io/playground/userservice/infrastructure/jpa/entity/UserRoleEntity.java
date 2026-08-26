package io.playground.userservice.infrastructure.jpa.entity;

import io.playground.userservice.domain.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "user_roles")
public class UserRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserEntity user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole.RoleType role;

    public static UserRoleEntity from(UserRole userRole,
                                      UserEntity user) {
        return new UserRoleEntity(
                userRole.getId(),
                user,
                userRole.getRole()
        );
    }

    public UserRole toDomain() {
        return UserRole.of(
                id,
                user.getId(),
                role
        );
    }
}
