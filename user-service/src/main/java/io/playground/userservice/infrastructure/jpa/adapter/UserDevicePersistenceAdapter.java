package io.playground.userservice.infrastructure.jpa.adapter;

import io.playground.userservice.application.port.UserDevicePersistencePort;
import io.playground.userservice.domain.UserDevice;
import io.playground.userservice.infrastructure.jpa.entity.UserDeviceEntity;
import io.playground.userservice.infrastructure.jpa.repository.UserDeviceRepository;
import io.playground.userservice.infrastructure.jpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDevicePersistenceAdapter implements UserDevicePersistencePort {
    private final UserDeviceRepository userDeviceRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<UserDevice> findByUserIdAndDeviceId(Long userId, String deviceId) {
        return userDeviceRepository
                .findByUserIdAndDeviceId(
                        userId, deviceId
                )
                .map(UserDeviceEntity::toDomain);
    }

    @Override
    public List<UserDevice> findAllByUserId(Long userId) {
        return userDeviceRepository
                .findAllByUserId(userId).stream()
                .map(UserDeviceEntity::toDomain)
                .toList();
    }

    @Override
    public UserDevice save(UserDevice userDevice) {
        return userDeviceRepository.save(
                UserDeviceEntity.from(
                        userDevice,
                        userRepository.getReferenceById(
                                userDevice.getUserId()
                        )
                )
        ).toDomain();
    }

    @Override
    public boolean updateLastSeenAtByUserIdAndDeviceId(Long userId,
                                                       String deviceId) {
        return userDeviceRepository
                .updateLastSeenAtByUserIdAndDeviceId(
                        userId,
                        deviceId
                ) == 1;
    }

    @Override
    public void delete(UserDevice userDevice) {
        userDeviceRepository.deleteById(
                userDevice.getId()
        );
    }
}
