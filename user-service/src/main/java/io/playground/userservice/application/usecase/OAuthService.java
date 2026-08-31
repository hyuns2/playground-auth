package io.playground.userservice.application.usecase;

import io.playground.userservice.application.dto.UserQueryDto;
import io.playground.userservice.application.port.UserOAuthPersistencePort;
import io.playground.userservice.application.port.UserPersistencePort;
import io.playground.userservice.application.port.UserRolePersistencePort;
import io.playground.userservice.domain.User;
import io.playground.userservice.domain.UserOAuth;
import io.playground.userservice.domain.UserRole;
import io.playground.userservice.exception.BusinessErrorCode;
import io.playground.userservice.exception.BusinessException;
import io.playground.userservice.infrastructure.jwt.model.AuthPrincipal;
import io.playground.userservice.infrastructure.jwt.model.JwtToken;
import io.playground.userservice.infrastructure.jwt.service.JwtTokenService;
import io.playground.userservice.infrastructure.security.dto.GoogleUserInfo;
import io.playground.userservice.infrastructure.security.dto.NaverUserInfo;
import io.playground.userservice.infrastructure.security.dto.OAuthUser;
import io.playground.userservice.infrastructure.security.dto.OAuthUserInfo;
import io.playground.userservice.presentation.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuthService extends DefaultOAuth2UserService {
    private final UserPersistencePort userPort;
    private final UserOAuthPersistencePort userOAuthPort;
    private final UserRolePersistencePort userRolePort;
    private final JwtTokenService jwtTokenService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuthUserInfo oAuthUserInfo = switch (
                userRequest.getClientRegistration()
                        .getRegistrationId()
        ) {
            case "google" -> GoogleUserInfo.of(oAuth2User.getAttributes());
            case "naver" -> NaverUserInfo.of(oAuth2User.getAttributes());
            default -> throw new BusinessException(
                    BusinessErrorCode.UNSUPPORTED_OAUTH
            );
        };

        // 1. 로그인
        UserOAuth userOAuth = signIn(
                oAuthUserInfo.getProvider(),
                oAuthUserInfo.getProviderId()
        ).orElse(null);

        if (userOAuth != null)
            return createOAuthUser(
                    userOAuth.getUserId()
            );

        // 2. 일반 계정과 연동
        // -> 이미 연동된 소셜 타입이면 비즈니스 에러 발생
        userOAuth = connectOAuthIfUserExists(
                oAuthUserInfo
        ).orElse(null);

        if (userOAuth != null)
            return createOAuthUser(
                    userOAuth.getUserId()
            );

        // 3. 신규 회원가입
        userOAuth = signUp(oAuthUserInfo);

        return createOAuthUser(
                userOAuth.getUserId()
        );
    }

    private OAuthUser createOAuthUser(Long userId) {
        UserQueryDto.UserAccessInfo userAccessInfo =
                userRolePort.findUserAccessInfoById(userId)
                        .orElseThrow(
                                () -> new BusinessException(
                                        BusinessErrorCode.USER_NOT_FOUND
                                )
                        );

        return OAuthUser.of(
                AuthPrincipal.of(
                        userId,
                        null,
                        userAccessInfo.status(),
                        userAccessInfo.roles()
                )
        );
    }

    private Optional<UserOAuth> signIn(UserOAuth.ProviderType provider,
                                       String providerId) {
        return userOAuthPort.findByProviderAndProviderId(
                provider, providerId
        );
    }

    private Optional<UserOAuth> connectOAuthIfUserExists(OAuthUserInfo oAuthUserInfo) {
        User user = userPort.findByEmail(oAuthUserInfo.getEmail())
                .orElse(null);

        if (user == null)
            return Optional.empty();

        for (
                UserOAuth.ProviderType provider :
                userOAuthPort.findProvidersByUserId(
                        user.getId()
                )
        )
            if (provider == oAuthUserInfo.getProvider())
                throw new BusinessException(
                        BusinessErrorCode.OAUTH_ALREADY_CONNECTED
                );

        return Optional.of(
                userOAuthPort.save(
                        UserOAuth.of(
                                null,
                                user.getId(),
                                oAuthUserInfo.getProvider(),
                                oAuthUserInfo.getProviderId(),
                                oAuthUserInfo.getEmail()
                        )
                )
        );
    }

    private UserOAuth signUp(OAuthUserInfo oAuthUserInfo) {
        User user = userPort.save(
                User.of(
                        null,
                        oAuthUserInfo.getEmail(),
                        null,
                        User.UserStatus.PENDING,
                        oAuthUserInfo.getName(),
                        false
                )
        );

        userRolePort.save(
                UserRole.of(
                        null,
                        user.getId(),
                        UserRole.RoleType.USER
                )
        );

        return userOAuthPort.save(
                UserOAuth.of(
                        null,
                        user.getId(),
                        oAuthUserInfo.getProvider(),
                        oAuthUserInfo.getProviderId(),
                        oAuthUserInfo.getEmail()
                )
        );
    }

    @Transactional
    public JwtToken wrapUp(String accessToken,
                           UserRequestDto.WrapUp dto) {
        AuthPrincipal authPrincipal = jwtTokenService.parseToken(
                accessToken, "ACCESS"
        );

        User user = userPort.findById(authPrincipal.getUserId())
                .orElseThrow(
                        () -> new BusinessException(
                                BusinessErrorCode.USER_NOT_FOUND
                        )
                );
        if (user.getStatus() != User.UserStatus.PENDING)
            throw new BusinessException(
                    BusinessErrorCode.USER_ALREADY_WRAPPED_UP
            );

        user.updateStatus(User.UserStatus.ACTIVE);
        user.updateProfile(
                dto.name(), dto.pushAgreed()
        );
        userPort.updateStatusAndProfile(user);

        return jwtTokenService.generateToken(
                AuthPrincipal.of(
                        authPrincipal.getUserId(),
                        authPrincipal.getDeviceId(),
                        User.UserStatus.ACTIVE,
                        authPrincipal.getRoles()
                )
        );
    }
}
