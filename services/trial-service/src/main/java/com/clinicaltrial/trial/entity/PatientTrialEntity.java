package com.clinicaltrial.trial.entity;

import com.github.ku4marez.commonlibraries.entity.common.PersistentAuditedEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;

@Entity
@Table(name = "patient_trials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientTrialEntity extends PersistentAuditedEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "trial_id", nullable = false)
    private TrialEntity trial;

    @Column(nullable = false)
    private String patientId;

    @Column(nullable = false)
    private LocalDate enrollmentDate;
}
