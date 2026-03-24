package com.ssline.msreport.stream;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import com.ssline.msreport.model.Company;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ReportPublisher {
    
    private final StreamBridge streamBridge;

    public void publishReport(String report) {
        streamBridge.send("consumerReport", report);
        streamBridge.send("consumerReport-in-0", report);
        streamBridge.send("consumerReport-out-0", report);
    }

    public Company publishCBReport(String company) {
        streamBridge.send("consumerCBReport", company);
        streamBridge.send("consumerCBReport-in-0", company);
        streamBridge.send("consumerCBReport-out-0", company);
        return Company.builder().build();
    }

    

}
