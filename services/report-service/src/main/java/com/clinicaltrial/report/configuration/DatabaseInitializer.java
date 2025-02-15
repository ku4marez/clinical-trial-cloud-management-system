package com.clinicaltrial.report.configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import com.clinicaltrial.report.entity.TrialReportEntity;
import com.clinicaltrial.report.repository.TrialReportRepository;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer {

    private final TrialReportRepository trialReportRepository;

    @PostConstruct
    public void init() {
        seedDefaultReports();
    }

    private void seedDefaultReports() {
        if (trialReportRepository.count() == 0) {
            List<TrialReportEntity> reports = List.of(
                    new TrialReportEntity("1", "1", "COVID-19 Vaccine Trial", 50, LocalDate.now()),
                    new TrialReportEntity("2", "2", "Cancer Immunotherapy Study", 30, LocalDate.now())
            );
            trialReportRepository.saveAll(reports);
        }
    }
}

