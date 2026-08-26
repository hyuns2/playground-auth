package io.playground.userservice.infrastructure.security.handler.oauth;

import io.playground.userservice.application.usecase.DeviceService;
import io.playground.userservice.infrastructure.jwt.model.AuthPrincipal;
import io.playground.userservice.infrastructure.jwt.provider.JwtTokenService;
import io.playground.userservice.infrastructure.security.dto.OAuthUser;
import io.playground.userservice.presentation.dto.UserRequestDto;
import io.playground.userservice.presentation.dto.UserResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {
    private final DeviceService deviceService;
    private final JwtTokenService jwtTokenService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        AuthPrincipal authPrincipal = ((OAuthUser) Objects.requireNonNull(
                authentication.getPrincipal()
        )).authPrincipal();

        String deviceId = deviceService.getOrCreateUserDevice(
                authPrincipal.getUserId(),
                objectMapper.readValue(
                        Base64.getUrlDecoder().decode(
                                request.getParameter("state")
                        ),
                        UserRequestDto.DeviceInfo.class
                )
        );
        authPrincipal.updateDeviceId(deviceId);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter()
                .write(
                        objectMapper.writeValueAsString(
                                UserResponseDto.SignIn.builder()
                                        .token(
                                                jwtTokenService.generateToken(authPrincipal)
                                        )
                                        .deviceId(deviceId)
                                        .build()
                        )
                );
    }
}
