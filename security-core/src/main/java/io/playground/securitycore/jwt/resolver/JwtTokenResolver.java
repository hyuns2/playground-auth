package io.playground.securitycore.jwt.resolver;

import io.playground.securitycore.jwt.AuthPrincipal;
import io.playground.securitycore.jwt.JwtTokenParser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

public class JwtTokenResolver implements AuthPrincipalResolver {
    private final JwtTokenParser jwtTokenParser;

    public JwtTokenResolver(JwtTokenParser jwtTokenParser) {
        this.jwtTokenParser = jwtTokenParser;
    }

    @Override
    public AuthPrincipal resolve(HttpServletRequest request) {
        return jwtTokenParser.parseToken(
                request.getHeader(
                        HttpHeaders.AUTHORIZATION
                )
        );
    }
}
