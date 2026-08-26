package io.playground.authgateway.common;

import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;

public record ErrorResponseDto(
        String code,
        String message,
        @Nullable String details,
        HttpStatus httpStatus
) {
}
