package io.playground.userservice.infrastructure.jwt.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.playground.userservice.domain.User;
import io.playground.userservice.domain.UserRole;
import io.playground.userservice.exception.BusinessErrorCode;
import io.playground.userservice.exception.BusinessException;
import io.playground.userservice.infrastructure.jwt.config.JwtProperties;
import io.playground.userservice.infrastructure.jwt.model.AuthPrincipal;
import io.playground.userservice.infrastructure.jwt.model.JwtToken;
import io.playground.userservice.infrastructure.jwt.persistence.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtTokenService {
    private final SecretKey key;
    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    private final static String DEVICE_ID_CLAIM = "deviceId";
    private final static String STATUS_CLAIM = "status";
    private final static String ROLES_CLAIM = "roles";
    private final static String TOKEN_TYPE_CLAIM = "tokenType";

    public JwtToken generateToken(AuthPrincipal authPrincipal) {
        String accessToken = createJwt(
                authPrincipal,
                "ACCESS"
        );
        String refreshToken = createJwt(
                authPrincipal,
                "REFRESH"
        );

        refreshTokenRepository.saveOrUpdate(
                authPrincipal,
                refreshToken
        );
        return JwtToken.of(
                jwtProperties.getGrantType(),
                accessToken,
                refreshToken
        );
    }

    private String createJwt(AuthPrincipal authPrincipal,
                             String tokenType) {
        return Jwts.builder()
                .subject(Long.toString(authPrincipal.getUserId()))
                .claim(DEVICE_ID_CLAIM, authPrincipal.getDeviceId())
                .claim(STATUS_CLAIM, authPrincipal.getStatus().name())
                .claim(
                        ROLES_CLAIM,
                        authPrincipal.getRoles().stream()
                                .map(UserRole.RoleType::name)
                                .collect(Collectors.joining(","))
                )
                .claim(TOKEN_TYPE_CLAIM, tokenType)
                .expiration(
                        tokenType.equals("ACCESS") ?
                                Date.from(
                                        Instant.now()
                                                .plus(
                                                        jwtProperties.getAccessExp(),
                                                        ChronoUnit.MILLIS
                                                )
                                ) :
                                Date.from(
                                        Instant.now()
                                                .plus(
                                                        jwtProperties.getRefreshExp(),
                                                        ChronoUnit.MILLIS
                                                )
                                )
                )
                .signWith(key)
                .compact();
    }

    public AuthPrincipal parseToken(String tokenWithGrantType,
                                    String tokenType) {
        String token = resolveToken(tokenWithGrantType);
        Claims claims = parseClaims(token);

        if (
                !tokenType.equals(
                        claims.get(TOKEN_TYPE_CLAIM, String.class)
                )
        )
            throw new InsufficientAuthenticationException(
                    "INVALID_TOKEN_TYPE"
            );

        String roles = claims.get(ROLES_CLAIM, String.class);
        return AuthPrincipal.of(
                Long.parseLong(claims.getSubject()),
                claims.get(DEVICE_ID_CLAIM, String.class),
                User.UserStatus.valueOf(
                        claims.get(STATUS_CLAIM, String.class)
                ),
                StringUtils.hasText(roles) ?
                        Arrays.stream(
                                roles.split(",")
                                )
                                .map(UserRole.RoleType::valueOf)
                                .toList() :
                        List.of()
        );
    }

    private String resolveToken(String token) {
        String prefix = jwtProperties.getGrantType() + " ";

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

    public AuthPrincipal invalidateRefreshToken(String refreshToken) {
        AuthPrincipal authPrincipal = parseToken(
                refreshToken, "REFRESH"
        );

        String savedRefreshToken = refreshTokenRepository
                .findByAuthPrincipal(authPrincipal)
                .orElseThrow(
                        () -> new BusinessException(
                                BusinessErrorCode.INVALID_TOKEN_TYPE
                        )
                );

        if (
                !savedRefreshToken.equals(
                        resolveToken(refreshToken)
                )
        )
            throw new BusinessException(
                    BusinessErrorCode.INVALID_TOKEN
            );

        refreshTokenRepository.delete(authPrincipal);
        return authPrincipal;
    }
}
