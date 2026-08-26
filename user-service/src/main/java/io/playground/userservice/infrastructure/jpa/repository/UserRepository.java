package io.playground.userservice.infrastructure.jpa.repository;

import io.playground.userservice.infrastructure.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);

    Optional<UserEntity> findById(Long userId);

    Optional<UserEntity> findByEmail(String email);

    @Modifying
    @Query("""
        update UserEntity e
            set e.status = :#{#user.status},
                e.name = :#{#user.name},
                e.pushAgreed = :#{#user.pushAgreed}
        where e.id = :#{#user.id}
    """)
    void updateStatusAndProfile(UserEntity user);
}
