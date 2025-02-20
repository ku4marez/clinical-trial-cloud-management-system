package com.clinicaltrial.document.controller;

import com.clinicaltrial.document.entity.DocumentMetadata;
import com.clinicaltrial.document.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping("/documents/{id}")
    public ResponseEntity<DocumentMetadata> getDocument(@PathVariable String id) {
        DocumentMetadata metadata = documentService.getDocument(id);
        return ResponseEntity.ok(metadata);
    }

    @DeleteMapping("/documents/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable String id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/documents")
    public ResponseEntity<DocumentMetadata> uploadDocument(@RequestParam("file") MultipartFile file) {
        DocumentMetadata metadata = documentService.uploadDocument(file);
        return ResponseEntity.ok(metadata);
    }
}