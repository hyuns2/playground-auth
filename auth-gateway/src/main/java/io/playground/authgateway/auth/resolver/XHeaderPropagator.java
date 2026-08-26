package io.playground.authgateway.auth.resolver;

import io.playground.authgateway.auth.AuthPrincipal;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        prefix = "auth",
        name = "mode",
        havingValue = "x-header"
)
public class XHeaderPropagator implements AuthPrincipalPropagator {
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String DEVICE_ID_HEADER = "X-Device-Id";
    private static final String STATUS_HEADER = "X-Status";
    private static final String ROLES_HEADER = "X-Roles";

    @Override
    public void propagate(ServerHttpRequest request,
                          AuthPrincipal authPrincipal) {
        setHeader(
                request,
                USER_ID_HEADER,
                Long.toString(authPrincipal.userId())
        );
        setHeader(
                request,
                DEVICE_ID_HEADER,
                authPrincipal.deviceId()
        );
        setHeader(
                request,
                STATUS_HEADER,
                authPrincipal.status()
        );
        setHeader(
                request,
                ROLES_HEADER,
                String.join(",", authPrincipal.roles())
        );
    }

    private void setHeader(ServerHttpRequest request,
                           String headerName,
                           String headerValue) {
        request.getHeaders()
                .set(headerName, headerValue);
    }
}
