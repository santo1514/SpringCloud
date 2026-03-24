package com.ssline.company.controllers;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssline.company.entity.Company;
import com.ssline.company.service.CompanyService;

import io.micrometer.core.annotation.Timed;
import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@AllArgsConstructor
@RequestMapping("/company")
@Slf4j
@Tag(name = "Company resources")
public class CompanyController {
    
    private final CompanyService companyService;

    @Operation(summary = "Get - Company by name")
    @GetMapping("/{name}")
    @Observed(name = "company.name")
    @Timed(value = "company.name")
    public ResponseEntity<Company> get(@PathVariable String name) {
        // try {
        //     Thread.sleep(7000);
        // } catch (InterruptedException e) {
        //    throw new RuntimeException(e);
        // }
        log.info("GET /company/{}", name);
        return ResponseEntity.ok(companyService.readByName(name));
    }

    @Operation(summary = "Post - Create company")
    @SuppressWarnings("null")
    @PostMapping("/")
    @Observed(name = "company.save")
    @Timed(value = "company.save")
    public ResponseEntity<Company> create(@RequestBody Company company) {
        log.info("POST /company/", company.getName());
        return ResponseEntity.created(URI.create(companyService.create(company).getName())).build();
    }

    @Operation(summary = "Put - Update company by name")
    @PutMapping("/{name}")
    public ResponseEntity<Company> update(@PathVariable String name, @RequestBody Company company){
        log.info("PUT /company/{}", name);
        return ResponseEntity.ok(companyService.update(company, name));
    }

    @Operation(summary = "Delete - Remove company by name")
    @DeleteMapping("/{name}")
    public ResponseEntity<?> delete(@PathVariable String name){
        log.info("DELETE /company/{}", name);
        companyService.delete(name);
        return ResponseEntity.noContent().build();  
    }

}
