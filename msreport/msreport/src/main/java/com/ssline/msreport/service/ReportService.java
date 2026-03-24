package com.ssline.msreport.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssline.msreport.helper.ReportHelper;
import com.ssline.msreport.model.Company;
import com.ssline.msreport.model.WebSite;
import com.ssline.msreport.repository.CompanyFallbackRepository;
import com.ssline.msreport.repository.ICompanyRepository;
import com.ssline.msreport.stream.ReportPublisher;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class ReportService implements IReportService {

    private final ICompanyRepository companyRepository;
    private final ReportHelper reportHelper;
    private final CompanyFallbackRepository companyFallbackRepository;
    private final Resilience4JCircuitBreakerFactory circuitBreakerFactory;
    private final ReportPublisher reportPublisher;
    private final ObjectMapper objectMapper;
    


    @Override
    public String makeReport(String name){
        var circuitBreaker = circuitBreakerFactory.create("company-circuitbreaker");        
        return circuitBreaker.run(
            () -> makeReportMain(name),
            throwable -> makeReportFallback(name, throwable)
        );
    }
    
    @Override
    public String saveReport(String report) {
        var format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        var placeHolders = reportHelper.getPalaceHoldersFromTemplate(report);
        var circuitBreaker = circuitBreakerFactory.create("company-circuitbreaker-event");  
        var webSites = Stream.of(placeHolders.get(3))
                        .map(ws -> {
                            return WebSite.builder().name(ws).build();
                        })
                        .toList();
        var company = Company.builder()
                            .name(placeHolders.get(0))
                            .foundationDate(LocalDate.parse(placeHolders.get(1), format))
                            .founder(placeHolders.get(2))
                            .webSites(webSites)
                            .build();
        reportPublisher.publishReport(report);
        circuitBreaker.run(
            () -> companyRepository.postByName(company),
            throwable -> reportPublisher.publishCBReport(buildEventMsg(company))
        );
        
        return "Saved";
    }

    @Override
    public void deleteReport(String name) {
        companyRepository.deleteByName(name);
    }
    
    private String makeReportMain(String name){
        return reportHelper.readTemplate(companyRepository.findByName(name).orElseThrow());
    }

    private String makeReportFallback(String name, Throwable error){
        log.warn(error.getMessage());
        return reportHelper.readTemplate(companyFallbackRepository.findByName(name));
    }

    private String buildEventMsg(Company company){
        try {
            return objectMapper.writeValueAsString(company);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

}
