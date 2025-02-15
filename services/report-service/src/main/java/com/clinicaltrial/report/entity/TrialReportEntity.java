package com.clinicaltrial.report.entity;

import com.github.ku4marez.commonlibraries.entity.common.PersistentAuditedEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;

@Entity
@Table(name = "trial_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrialReportEntity extends PersistentAuditedEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(nullable = false)
    private String trialId;

    @Column(nullable = false)
    private String trialName;

    private int enrolledPatients;

    private LocalDate reportDate;
}
