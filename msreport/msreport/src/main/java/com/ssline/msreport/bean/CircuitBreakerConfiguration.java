package com.ssline.msreport.bean;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@Configuration
public class CircuitBreakerConfiguration {
    
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> globalCustomCB() {
        var circuitBreakerConfig = CircuitBreakerConfig.custom()
                    .failureRateThreshold(60)
                    .waitDurationInOpenState(Duration.ofSeconds(90))
                    .slidingWindowSize(2)
                    .build();
        
        var timeLimiterConfig = TimeLimiterConfig.custom()
                    .timeoutDuration(Duration.ofSeconds(5))
                    .build();
        
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                    .timeLimiterConfig(timeLimiterConfig)
                    .circuitBreakerConfig(circuitBreakerConfig)
                    .build());
    }

}
