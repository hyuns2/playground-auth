package io.playground.userservice.application.port;

import io.playground.userservice.domain.UserDevice;

import java.util.List;
import java.util.Optional;

public interface UserDevicePersistencePort {
    Optional<UserDevice> findByUserIdAndDeviceId(Long userId,
                                                 String deviceId);

    List<UserDevice> findAllByUserId(Long userId);

    UserDevice save(UserDevice userDevice);

    boolean updateLastSeenAtByUserIdAndDeviceId(Long userId,
                                                String deviceId);

    void delete(UserDevice userDevice);
}
