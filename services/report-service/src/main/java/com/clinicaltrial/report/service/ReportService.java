package com.clinicaltrial.report.service;

import com.clinicaltrial.report.entity.TrialReportEntity;
import com.clinicaltrial.report.exception.TrialReportAlreadyExistsException;
import com.clinicaltrial.report.exception.TrialReportNotFoundException;
import com.clinicaltrial.report.repository.TrialReportRepository;
import com.github.ku4marez.commonlibraries.dto.trial.TrialDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final TrialReportRepository trialReportRepository;

    @CircuitBreaker(name = "reportService", fallbackMethod = "fallbackCreateTrialReport")
    public void createTrialReport(TrialDTO trialDTO) {
        log.info("Creating trial report for trial ID: {}", trialDTO.getId());

        TrialReportEntity report = trialReportRepository.findByTrialId(trialDTO.getId())
                .orElse(new TrialReportEntity());

        if (report.getId() != null) {
            throw new TrialReportAlreadyExistsException(report.getId());
        }

        report.setTrialId(trialDTO.getId());
        report.setTrialName(trialDTO.getName());
        report.setReportDate(trialDTO.getStartDate());

        trialReportRepository.save(report);
    }

    @CircuitBreaker(name = "reportService", fallbackMethod = "fallbackUpdateTrialReport")
    public void updateTrialReport(String trialId, String trialName) {
        log.info("Updating trial report for trial ID: {}", trialId);

        TrialReportEntity report = trialReportRepository.findByTrialId(trialId)
                .orElseThrow(() -> new TrialReportNotFoundException(trialId));

        report.setTrialName(trialName);
        report.setReportDate(LocalDate.now());

        trialReportRepository.save(report);
    }

    @CircuitBreaker(name = "reportService", fallbackMethod = "fallbackGetAllReports")
    public List<TrialReportEntity> getAllReports() {
        log.info("Fetching all trial reports.");
        return trialReportRepository.findAll();
    }


    @SuppressWarnings("unused")
    public void fallbackCreateTrialReport(TrialDTO trialDTO, Throwable t) {
        log.warn("Fallback triggered for createTrialReport(). Unable to create report for Trial ID: {}. Reason: {}",
                trialDTO.getId(), t.getMessage());
    }

    @SuppressWarnings("unused")
    public void fallbackUpdateTrialReport(String trialId, String trialName, Throwable t) {
        log.warn("Fallback triggered for updateTrialReport(). Unable to update Trial ID: {}. Reason: {}",
                trialId, t.getMessage());
    }

    @SuppressWarnings("unused")
    public List<TrialReportEntity> fallbackGetAllReports(Throwable t) {
        log.warn("Fallback triggered for getAllReports(). Returning empty list. Reason: {}", t.getMessage());
        return Collections.emptyList();
    }
}
