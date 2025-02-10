package com.clinicaltrial.patient.controller;

import com.clinicaltrial.patient.entity.PatientEntity;
import com.clinicaltrial.patient.service.PatientService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@AllArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public List<PatientEntity> getPatients() {
        return patientService.getAllPatients();
    }

    @PostMapping
    public PatientEntity addPatient(@RequestBody PatientEntity patient) {
        return patientService.addPatient(patient);
    }
}