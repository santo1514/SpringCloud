package com.ssline.gateway.bean;

import java.util.Set;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.ssline.gateway.filter.AuthFilter;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class GatewayConfig {

    private final AuthFilter authFilter;
    
    @Bean
    @Profile("eureka-off")
    public RouteLocator routeLocatorEurekaOff(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(r -> r
                .path("/company-crud/company/*")
                .uri("http://localhost:8081")
            )
            .route(r -> r
                .path("/msreport/report/*")
                .uri("http://localhost:7070")
            ).build();
    }

    @Bean
    @Profile("eureka-on")
    public RouteLocator routeLocatorEurekaOn(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(r -> r
                .path("/company-crud/company/**")
                .uri("lb://company")
            )
            .route(r -> r
                .path("/msreport/report/**")
                .uri("lb://msreport")
            ).build();
    }

    @Bean
    @Profile("eureka-on-cb")
    public RouteLocator routeLocatorEurekaOnCB(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(r -> r
                .path("/company-crud/company/**")
                .filters(f -> {
                    f.circuitBreaker(config -> config
                        .setName("gateway")
                        .setStatusCodes(Set.of("500", "400"))
                        .setFallbackUri("forward:/company.fallback/company/*"));
                    return f;
                })
                .uri("lb://company")
            )
            .route(r -> r
                .path("/msreport/report/**")
                .uri("lb://msreport")
            ).route(r -> r
                .path("/company.fallback/company/**")
                .uri("lb://company.fallback")
            ).build();
    }

    @Bean
    @Profile("oauth2")
    public RouteLocator routeLocatorOauth2(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(r -> r
                .path("/company-crud/company/**")
                .filters(f -> {
                    f.circuitBreaker(config -> config
                        .setName("gateway")
                        .setStatusCodes(Set.of("500", "400"))
                        .setFallbackUri("forward:/company.fallback/company/*"));
                    f.filter(authFilter);
                    return f;
                })
                .uri("lb://company")
            ).route(r -> r
                .path("/msreport/report/**")
                .filters(f -> f.filter(authFilter))
                .uri("lb://msreport")
            ).route(r -> r
                .path("/authserver/auth/**")
                .uri("lb://authserver")
            ).route(r -> r
                .path("/company.fallback/company/**")
                .filters(f -> f.filter(authFilter))
                .uri("lb://company.fallback")
            ).build();
    }
}
