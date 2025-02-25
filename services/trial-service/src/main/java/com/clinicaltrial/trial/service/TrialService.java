package com.clinicaltrial.trial.service;

import com.clinicaltrial.trial.entity.PatientTrialEntity;
import com.clinicaltrial.trial.entity.TrialEntity;
import com.clinicaltrial.trial.repository.PatientTrialRepository;
import com.clinicaltrial.trial.repository.TrialRepository;
import com.github.ku4marez.commonlibraries.dto.event.KafkaEvent;
import com.github.ku4marez.commonlibraries.util.KafkaProducerUtil;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.github.ku4marez.commonlibraries.constant.KafkaConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrialService {

    private final KafkaProducerUtil kafkaProducerUtil;
    private final TrialRepository trialRepository;
    private final PatientTrialRepository patientTrialRepository;
    private final RestTemplate restTemplate;

    @CircuitBreaker(name = "trialService", fallbackMethod = "fallbackGetAllTrials")
    public List<TrialEntity> getAllTrials() {
        return trialRepository.findAll();
    }

    @CircuitBreaker(name = "trialService", fallbackMethod = "fallbackCreateTrial")
    public TrialEntity createTrial(TrialEntity trial) {
        trial.setStartDate(LocalDate.now());
        trial = trialRepository.save(trial);
        log.info("New trial created: {}", trial.getId());
        KafkaEvent<String> event = new KafkaEvent<>(TRIAL_CREATED_EVENT, trial.getId());
        kafkaProducerUtil.sendMessage(TRIAL_CREATED_TOPIC, trial.getId(), event.toJson());
        return trial;
    }

    @CircuitBreaker(name = "trialService", fallbackMethod = "fallbackGetTrialById")
    public Optional<TrialEntity> getTrialById(String trialId) {
        return trialRepository.findById(trialId);
    }

    @CircuitBreaker(name = "trialService", fallbackMethod = "fallbackDeleteTrial")
    public void deleteTrial(String trialId) {
        trialRepository.deleteById(trialId);
    }

    @CircuitBreaker(name = "trialService", fallbackMethod = "fallbackEnrollPatientInTrial")
    public void enrollPatientInTrial(String trialId, String patientId) {
        TrialEntity trial = trialRepository.findById(trialId)
                .orElseThrow(() -> new IllegalArgumentException("Trial not found"));

        String patientCheckUrl = "http://patient-service/api/patients/" + patientId;
        ResponseEntity<Void> response = restTemplate.getForEntity(patientCheckUrl, Void.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IllegalArgumentException("Patient does not exist in patient-service");
        }

        // Enroll patient in trial
        PatientTrialEntity enrollment = new PatientTrialEntity();
        enrollment.setTrial(trial);
        enrollment.setPatientId(patientId);
        enrollment.setEnrollmentDate(LocalDate.now());

        patientTrialRepository.save(enrollment);
        log.info("Patient {} enrolled in trial {}", patientId, trialId);
    }

    // Fallback methods
    @SuppressWarnings("unused")
    public List<TrialEntity> fallbackGetAllTrials(Throwable t) {
        log.warn("Fallback: Unable to fetch trials. Returning empty list.");
        return Collections.emptyList();
    }

    @SuppressWarnings("unused")
    public TrialEntity fallbackCreateTrial(TrialEntity trial, Throwable t) {
        log.warn("Fallback: Unable to create trial. Returning trial with ID 'N/A'.");
        return new TrialEntity("N/A", "N/A", "N/A", null, LocalDate.now(), LocalDate.now(), Collections.emptyList());
    }

    @SuppressWarnings("unused")
    public Optional<TrialEntity> fallbackGetTrialById(String trialId, Throwable t) {
        log.warn("Fallback: Unable to fetch trial by ID {}. Returning empty optional.", trialId);
        return Optional.empty();
    }

    @SuppressWarnings("unused")
    public void fallbackDeleteTrial(String trialId, Throwable t) {
        log.warn("Fallback: Unable to delete trial with ID {}", trialId);
    }

    @SuppressWarnings("unused")
    public void fallbackEnrollPatientInTrial(String trialId, String patientId, Throwable t) {
        log.warn("Fallback: Unable to enroll patient {} in trial {}", patientId, trialId);
    }
}

