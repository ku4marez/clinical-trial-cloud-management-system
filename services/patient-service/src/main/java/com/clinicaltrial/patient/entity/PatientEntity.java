package com.clinicaltrial.patient.entity;

import com.github.ku4marez.commonlibraries.entity.common.PersistentAuditedEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "patients")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientEntity extends PersistentAuditedEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private String id;

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
