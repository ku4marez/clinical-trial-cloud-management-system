package com.clinicaltrial.trial.repository;

import com.clinicaltrial.trial.entity.PatientTrialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientTrialRepository extends JpaRepository<PatientTrialEntity, String> {

}
