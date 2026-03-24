package com.ssline.company_fallback;

import java.time.LocalDate;
import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/company")
@Slf4j
public class CompanyController {
    
    private static final Company DEFAULT_COMPANY = Company.builder()
            .id(0L)
            .name("Fallback company")
            .founder("Fallback")
            .logo("default_logo.com")
            .foundationDate(LocalDate.now())
            .webSites(Collections.emptyList())
            .build();
    
    @GetMapping("/{name}")
    public ResponseEntity<Company> get(@PathVariable String name) {
        log.info("GET /company/{}", name);
        return ResponseEntity.ok(DEFAULT_COMPANY);
    }
}
