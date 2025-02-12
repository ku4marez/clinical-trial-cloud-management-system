package com.clinicaltrial.trial.controller;

import com.clinicaltrial.trial.entity.TrialEntity;
import com.clinicaltrial.trial.service.TrialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trials")
@RequiredArgsConstructor
public class TrialController {

    private final TrialService trialService;

    @GetMapping
    public List<TrialEntity> getAllTrials() {
        return trialService.getAllTrials();
    }

    @PostMapping
    public ResponseEntity<TrialEntity> createTrial(@RequestBody TrialEntity trial) {
        return ResponseEntity.ok(trialService.createTrial(trial));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrialEntity> getTrialById(@PathVariable String id) {
        return trialService.getTrialById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrial(@PathVariable String id) {
        trialService.deleteTrial(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{trialId}/enroll/{patientId}")
    public ResponseEntity<String> enrollPatient(
            @PathVariable String trialId,
            @PathVariable String patientId) {
        trialService.enrollPatientInTrial(trialId, patientId);
        return ResponseEntity.ok("Patient " + patientId + " enrolled in trial " + trialId);
    }
}
