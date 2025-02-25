package com.clinicaltrial.patient.service;

import com.clinicaltrial.patient.entity.PatientEntity;
import com.clinicaltrial.patient.repository.PatientRepository;
import com.github.ku4marez.commonlibraries.dto.event.KafkaEvent;
import com.github.ku4marez.commonlibraries.util.KafkaProducerUtil;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.github.ku4marez.commonlibraries.constant.KafkaConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {
    private final KafkaProducerUtil kafkaProducerUtil;
    private final PatientRepository patientRepository;

    @CircuitBreaker(name = "patientService", fallbackMethod = "fallbackGetPatients")
    public List<PatientEntity> getAllPatients() {
        log.info("Fetching all patients from the database.");
        return patientRepository.findAll();
    }

    @CircuitBreaker(name = "patientService", fallbackMethod = "fallbackAddPatient")
    public PatientEntity addPatient(PatientEntity patient) {
        patient = patientRepository.save(patient);
        log.info("New patient created: {}", patient.getId());

        // Send event to Kafka
        KafkaEvent<String> event = new KafkaEvent<>(PATIENT_CREATED_EVENT, patient.getId());
        kafkaProducerUtil.sendMessage(PATIENT_CREATED_TOPIC, patient.getId(), event.toJson());

        return patient;
    }

    @SuppressWarnings("unused")
    public List<PatientEntity> fallbackGetPatients(Throwable t) {
        log.warn("Fallback triggered for getAllPatients. Returning empty list. Reason: {}", t.getMessage());
        return Collections.emptyList();
    }

    @SuppressWarnings("unused")
    public PatientEntity fallbackAddPatient(PatientEntity patient, Throwable t) {
        log.warn("Fallback triggered for addPatient. Returning a placeholder patient. Reason: {}", t.getMessage());
        return new PatientEntity("N/A", "Unknown", "Unknown", "Unknown", "Unknown");
    }
}

