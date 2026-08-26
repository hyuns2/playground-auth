package io.playground.userservice.application.usecase;

import io.playground.userservice.application.dto.UserQueryDto;
import io.playground.userservice.application.port.UserPersistencePort;
import io.playground.userservice.application.port.UserRolePersistencePort;
import io.playground.userservice.domain.User;
import io.playground.userservice.domain.UserRole;
import io.playground.userservice.exception.BusinessErrorCode;
import io.playground.userservice.exception.BusinessException;
import io.playground.userservice.infrastructure.jwt.model.AuthPrincipal;
import io.playground.userservice.infrastructure.jwt.model.JwtToken;
import io.playground.userservice.infrastructure.jwt.provider.JwtTokenService;
import io.playground.userservice.presentation.dto.UserRequestDto;
import io.playground.userservice.presentation.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserPersistencePort userPort;
    private final UserRolePersistencePort userRolePort;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final DeviceService deviceService;

    @Transactional
    public void signUp(UserRequestDto.SignUp dto) {
        if (userPort.existsByEmail(dto.email()))
            throw new BusinessException(
                    BusinessErrorCode.USER_ALREADY_EXISTS
            );

        User user = userPort.save(
                User.of(
                        null,
                        dto.email(),
                        passwordEncoder.encode(
                                dto.password()
                        ),
                        User.UserStatus.ACTIVE,
                        dto.name(),
                        dto.pushAgreed()
                )
        );
        userRolePort.save(
                UserRole.of(
                        null,
                        user.getId(),
                        UserRole.RoleType.USER
                )
        );
    }

    @Transactional
    public UserResponseDto.SignIn signIn(UserRequestDto.SignIn dto) {
        UserQueryDto.UserInfo userInfo =
                userRolePort.findUserInfoByEmail(dto.email())
                        .orElseThrow(
                                () -> new BusinessException(
                                        BusinessErrorCode.USER_NOT_FOUND
                                )
                        );
        if (
                dto.password() == null ||
                        dto.password().isBlank() ||
                        !passwordEncoder.matches(
                                dto.password(), userInfo.password()
                        )
        )
            throw new BusinessException(
                    BusinessErrorCode.PASSWORD_MISMATCH
            );

        String deviceId = deviceService.getOrCreateUserDevice(
                userInfo.userId(), dto.deviceInfo()
        );

        return UserResponseDto.SignIn.builder()
                .token(
                        jwtTokenService.generateToken(
                                AuthPrincipal.of(
                                        userInfo.userId(),
                                        deviceId,
                                        userInfo.status(),
                                        userInfo.roles()
                                )
                        )
                )
                .deviceId(deviceId)
                .build();
    }

    @Transactional
    public JwtToken reissueToken(String refreshToken) {
        AuthPrincipal authPrincipal =
                jwtTokenService.invalidateRefreshToken(refreshToken);

        if (
                !deviceService.updateLastSeenAt(
                        authPrincipal.getUserId(),
                        authPrincipal.getDeviceId()
                )
        )
            throw new BusinessException(
                    BusinessErrorCode.DEVICE_NOT_FOUND
            );

        authPrincipal.renewRoles(
                userRolePort.findRolesByUserId(
                        authPrincipal.getUserId()
                )
        );
        return jwtTokenService.generateToken(
                authPrincipal
        );
    }
}
