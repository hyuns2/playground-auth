package io.playground.userservice.infrastructure.common;

import io.playground.userservice.exception.ErrorResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ErrorResponseWriter {
    public void write(HttpServletResponse response,
                      ErrorResponseDto dto) throws IOException {
        response.setStatus(dto.httpStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter()
                .write(
                        writeAsString(
                                dto.code(),
                                dto.message(),
                                dto.details() != null ?
                                        dto.details() :
                                        null,
                                dto.httpStatus()
                        )
                );
    }

    private String writeAsString(String code,
                                 String message,
                                 String details,
                                 HttpStatus httpStatus) {
        return "{\"code\":\"" + code + "\"," +
                "\"message\":\"" + message + "\"," +
                "\"details\":\"" + details + "\"," +
                "\"httpStatus\":\"" + httpStatus + "\"}";
    }
}
