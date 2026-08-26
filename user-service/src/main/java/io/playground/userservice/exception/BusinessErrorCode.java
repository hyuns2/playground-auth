package io.playground.userservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BusinessErrorCode {
    // 400 Bad Request
    USER_ALREADY_EXISTS("USER-400", "이미 존재하는 유저입니다.", HttpStatus.BAD_REQUEST),
    DEVICE_REGISTRATION_REQUIRED("USER-400", "새로운 디바이스로, 등록이 필요합니다. 디바이스 종류와 이름을 입력해주세요.", HttpStatus.BAD_REQUEST),
    UNSUPPORTED_OAUTH("USER-400", "지원하지 않는 소셜로그인입니다.", HttpStatus.BAD_REQUEST),
    OAUTH_ALREADY_CONNECTED("USER-400", "해당 소셜로그인과 이미 연동되어 있습니다.", HttpStatus.BAD_REQUEST),
    USER_ALREADY_WRAPPED_UP("USER-400", "이미 추가 정보 입력이 완료된 유저입니다.", HttpStatus.BAD_REQUEST),

    // 401 Unauthorized
    INVALID_TOKEN("USER-401", "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN_TYPE("USER-401", "토큰 타입(액세스/리프레시)을 다시 확인해주세요.", HttpStatus.UNAUTHORIZED),
    PASSWORD_MISMATCH("USER-401", "비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),

    // 404 Not Found
    USER_NOT_FOUND("USER-404", "유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DEVICE_NOT_FOUND("USER-404", "디바이스를 찾을 수 없습니다. 재로그인 해주세요.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
