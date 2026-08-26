package io.playground.userservice.presentation.controller;

import io.playground.userservice.application.usecase.OAuthService;
import io.playground.userservice.infrastructure.jwt.model.JwtToken;
import io.playground.userservice.presentation.dto.UserRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthService oAuthService;

    @PostMapping("wrap-up")
    public ResponseEntity<JwtToken> wrapUp(@RequestHeader("Authorization") String accessToken,
                                           @Valid @RequestBody UserRequestDto.WrapUp dto) {
        return ResponseEntity.ok(
                oAuthService.wrapUp(accessToken, dto)
        );
    }
}
