package com.ssline.msreport.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import com.ssline.msreport.model.Company;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class CompanyFallbackRepository {
    private final WebClient webClient;
    private final String uri;

    public CompanyFallbackRepository(WebClient webClient, @Value("${fallback.url}") String uri) {
        this.webClient = webClient;
        this.uri = uri;
    }

    @SuppressWarnings("null")
    public Company findByName(String name) {
        log.warn("Calling comapnies fallback {}", uri);
        return webClient.get()
                .uri(uri, name)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Company.class)
                .log()
                .block();
    }
}
