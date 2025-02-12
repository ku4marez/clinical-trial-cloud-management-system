package com.clinicaltrial.trial.entity;

import com.github.ku4marez.commonlibraries.entity.common.PersistentAuditedEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "trials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrialEntity extends PersistentAuditedEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @OneToMany(mappedBy = "trial", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatientTrialEntity> enrolledPatients;
}
