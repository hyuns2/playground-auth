package io.playground.userservice.infrastructure.security.config;

import io.playground.userservice.infrastructure.security.handler.exception.AuthDeniedHandler;
import io.playground.userservice.infrastructure.security.handler.exception.AuthEntryPoint;
import io.playground.userservice.infrastructure.security.handler.oauth.OAuthFailureHandler;
import io.playground.userservice.infrastructure.security.handler.oauth.OAuthSuccessHandler;
import io.playground.userservice.infrastructure.security.handler.signout.SignOutHandler;
import io.playground.userservice.infrastructure.security.handler.signout.SignOutSuccessHandler;
import io.playground.userservice.infrastructure.security.resolver.OAuthRequestResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class SecurityConfig {
    private final OAuthRequestResolver oAuthRequestResolver;
    private final OAuthSuccessHandler oAuthSuccessHandler;
    private final OAuthFailureHandler oAuthFailureHandler;

    private final SignOutHandler signOutHandler;
    private final SignOutSuccessHandler signOutSuccessHandler;

    private final AuthEntryPoint authEntryPoint;
    private final AuthDeniedHandler authDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        httpSecurity
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        configurer -> configurer
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        httpSecurity
                .authorizeHttpRequests(
                        authorizeRequest -> authorizeRequest
                                .anyRequest().permitAll()
                );

        httpSecurity
                .oauth2Login(
                        oauthLogin -> oauthLogin
                                .authorizationEndpoint(
                                        endPoint -> endPoint
                                                .authorizationRequestResolver(oAuthRequestResolver)
                                )
                                .redirectionEndpoint(
                                        endpoint -> endpoint
                                                .baseUri("/oauth2/callback/*")
                                )
                                .successHandler(oAuthSuccessHandler)
                                .failureHandler(oAuthFailureHandler)
                );

        httpSecurity
                .logout(
                        logout -> logout
                                .logoutUrl("/auth/sign-out")
                                .addLogoutHandler(signOutHandler)
                                .logoutSuccessHandler(signOutSuccessHandler)
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
