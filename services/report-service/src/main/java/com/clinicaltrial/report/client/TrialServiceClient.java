package com.clinicaltrial.report.client;

import com.github.ku4marez.commonlibraries.dto.trial.TrialDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "trial-service", url = "http://trial-service")
public interface TrialServiceClient {

    @GetMapping("/api/trials/{trialId}")
    TrialDTO getTrialById(@PathVariable String trialId);
}
