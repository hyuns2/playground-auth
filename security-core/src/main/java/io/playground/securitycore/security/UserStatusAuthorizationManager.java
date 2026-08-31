package io.playground.securitycore.security;

import io.playground.securitycore.jwt.AuthPrincipal;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.List;
import java.util.function.Supplier;

public class UserStatusAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private final List<String> allowedStatuses;

    public UserStatusAuthorizationManager(List<String> allowedStatuses) {
        this.allowedStatuses = allowedStatuses;
    }

    @Override
    public @Nullable AuthorizationResult authorize(Supplier<? extends @Nullable Authentication> authentication,
                                                   RequestAuthorizationContext object) {
        Authentication auth = authentication.get();
        if (
                auth == null ||
                        !(auth.getPrincipal() instanceof
                                AuthPrincipal authPrincipal)
        )
            return new AuthorizationDecision(false);

        return new AuthorizationDecision(
                        allowedStatuses.contains(
                                authPrincipal.status()
                        )
                );
    }
}
