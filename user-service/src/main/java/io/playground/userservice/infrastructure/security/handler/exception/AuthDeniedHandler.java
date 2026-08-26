package io.playground.userservice.infrastructure.security.handler.exception;

import io.playground.userservice.exception.ErrorResponseDto;
import io.playground.userservice.infrastructure.common.ErrorResponseWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthDeniedHandler implements AccessDeniedHandler {
    private final ErrorResponseWriter errorResponseWriter;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        errorResponseWriter.write(
                response,
                new ErrorResponseDto(
                        "AUTH-403",
                        "권한이 없습니다.",
                        accessDeniedException.getMessage(),
                        HttpStatus.FORBIDDEN
                )
        );
    }
}
