package com.ssline.msreport.helper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ssline.msreport.model.Company;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ReportHelper {
    
    @Value("${report.template}")
    private String reportTemplate;

    public String readTemplate(Company company){
        return reportTemplate
            .replace("{company}", company.getName())
            .replace("{foundation_date}", company.getFoundationDate().toString())
            .replace("{founder}", company.getFounder())
            .replace("{web_sites}", company.getWebSites().toString());

    }

    public List<String> getPalaceHoldersFromTemplate(String template){
        var split = template.split("\\{");
        return Arrays.stream(split)
                .filter(line -> !line.isEmpty())
                .map(line -> {
                    var index = line.indexOf("}");
                    return line.substring(0, index);
                })
                .collect(Collectors.toList());

    }
}