package io.playground.securitycore.security.handler;

import io.playground.securitycore.common.ErrorResponseWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class AuthDeniedHandler implements AccessDeniedHandler {
    private final ErrorResponseWriter errorResponseWriter;

    public AuthDeniedHandler(ErrorResponseWriter errorResponseWriter) {
        this.errorResponseWriter = errorResponseWriter;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        errorResponseWriter.write(
                response,
                "AUTH-403",
                "권한이 없습니다.",
                accessDeniedException.getMessage(),
                HttpStatus.FORBIDDEN
        );
    }
}
