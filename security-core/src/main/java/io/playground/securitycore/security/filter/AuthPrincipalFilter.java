package io.playground.securitycore.security.filter;

import io.playground.securitycore.jwt.AuthPrincipal;
import io.playground.securitycore.jwt.resolver.AuthPrincipalResolver;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.server.PathContainer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.pattern.PathPatternParser;

import java.io.IOException;
import java.util.List;

public class AuthPrincipalFilter extends OncePerRequestFilter {
    private final AuthPrincipalResolver authPrincipalResolver;
    private final List<String> excludedPatterns;

    private final PathPatternParser parser = new PathPatternParser();

    public AuthPrincipalFilter(AuthPrincipalResolver authPrincipalResolver,
                               List<String> excludedPatterns) {
        this.authPrincipalResolver = authPrincipalResolver;
        this.excludedPatterns = excludedPatterns;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String currentPath = request.getRequestURI();
        for (String path : excludedPatterns)
            if (
                    parser.parse(path)
                            .matches(PathContainer.parsePath(currentPath))
            ) {
                filterChain.doFilter(request, response);
                return;
            }

        AuthPrincipal authPrincipal = authPrincipalResolver.resolve(request);
        SecurityContextHolder.getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                authPrincipal,
                                null,
                                authPrincipal.roles().stream()
                                        .map(SimpleGrantedAuthority::new)
                                        .toList()
                        )
                );

        filterChain.doFilter(request, response);
    }
}
