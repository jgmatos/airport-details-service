package com.sporty.airport.config;

import com.sporty.airport.properties.AviationApiProperties;
import com.sporty.airport.properties.CircuitBreakerProperties;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class ResilienceConfig {

    private final CircuitBreakerProperties circuitBreakerProperties;

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer(AviationApiProperties props) {
        return factory -> factory.configure(
            builder -> builder
                .circuitBreakerConfig(CircuitBreakerConfig.custom()
                    .failureRateThreshold(circuitBreakerProperties.getFailureRate())
                    .waitDurationInOpenState(Duration.ofSeconds(circuitBreakerProperties.getWaitSeconds()))
                    .slidingWindowSize(circuitBreakerProperties.getWindow())
                    .build())
                .timeLimiterConfig(TimeLimiterConfig.custom()
                    .timeoutDuration(Duration.ofSeconds(props.getTimeoutSeconds()))
                    .build()
                ),
                circuitBreakerProperties.getName()
        );
    }
}