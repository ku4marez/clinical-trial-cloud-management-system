package com.clinicaltrial.document.exception;

public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(String documentId) {
        super("Document with ID " + documentId + " not found.");
    }
}
