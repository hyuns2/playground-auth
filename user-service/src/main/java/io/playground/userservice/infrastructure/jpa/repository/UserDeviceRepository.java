package io.playground.userservice.infrastructure.jpa.repository;

import io.playground.userservice.infrastructure.jpa.entity.UserDeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserDeviceRepository extends JpaRepository<UserDeviceEntity, Long> {
   Optional<UserDeviceEntity> findByUserIdAndDeviceId(Long userId, String deviceId);

   List<UserDeviceEntity> findAllByUserId(Long userId);

   @Modifying
   @Query("""
        update UserDeviceEntity e
            set e.lastSeenAt = now()
        where e.user.id = :userId
            and e.deviceId = :deviceId
    """)
   int updateLastSeenAtByUserIdAndDeviceId(Long userId,
                                           String deviceId);

    @Modifying
    @Query("""
        delete UserDeviceEntity e
        where e.id = :userDeviceId
    """)
    void deleteById(Long userDeviceId);
}
