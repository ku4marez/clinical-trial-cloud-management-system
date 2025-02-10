package com.clinicaltrial.patient.configuration;

import com.clinicaltrial.patient.entity.PatientEntity;
import com.clinicaltrial.patient.repository.PatientRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer {

    private final PatientRepository patientRepository;

    @PostConstruct
    public void init() {
        seedDefaultPatients();
    }

    private void seedDefaultPatients() {
        if (patientRepository.count() == 0) {
            List<PatientEntity> patients = List.of(
                    new PatientEntity("1", "John", "Doe", "john.doe@clinic.com", "123-456-7890"),
                    new PatientEntity("2", "Jane", "Smith", "jane.smith@clinic.com", "987-654-3210")
            );
            patientRepository.saveAll(patients);
        }
    }
}
