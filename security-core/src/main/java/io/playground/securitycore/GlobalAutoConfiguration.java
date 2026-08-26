package io.playground.securitycore;

import io.playground.securitycore.common.CommonConfig;
import io.playground.securitycore.jwt.JwtConfig;
import io.playground.securitycore.security.SecurityConfig;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@AutoConfiguration
@EnableMethodSecurity
@Import({
        CommonConfig.class,
        JwtConfig.class,
        SecurityConfig.class
})
public class GlobalAutoConfiguration {
}
