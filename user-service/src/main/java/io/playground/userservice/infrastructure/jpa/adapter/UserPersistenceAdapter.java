package io.playground.userservice.infrastructure.jpa.adapter;

import io.playground.userservice.application.port.UserPersistencePort;
import io.playground.userservice.domain.User;
import io.playground.userservice.infrastructure.jpa.entity.UserEntity;
import io.playground.userservice.infrastructure.jpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPersistencePort {
    private final UserRepository userRepository;

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId)
                .map(UserEntity::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserEntity::toDomain);
    }

    @Override
    public User save(User user) {
        return userRepository.save(
                UserEntity.from(user)
        ).toDomain();
    }

    @Override
    public void updateStatusAndProfile(User user) {
        userRepository.updateStatusAndProfile(
                UserEntity.from(user)
        );
    }
}
