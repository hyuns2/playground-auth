package io.playground.userservice.presentation.dto;

import io.playground.userservice.infrastructure.jwt.model.JwtToken;
import lombok.Builder;

public class UserResponseDto {
    @Builder
    public record SignIn(
            JwtToken token,
            String deviceId
    ) {
    }
}
