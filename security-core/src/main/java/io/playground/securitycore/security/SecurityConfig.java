package io.playground.securitycore.security;

import io.playground.securitycore.common.ErrorResponseWriter;
import io.playground.securitycore.jwt.resolver.AuthPrincipalResolver;
import io.playground.securitycore.security.filter.AuthPrincipalFilter;
import io.playground.securitycore.security.handler.AuthDeniedHandler;
import io.playground.securitycore.security.handler.AuthEntryPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class SecurityConfig {
    @Bean
    @ConditionalOnMissingBean
    public AuthPrincipalFilter authPrincipalFilter(AuthPrincipalResolver authPrincipalResolver,
                                                   @Value("${auth.excluded-patterns}") List<String> excludedPatterns) {
        return new AuthPrincipalFilter(
                authPrincipalResolver,
                excludedPatterns
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public UserStatusAuthorizationManager userStatusAuthorizationManager(@Value("${auth.allowed-statuses}") List<String> allowedStatuses) {
        return new UserStatusAuthorizationManager(
                allowedStatuses
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthEntryPoint authEntryPoint(ErrorResponseWriter errorResponseWriter) {
        return new AuthEntryPoint(
                errorResponseWriter
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthDeniedHandler authDeniedHandler(ErrorResponseWriter errorResponseWriter) {
        return new AuthDeniedHandler(
                errorResponseWriter
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity,
                                           AuthPrincipalFilter authPrincipalFilter,
                                           UserStatusAuthorizationManager userStatusAuthorizationManager,
                                           @Value("${auth.excluded-patterns}") List<String> excludedPatterns,
                                           AuthEntryPoint authEntryPoint,
                                           AuthDeniedHandler authDeniedHandler) {
        httpSecurity
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        configurer -> configurer
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        httpSecurity
                .addFilterBefore(
                        authPrincipalFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers(
                                        excludedPatterns.toArray(new String[0])
                                ).permitAll()
                                .anyRequest().access(userStatusAuthorizationManager)
                );

        httpSecurity
                .exceptionHandling(
                        configurer -> configurer
                                .authenticationEntryPoint(authEntryPoint)
                                .accessDeniedHandler(authDeniedHandler)
                );

        return httpSecurity.build();
    }
}
