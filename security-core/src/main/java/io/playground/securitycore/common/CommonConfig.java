package io.playground.securitycore.common;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class CommonConfig {
    @Bean
    @ConditionalOnMissingBean
    public ErrorResponseWriter errorResponseWriter() {
        return new ErrorResponseWriter();
    }
}
