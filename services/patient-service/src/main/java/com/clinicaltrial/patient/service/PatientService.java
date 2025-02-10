package com.clinicaltrial.patient.service;

import com.clinicaltrial.patient.entity.PatientEntity;
import com.clinicaltrial.patient.repository.PatientRepository;
import com.github.ku4marez.commonlibraries.dto.event.KafkaEvent;
import com.github.ku4marez.commonlibraries.util.KafkaProducerUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.github.ku4marez.commonlibraries.constant.KafkaConstants.*;

@Service
@AllArgsConstructor
@Slf4j
public class PatientService {
    private final KafkaProducerUtil kafkaProducerUtil;
    private final PatientRepository patientRepository;

    public List<PatientEntity> getAllPatients() {
        log.info("Getting all patients");
        return patientRepository.findAll();
    }

    public PatientEntity addPatient(PatientEntity patient) {
        patient = patientRepository.save(patient);
        log.info("New patient created: {}", patient.getId());
        KafkaEvent<String> event = new KafkaEvent<>(PATIENT_CREATED_EVENT, patient.getId());
        kafkaProducerUtil.sendMessage(PATIENT_CREATED_TOPIC, patient.getId(), event.toJson());
        return patient;
    }
}
