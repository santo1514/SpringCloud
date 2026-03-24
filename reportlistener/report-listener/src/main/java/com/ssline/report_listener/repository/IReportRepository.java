package com.ssline.report_listener.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ssline.report_listener.document.ReportDocument;

public interface IReportRepository extends MongoRepository<ReportDocument, String>{
}
