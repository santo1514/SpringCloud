package com.ssline.msreport.service;

public interface IReportService {
    String makeReport(String name);
    String saveReport(String name);
    void deleteReport(String name);
    
}
