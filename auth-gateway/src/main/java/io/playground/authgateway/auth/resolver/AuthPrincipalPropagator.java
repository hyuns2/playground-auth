package io.playground.authgateway.auth.resolver;

import io.playground.authgateway.auth.AuthPrincipal;
import org.springframework.http.server.reactive.ServerHttpRequest;

public interface AuthPrincipalPropagator {
    void propagate(ServerHttpRequest request,
                   AuthPrincipal authPrincipal);
}
