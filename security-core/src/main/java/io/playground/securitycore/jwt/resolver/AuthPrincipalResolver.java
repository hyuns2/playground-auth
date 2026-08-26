package io.playground.securitycore.jwt.resolver;

import io.playground.securitycore.jwt.AuthPrincipal;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthPrincipalResolver {
    AuthPrincipal resolve(HttpServletRequest request);
}
