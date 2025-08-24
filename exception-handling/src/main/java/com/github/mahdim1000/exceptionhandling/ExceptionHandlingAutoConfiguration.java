package com.github.mahdim1000.exceptionhandling;

import com.github.mahdim1000.exceptionhandling.handler.GlobalExceptionHandler;
import com.github.mahdim1000.exceptionhandling.util.MessageResolver;
import com.github.mahdim1000.exceptionhandling.util.TraceIdGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for the exception handling module.
 */
@Configuration
public class ExceptionHandlingAutoConfiguration {

    /**
     * Configure MessageResolver bean if not already present.
     */
    @Bean
    @ConditionalOnMissingBean
    public MessageResolver messageResolver(MessageSource messageSource) {
        return new MessageResolver(messageSource);
    }

    /**
     * Configure TraceIdGenerator bean if not already present.
     */
    @Bean
    @ConditionalOnMissingBean
    public TraceIdGenerator traceIdGenerator() {
        return new TraceIdGenerator();
    }

    /**
     * Configure GlobalExceptionHandler bean if not already present.
     */
    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler(MessageResolver messageResolver, TraceIdGenerator traceIdGenerator) {
        return new GlobalExceptionHandler(messageResolver, traceIdGenerator);
    }
}
