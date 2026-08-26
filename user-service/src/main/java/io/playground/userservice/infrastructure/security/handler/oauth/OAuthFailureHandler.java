package io.playground.userservice.infrastructure.security.handler.oauth;

import io.playground.userservice.exception.ErrorResponseDto;
import io.playground.userservice.infrastructure.common.ErrorResponseWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuthFailureHandler implements AuthenticationFailureHandler {
    private final ErrorResponseWriter errorResponseWriter;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        errorResponseWriter.write(
                response,
                new ErrorResponseDto(
                        "AUTH-401",
                        "소셜 로그인에 실패했습니다.",
                        exception.getMessage(),
                        HttpStatus.UNAUTHORIZED
                )
        );
    }
}
