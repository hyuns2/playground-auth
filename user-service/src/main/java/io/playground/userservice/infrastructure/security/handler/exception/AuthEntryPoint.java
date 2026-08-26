package io.playground.userservice.infrastructure.security.handler.exception;

import io.playground.userservice.exception.ErrorResponseDto;
import io.playground.userservice.infrastructure.common.ErrorResponseWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthEntryPoint implements AuthenticationEntryPoint {
    private final ErrorResponseWriter errorResponseWriter;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        errorResponseWriter.write(
                response,
                new ErrorResponseDto(
                        "AUTH-401",
                        "인증에 실패했습니다.",
                        authException.getMessage(),
                        HttpStatus.UNAUTHORIZED
                )
        );
    }
}
