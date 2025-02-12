package com.clinicaltrial.trial.repository;

import com.clinicaltrial.trial.entity.Status;
import com.clinicaltrial.trial.entity.TrialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrialRepository extends JpaRepository<TrialEntity, String> {
    List<TrialEntity> findByStatus(Status status);

}
