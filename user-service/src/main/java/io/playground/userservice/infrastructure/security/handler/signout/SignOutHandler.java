package io.playground.userservice.infrastructure.security.handler.signout;

import io.playground.userservice.infrastructure.jwt.model.AuthPrincipal;
import io.playground.userservice.infrastructure.jwt.persistence.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignOutHandler implements LogoutHandler {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       @Nullable Authentication authentication) {
        if (authentication == null)
            return;

        AuthPrincipal authPrincipal = (AuthPrincipal) authentication.getPrincipal();
        if (authPrincipal == null)
            return;

        refreshTokenRepository.delete(authPrincipal);
    }
}
