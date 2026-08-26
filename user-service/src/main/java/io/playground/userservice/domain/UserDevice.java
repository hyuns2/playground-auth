package io.playground.userservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class UserDevice {
    private Long id;

    private Long userId;

    private String deviceId;

    private DeviceType deviceType;

    private String deviceName;

    private Instant lastSeenAt;

    public enum DeviceType {
        WEB, IOS, ANDROID
    }

    public static UserDevice of(Long id,
                                Long userId,
                                String deviceId,
                                DeviceType deviceType,
                                String deviceName,
                                Instant lastSeenAt) {
        return new UserDevice(
                id,
                userId,
                deviceId,
                deviceType,
                deviceName,
                lastSeenAt
        );
    }
}
