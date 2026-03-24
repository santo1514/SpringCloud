package com.ssline.company.service;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.ssline.company.entity.Category;
import com.ssline.company.entity.Company;
import com.ssline.company.exception.NotFoundException;
import com.ssline.company.repository.CompanyRepository;

import io.micrometer.tracing.Tracer;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class CompanyService implements ICompanyService {

    
    private final CompanyRepository companyRepository;
    private final Tracer tracer;


    @Override
    public Company create(Company company) {
        company.getWebSites().forEach(ws -> {
                            if(Objects.isNull(ws.getCategory()))
                                ws.setCategory(Category.NONE);
                            });
        return companyRepository.save(company); 
    }

    @Override
    public Company readByName(String name) {
        var span = tracer.nextSpan().name("readByName");
        try(Tracer.SpanInScope spanInScope = tracer.withSpan(span.start())){
            log.info("Getting company from DB");
        }finally{
            span.end();
        }
        return companyRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Company not found with name: " + name));
    }

    @SuppressWarnings({ "static-access", "null" })
    @Override
    public Company update(Company company, String name) {
        var companyToUpdate = companyRepository.findByName(name).orElseThrow(() -> new NotFoundException("Company not found."));
        companyToUpdate.builder()
            .founder(company.getFounder())
            .logo(company.getLogo())
            .foundationDate(company.getFoundationDate())
            .webSites(company.getWebSites())
            .build();
        return companyRepository.save(companyToUpdate);
        
    }

    @SuppressWarnings("null")
    @Override
    public void delete(String name) {
        var companyToDelete = companyRepository.findByName(name).orElseThrow(() -> new NotFoundException("Company not found."));
        companyRepository.delete(companyToDelete);
    }

    
}
