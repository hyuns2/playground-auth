package io.playground.authgateway.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.util.List;

@Component
public class JwtTokenParser {
    private final SecretKey key;
    private final String grantType;

    private final static String DEVICE_ID_CLAIM = "deviceId";
    private final static String STATUS_CLAIM = "status";
    private final static String ROLES_CLAIM = "roles";
    private final static String TOKEN_TYPE_CLAIM = "tokenType";
    private static final String ALLOWED_TOKEN_TYPE = "ACCESS";

    public JwtTokenParser(@Value("${auth.jwt.secret-key}") String secretKey,
                          @Value("${auth.jwt.grant-type}") String grantType) {
        this.key = Keys.hmacShaKeyFor(
                Decoders.BASE64
                        .decode(secretKey)
        );
        this.grantType = grantType;
    }

    public AuthPrincipal parseToken(@Nullable String tokenWithGrantType) {
        String token = resolveToken(tokenWithGrantType);
        Claims claims = parseClaims(token);

        if (
                !ALLOWED_TOKEN_TYPE.equals(
                        claims.get(TOKEN_TYPE_CLAIM, String.class)
                )
        )
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
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

    private String resolveToken(@Nullable String token) {
        String prefix = grantType + " ";

        if (
                StringUtils.hasText(token) &&
                        token.startsWith(prefix)
        )
            return token.substring(prefix.length());
        else
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
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
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "INVALID_TOKEN"
            );
        }
    }
}
