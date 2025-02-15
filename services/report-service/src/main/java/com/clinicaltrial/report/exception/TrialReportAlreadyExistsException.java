package com.clinicaltrial.report.exception;

import com.github.ku4marez.commonlibraries.exception.EntityExistsException;

public class TrialReportAlreadyExistsException extends EntityExistsException {

    public TrialReportAlreadyExistsException(String identifier) {
        super("Trial Report", identifier);
    }
}
