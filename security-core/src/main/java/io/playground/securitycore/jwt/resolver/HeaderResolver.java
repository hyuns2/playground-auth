package io.playground.securitycore.jwt.resolver;

import io.playground.securitycore.jwt.AuthPrincipal;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public class HeaderResolver implements AuthPrincipalResolver {
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String DEVICE_ID_HEADER = "X-Device-Id";
    private static final String STATUS_HEADER = "X-Status";
    private static final String ROLES_HEADER = "X-Roles";

    public HeaderResolver() {}

    @Override
    public AuthPrincipal resolve(HttpServletRequest request) {
        Long userId = Long.parseLong(
                request.getHeader(USER_ID_HEADER)
        );
        String deviceId = request.getHeader(DEVICE_ID_HEADER);
        String status = request.getHeader(STATUS_HEADER);
        List<String> roles = List.of(
                request.getHeader(ROLES_HEADER).split(",")
        );

        return AuthPrincipal.of(
                userId,
                deviceId,
                status,
                roles
        );
    }
}
