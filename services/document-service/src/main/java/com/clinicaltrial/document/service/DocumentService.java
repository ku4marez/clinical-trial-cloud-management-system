package com.clinicaltrial.document.service;

import com.clinicaltrial.document.entity.DocumentMetadata;
import com.clinicaltrial.document.exception.DocumentNotFoundException;
import com.clinicaltrial.document.exception.FileUploadException;
import com.clinicaltrial.document.repository.DocumentRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

import software.amazon.awssdk.services.s3.S3Client;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {

    private final S3Client s3Client;
    private final DocumentRepository documentRepository;
    private final String bucketName;

    @CircuitBreaker(name = "documentService", fallbackMethod = "fallbackGetDocument")
    public DocumentMetadata getDocument(String documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException(documentId));
    }

    @CircuitBreaker(name = "documentService", fallbackMethod = "fallbackUploadDocument")
    public DocumentMetadata uploadDocument(MultipartFile file) {
        String fileKey = UUID.randomUUID().toString();

        try {
            // Upload file to S3
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .contentType(file.getContentType())
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            // Save metadata in DynamoDB
            DocumentMetadata metadata = new DocumentMetadata(fileKey, file.getOriginalFilename(), fileKey,
                    file.getContentType(), file.getSize(), "UPLOADED", Instant.now());

            documentRepository.save(metadata);
            return metadata;

        } catch (IOException e) {
            throw new FileUploadException("Failed to upload file to S3", e);
        }
    }

    @CircuitBreaker(name = "documentService", fallbackMethod = "fallbackDeleteDocument")
    public void deleteDocument(String documentId) {
        DocumentMetadata metadata = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException(documentId));

        // Delete from S3
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(metadata.getS3Key())
                .build());

        // Delete from DynamoDB
        documentRepository.delete(documentId);
    }

    // Fallback methods
    @SuppressWarnings("unused")
    public DocumentMetadata fallbackGetDocument(String documentId, Throwable t) {
        log.warn("Fallback triggered for getDocument. Returning fallback document. Reason: {}", t.getMessage());
        return new DocumentMetadata(documentId, "N/A", "N/A", "N/A", 0, "N/A", Instant.now());
    }

    @SuppressWarnings("unused")
    public DocumentMetadata fallbackUploadDocument(MultipartFile file, Throwable t) {
        log.warn("Fallback triggered for uploadDocument. Returning fallback upload document. Reason: {}", t.getMessage());
        return new DocumentMetadata("N/A", file.getOriginalFilename(), "N/A", file.getContentType(), file.getSize(), "N/A", Instant.now());
    }

    @SuppressWarnings("unused")
    public void fallbackDeleteDocument(String documentId, Throwable t) {
        log.warn("Fallback executed: Failed to delete document with ID: {}", documentId);
    }
}
