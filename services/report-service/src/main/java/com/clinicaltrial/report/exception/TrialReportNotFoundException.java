package com.clinicaltrial.report.exception;

import com.github.ku4marez.commonlibraries.exception.EntityExistsException;

public class TrialReportNotFoundException extends EntityExistsException {

    public TrialReportNotFoundException(String identifier) {
        super("Trial Report", identifier);
    }
}
