package io.playground.userservice.infrastructure.jpa.entity;

import io.playground.userservice.domain.UserDevice;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
        name = "user_devices",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_userId_deviceId",
                        columnNames = {"user_id", "device_id"}
                )
        }
)
public class UserDeviceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserEntity user;

    @Column(nullable = false)
    private String deviceId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserDevice.DeviceType deviceType;

    @Column(nullable = false)
    private String deviceName;

    @Column(columnDefinition = "DATETIME(6)", nullable = false)
    private Instant lastSeenAt;

    public static UserDeviceEntity from(UserDevice userDevice,
                                        UserEntity user) {
        return new UserDeviceEntity(
                userDevice.getId(),
                user,
                userDevice.getDeviceId(),
                userDevice.getDeviceType(),
                userDevice.getDeviceName(),
                userDevice.getLastSeenAt()
        );
    }

    public UserDevice toDomain() {
        return UserDevice.of(
                id,
                user.getId(),
                deviceId,
                deviceType,
                deviceName,
                lastSeenAt
        );
    }
}
