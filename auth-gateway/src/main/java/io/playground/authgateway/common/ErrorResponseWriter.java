package io.playground.authgateway.common;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class ErrorResponseWriter {
    private final ObjectMapper objectMapper;

    public Mono<Void> write(ServerHttpResponse response,
                            ErrorResponseDto dto) {
        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(
                    dto
            );
        } catch (Exception e) {
            bytes = fallback(dto).getBytes(
                    StandardCharsets.UTF_8
            );
        }

        response.setStatusCode(dto.httpStatus());
        response.getHeaders()
                .setContentType(
                        MediaType.APPLICATION_JSON
                );
        return response.writeWith(
                Mono.just(
                        response.bufferFactory()
                                .wrap(bytes)
                )
        );
    }

    private String fallback(ErrorResponseDto dto) {
        return "{\"code\":\"" + dto.code() + "\"," +
                "\"message\":\"" + dto.message() + "\"," +
                "\"details\":\"" + dto.details() + "\"," +
                "\"httpStatus\":\"" + dto.httpStatus() + "\"}";
    }
}
