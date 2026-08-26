package io.playground.securitycore.common;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class ErrorResponseWriter {
    public void write(HttpServletResponse response,
                      String code,
                      String message,
                      String details,
                      HttpStatus httpStatus) throws IOException {
        response.setStatus(httpStatus.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter()
                .write(
                        writeAsString(
                                code,
                                message,
                                details,
                                httpStatus
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
