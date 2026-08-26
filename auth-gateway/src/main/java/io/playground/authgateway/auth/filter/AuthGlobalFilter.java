package io.playground.authgateway.auth.filter;

import io.playground.authgateway.auth.AuthPrincipal;
import io.playground.authgateway.auth.JwtTokenParser;
import io.playground.authgateway.auth.resolver.AuthPrincipalPropagator;
import io.playground.authgateway.common.ErrorResponseDto;
import io.playground.authgateway.common.ErrorResponseWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Order(-1)
public class AuthGlobalFilter implements GlobalFilter {
    private final JwtTokenParser jwtTokenParser;
    private final AuthPrincipalPropagator authPrincipalPropagator;
    private final ErrorResponseWriter writer;

    private final List<String> excludedPatterns;
    private final PathPatternParser parser;

    public AuthGlobalFilter(JwtTokenParser jwtTokenParser,
                            AuthPrincipalPropagator authPrincipalPropagator,
                            ErrorResponseWriter writer,
                            @Value("${auth.excluded-patterns}") List<String> excludedPatterns) {
        this.jwtTokenParser = jwtTokenParser;
        this.authPrincipalPropagator = authPrincipalPropagator;
        this.writer = writer;

        this.excludedPatterns = excludedPatterns;
        this.parser = new PathPatternParser();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {
        String currentPath = exchange.getRequest()
                .getURI()
                .getPath();
        for (String path : excludedPatterns)
            if (
                    parser.parse(path)
                            .matches(PathContainer.parsePath(currentPath))
            )
                return chain.filter(exchange);

        try {
            AuthPrincipal authPrincipal = jwtTokenParser.parseToken(
                    exchange.getRequest()
                            .getHeaders()
                            .getFirst(HttpHeaders.AUTHORIZATION)
            );
            authPrincipalPropagator.propagate(
                    exchange.getRequest(),
                    authPrincipal
            );
        } catch (Exception e) {
            return writer.write(
                    exchange.getResponse(),
                    new ErrorResponseDto(
                            "AUTH-401",
                            "인증에 실패했습니다.",
                            e.getMessage(),
                            HttpStatus.UNAUTHORIZED
                    )
            );
        }

        return chain.filter(exchange);
    }
}
