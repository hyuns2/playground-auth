package io.playground.securitycore.security.handler;

import io.playground.securitycore.common.ErrorResponseWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class AuthEntryPoint implements AuthenticationEntryPoint {
    private final ErrorResponseWriter errorResponseWriter;

    public AuthEntryPoint(ErrorResponseWriter errorResponseWriter) {
        this.errorResponseWriter = errorResponseWriter;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        errorResponseWriter.write(
                response,
                "AUTH-401",
                "인증에 실패했습니다.",
                authException.getMessage(),
                HttpStatus.UNAUTHORIZED
        );
    }
}
