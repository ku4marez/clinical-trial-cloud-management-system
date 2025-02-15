package com.clinicaltrial.report.controller;

import com.clinicaltrial.report.entity.TrialReportEntity;
import com.clinicaltrial.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<List<TrialReportEntity>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }
}