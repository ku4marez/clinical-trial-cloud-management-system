package com.clinicaltrial.report.event;

import com.clinicaltrial.report.client.TrialServiceClient;
import com.clinicaltrial.report.service.ReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ku4marez.commonlibraries.constant.KafkaConstants;
import com.github.ku4marez.commonlibraries.dto.event.KafkaEvent;
import com.github.ku4marez.commonlibraries.dto.trial.TrialDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrialEventListener {

    private final ReportService reportService;
    private final TrialServiceClient client;

    @Autowired
    public TrialEventListener(ReportService reportService, TrialServiceClient client) {
        this.reportService = reportService;
        this.client = client;
    }

    @KafkaListener(topics = KafkaConstants.TRIAL_CREATED_EVENT, groupId = "report-service-group")
    public void handleTrialCreated(String message) {
        log.info("Received trial.created event: {}", message);
        try {
            KafkaEvent<String> event = new ObjectMapper().readValue(message, KafkaEvent.class);
            String trialId = event.getPayload();

            TrialDTO trialDTO = client.getTrialById(trialId);
            reportService.createTrialReport(
                    trialDTO
            );
        } catch (Exception e) {
            log.error("Error processing trial.created event: {}", e.getMessage(), e);
        }
    }
}
