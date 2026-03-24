package com.ssline.gateway.filter;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import com.ssline.gateway.dto.TokenDTO;

import reactor.core.publisher.Mono;


@Component
public class AuthFilter implements GatewayFilter{

    private final WebClient webClient;
    @Value("${app.auth.validate-uri}")
    private String AUTH_VALIDATE_URI ;
    private static final String ACCESS_TOKEN_HEADER_NAME = "accessToken";

    public AuthFilter(){
        this.webClient = WebClient.builder().build();
    }

    @SuppressWarnings("null")
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
            return onError(exchange);
        }
        final var tokenHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        final var chunks = tokenHeader.split(" ");
        if(chunks.length != 2 || !chunks[0].equals("Bearer")){
            return onError(exchange);
        }
        final var token = chunks[1];
        return webClient.post()
            .uri(AUTH_VALIDATE_URI)
            .header(ACCESS_TOKEN_HEADER_NAME, token)
            .retrieve()
            .bodyToMono(TokenDTO.class)
            .map(response -> exchange)
            .flatMap(chain::filter)
            .onErrorResume(e -> onError(exchange));
        
    }

    private Mono<Void> onError(ServerWebExchange exchange){
        final var response = exchange.getResponse();
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        return response.setComplete();
    }
    
}
