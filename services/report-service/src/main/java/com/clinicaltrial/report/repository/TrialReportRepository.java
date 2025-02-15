package com.clinicaltrial.report.repository;

import com.clinicaltrial.report.entity.TrialReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrialReportRepository extends JpaRepository<TrialReportEntity, String> {
    Optional<TrialReportEntity> findByTrialId(String trialReportId);
}
