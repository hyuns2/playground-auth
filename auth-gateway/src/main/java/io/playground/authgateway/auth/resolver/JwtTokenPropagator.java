package io.playground.authgateway.auth.resolver;

import io.playground.authgateway.auth.AuthPrincipal;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        prefix = "auth",
        name = "mode",
        havingValue = "jwt-token"
)
public class JwtTokenPropagator implements AuthPrincipalPropagator {
    @Override
    public void propagate(ServerHttpRequest request,
                          AuthPrincipal authPrincipal) {
        return;
    }
}
