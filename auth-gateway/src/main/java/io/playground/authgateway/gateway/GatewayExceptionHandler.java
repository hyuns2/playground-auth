package io.playground.authgateway.gateway;

import io.playground.authgateway.common.ErrorResponseDto;
import io.playground.authgateway.common.ErrorResponseWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.webflux.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Order(-1)
@RequiredArgsConstructor
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {
    private final ErrorResponseWriter writer;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange,
                             Throwable ex) {
        if (exchange.getResponse().isCommitted())
            return Mono.error(ex);

        if (ex instanceof ResponseStatusException e)
            return writer.write(
                    exchange.getResponse(),
                    new ErrorResponseDto(
                            "GATEWAY-" + e.getStatusCode().value(),
                            "게이트웨이 오류가 발생했습니다.",
                            e.getMessage(),
                            HttpStatus.valueOf(
                                    e.getStatusCode().value()
                            )
                    )
            );

        log.error(
                "Unexpected error. uri={}, method={}",
                exchange.getRequest().getURI(),
                exchange.getRequest().getMethod(),
                ex
        );
        return writer.write(
                exchange.getResponse(),
                new ErrorResponseDto(
                        "GATEWAY-500",
                        "서버 오류가 발생했습니다.",
                        null,
                        HttpStatus.INTERNAL_SERVER_ERROR
                )
        );
    }
}
