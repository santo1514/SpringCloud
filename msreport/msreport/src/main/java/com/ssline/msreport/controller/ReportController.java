package com.ssline.msreport.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssline.msreport.service.ReportService;

import lombok.AllArgsConstructor;



@RestController
@RequestMapping("/report")
@AllArgsConstructor
public class ReportController {
    private ReportService reportService;

    @GetMapping("/{name}")
    public ResponseEntity<Map<String,String>> getReport(@PathVariable String name){
        var response = Map.of("report", reportService.makeReport(name));
        return ResponseEntity.ok(response);
    }

    @PostMapping("")
    public ResponseEntity<String> postReport(@RequestBody String report) {
        return ResponseEntity.ok(reportService.saveReport(report));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteReport(@PathVariable String name) {
        reportService.deleteReport(name);
        return ResponseEntity.noContent().build();
    
    }

}