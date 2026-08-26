package io.playground.userservice.presentation.controller;

import io.playground.userservice.application.usecase.AuthService;
import io.playground.userservice.infrastructure.jwt.model.JwtToken;
import io.playground.userservice.presentation.dto.UserRequestDto;
import io.playground.userservice.presentation.dto.UserResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@Valid @RequestBody UserRequestDto.SignUp dto) {
        authService.signUp(dto);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<UserResponseDto.SignIn> signIn(@Valid @RequestBody UserRequestDto.SignIn dto) {
        return ResponseEntity.ok(
                authService.signIn(dto)
        );
    }

    @PostMapping("/reissue")
    public ResponseEntity<JwtToken> reissue(@Valid @RequestBody @NotBlank String refreshToken) {
        return ResponseEntity.ok(
                authService.reissueToken(refreshToken)
        );
    }
}
