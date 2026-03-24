package com.ssline.report_listener.stream;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ssline.report_listener.document.ReportDocument;
import com.ssline.report_listener.repository.IReportRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@AllArgsConstructor
public class ReportListener {
    
    private final IReportRepository reportRepository;

    @SuppressWarnings("null")
    @Bean
    public Consumer<String> consumerReport() {
        return report -> {            
            reportRepository.save(ReportDocument.builder().content(report).build());
            log.info("Saving report: {}", report);
        };
    }
}
