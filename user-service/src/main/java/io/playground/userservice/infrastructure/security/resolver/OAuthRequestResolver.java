package io.playground.userservice.infrastructure.security.resolver;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class OAuthRequestResolver implements OAuth2AuthorizationRequestResolver {
    private final DefaultOAuth2AuthorizationRequestResolver defaultResolver;

    private static final String DEVICE_INFO_PARAM = "deviceInfo";

    public OAuthRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(
                clientRegistrationRepository
        );
    }

    @Override
    public @Nullable OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest authorizationRequest =
                defaultResolver.resolve(request);

        return authorizationRequest != null ?
                OAuth2AuthorizationRequest.from(
                        authorizationRequest
                ).state(
                        createState(
                                request.getParameter(DEVICE_INFO_PARAM)
                        )
                ).build() :
                null;
    }

    @Override
    public @Nullable OAuth2AuthorizationRequest resolve(HttpServletRequest request,
                                                        String clientRegistrationId) {
        OAuth2AuthorizationRequest authorizationRequest =
                defaultResolver.resolve(
                        request, clientRegistrationId
                );

        return authorizationRequest != null ?
                OAuth2AuthorizationRequest.from(
                        authorizationRequest
                ).state(
                        createState(
                                request.getParameter(DEVICE_INFO_PARAM)
                        )
                ).build() :
                null;
    }

    private String createState(String deviceInfoDto) {
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(
                        deviceInfoDto.getBytes()
                );
    }
}
