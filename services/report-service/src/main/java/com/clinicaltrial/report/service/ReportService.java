package com.clinicaltrial.report.service;

import com.clinicaltrial.report.entity.TrialReportEntity;
import com.clinicaltrial.report.exception.TrialReportAlreadyExistsException;
import com.clinicaltrial.report.repository.TrialReportRepository;
import com.github.ku4marez.commonlibraries.dto.trial.TrialDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final TrialReportRepository trialReportRepository;

    public void createTrialReport(TrialDTO trialDTO) {
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

    public void updateTrialReport(String trialId, String trialName) {
        TrialReportEntity report = trialReportRepository.findByTrialId(trialId)
                .orElse(new TrialReportEntity());

        report.setTrialId(trialId);
        report.setTrialName(trialName);
        report.setReportDate(LocalDate.now());

        trialReportRepository.save(report);
    }

    public List<TrialReportEntity> getAllReports() {
        return trialReportRepository.findAll();
    }
}
