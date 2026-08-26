package io.playground.userservice.presentation.dto;

import io.playground.userservice.domain.UserDevice;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public class UserRequestDto {
    public record SignUp(
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        String name,
        boolean pushAgreed
    ) {
    }

    public record SignIn(
        @NotBlank
        String email,
        @NotBlank
        String password,
        @Valid
        DeviceInfo deviceInfo
    ) {
    }

    public record DeviceInfo(
        String deviceId,
        UserDevice.DeviceType deviceType,
        String deviceName
    ) {
    }

    public record WrapUp(
            @NotBlank
            String name,
            boolean pushAgreed
    ) {
    }
}
