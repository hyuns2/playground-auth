package io.playground.securitycore.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.List;

public class JwtTokenParser {
    private final SecretKey key;
    private final String grantType;

    private final static String DEVICE_ID_CLAIM = "deviceId";
    private final static String STATUS_CLAIM = "status";
    private final static String ROLES_CLAIM = "roles";
    private final static String TOKEN_TYPE_CLAIM = "tokenType";

    public JwtTokenParser(SecretKey key,
                          String grantType) {
        this.key = key;
        this.grantType = grantType;
    }

    public AuthPrincipal parseToken(String tokenWithGrantType) {
        String token = resolveToken(tokenWithGrantType);
        Claims claims = parseClaims(token);

        if (
                !claims.get(TOKEN_TYPE_CLAIM, String.class)
                        .equals("ACCESS")
        )
            throw new InsufficientAuthenticationException(
                    "INVALID_TOKEN_TYPE"
            );

        return AuthPrincipal.of(
                Long.parseLong(claims.getSubject()),
                claims.get(DEVICE_ID_CLAIM, String.class),
                claims.get(STATUS_CLAIM, String.class),
                List.of(
                        claims.get(ROLES_CLAIM, String.class)
                                .split(",")
                )
        );
    }

    private String resolveToken(String token) {
        String prefix = grantType + " ";

        if (
                StringUtils.hasText(token) &&
                        token.startsWith(prefix)
        )
            return token.substring(prefix.length());
        else
            throw new InsufficientAuthenticationException(
                    "INVALID_GRANT_TYPE"
            );
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new BadCredentialsException(
                    "INVALID_TOKEN"
            );
        }
    }
}
