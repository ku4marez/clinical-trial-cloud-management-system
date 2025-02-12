package com.clinicaltrial.trial.configuration;

import com.clinicaltrial.trial.entity.Status;
import com.clinicaltrial.trial.entity.TrialEntity;
import com.clinicaltrial.trial.entity.PatientTrialEntity;
import com.clinicaltrial.trial.repository.TrialRepository;
import com.clinicaltrial.trial.repository.PatientTrialRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer {

    private final TrialRepository trialRepository;
    private final PatientTrialRepository patientTrialRepository;

    @PostConstruct
    public void init() {
        seedDefaultTrials();
    }

    private void seedDefaultTrials() {
        if (trialRepository.count() == 0) {
            List<TrialEntity> trials = List.of(
                    new TrialEntity("1", "COVID-19 Vaccine Trial", "Testing COVID-19 vaccine", Status.ONGOING, LocalDate.now(), LocalDate.now(), null),
                    new TrialEntity("2", "Cancer Immunotherapy Study", "Evaluating new cancer treatment", Status.ONGOING, LocalDate.now(), LocalDate.now(), null)

            );
            trialRepository.saveAll(trials);
        }

        if (patientTrialRepository.count() == 0) {
            List<PatientTrialEntity> patientTrials = List.of(
                    new PatientTrialEntity("1", trialRepository.findAll().get(0), "1", LocalDate.now()),
                    new PatientTrialEntity("2", trialRepository.findAll().get(1), "2", LocalDate.now())
            );
            patientTrialRepository.saveAll(patientTrials);
        }
    }
}
