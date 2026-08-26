package io.playground.userservice.infrastructure.jpa.entity;

import io.playground.userservice.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private User.UserStatus status;

    @Column
    private String name;

    @Column(nullable = false)
    private boolean pushAgreed;

    public static UserEntity from(User user) {
        return new UserEntity(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getStatus(),
                user.getName(),
                user.isPushAgreed()
        );
    }

    public User toDomain() {
        return User.of(
                id,
                email,
                password,
                status,
                name,
                pushAgreed
        );
    }
}
