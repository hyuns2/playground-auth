package io.playground.userservice.application.port;

import io.playground.userservice.domain.User;

import java.util.Optional;

public interface UserPersistencePort {
    boolean existsByEmail(String email);

    Optional<User> findById(Long userId);

    Optional<User> findByEmail(String email);

    User save(User user);

    void updateStatusAndProfile(User user);
}
