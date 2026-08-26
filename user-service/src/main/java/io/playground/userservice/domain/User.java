package io.playground.userservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {
    private Long id;

    private String email;

    private String password;

    private UserStatus status;

    private String name;

    private boolean pushAgreed;

    public enum UserStatus {
        PENDING, ACTIVE, INACTIVE,
        SUSPENDED, WITHDRAWN
    }

    public static User of(Long id,
                          String email,
                          String password,
                          UserStatus status,
                          String name,
                          boolean pushAgreed) {
        return new User(
                id,
                email,
                password,
                status,
                name,
                pushAgreed
        );
    }

    public void updateStatus(UserStatus status) {
        this.status = status;
    }

    public void updateProfile(String name,
                              boolean pushAgreed) {
        this.name = name;
        this.pushAgreed = pushAgreed;
    }
}
