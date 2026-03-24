package com.ssline.company.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssline.company.entity.Company;
import com.ssline.company.service.ICompanyService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Component
public class CompanyCBListener {
    private final ICompanyService companyService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "consumerCBReport", groupId = "circuitbreaker")
    public void insertMsgEvent(String companyEvent) throws JsonMappingException, JsonProcessingException{
        log.info("Received event circuitBreaker {}", companyEvent);
        var company = objectMapper.readValue(companyEvent, Company.class);
        companyService.create(company);

    }


}
