package io.playground.userservice.application.usecase;

import io.playground.userservice.application.port.UserDevicePersistencePort;
import io.playground.userservice.domain.UserDevice;
import io.playground.userservice.exception.BusinessErrorCode;
import io.playground.userservice.exception.BusinessException;
import io.playground.userservice.presentation.dto.UserRequestDto;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeviceService {
    private final UserDevicePersistencePort userDevicePort;
    private final int maxDeviceCount;

    public DeviceService(UserDevicePersistencePort userDevicePort,
                         @Value("${app.device.max-count}") int maxDeviceCount) {
        this.userDevicePort = userDevicePort;
        this.maxDeviceCount = maxDeviceCount;
    }

    @Transactional
    public String getOrCreateUserDevice(Long userId,
                                        UserRequestDto.DeviceInfo deviceInfo) {
        UserDevice userDevice = getUserDevice(
                userId, deviceInfo.deviceId()
        ).orElseGet(
                () -> createUserDevice(
                        userId, deviceInfo
                )
        );

        updateLastSeenAt(
                userDevice.getUserId(),
                userDevice.getDeviceId()
        );

        return userDevice.getDeviceId();
    }

    @Transactional
    public boolean updateLastSeenAt(Long userId,
                                    String deviceId) {
        return userDevicePort.updateLastSeenAtByUserIdAndDeviceId(
                userId, deviceId
        );
    }

    private Optional<UserDevice> getUserDevice(Long userId,
                                               @Nullable String deviceId) {
        if (deviceId == null || deviceId.isBlank())
            return Optional.empty();

        return userDevicePort.findByUserIdAndDeviceId(
                userId, deviceId
        );
    }

    private UserDevice createUserDevice(Long userId,
                                        UserRequestDto.DeviceInfo deviceInfo) {
        if (
                deviceInfo.deviceType() == null ||
                deviceInfo.deviceName() == null ||
                deviceInfo.deviceName().isBlank()
        )
            throw new BusinessException(
                    BusinessErrorCode.DEVICE_REGISTRATION_REQUIRED
            );

        List<UserDevice> userDevices = new ArrayList<>(
                userDevicePort.findAllByUserId(userId)
        );
        while (userDevices.size() >= maxDeviceCount) {
            Optional<UserDevice> oldestUserDevice = userDevices.stream()
                    .min(
                            Comparator.comparing(
                                    UserDevice::getLastSeenAt
                            )
                    );
            if (oldestUserDevice.isEmpty())
                break;

            userDevicePort.delete(oldestUserDevice.get());
            userDevices.remove(oldestUserDevice.get());
        }

        return userDevicePort.save(
                UserDevice.of(
                        null,
                        userId,
                        UUID.randomUUID().toString(),
                        deviceInfo.deviceType(),
                        deviceInfo.deviceName(),
                        Instant.now()
                )
        );
    }
}
