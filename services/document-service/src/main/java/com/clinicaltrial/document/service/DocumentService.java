package com.clinicaltrial.document.service;

import com.clinicaltrial.document.entity.DocumentMetadata;
import com.clinicaltrial.document.exception.DocumentNotFoundException;
import com.clinicaltrial.document.exception.FileUploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.services.s3.S3Client;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final S3Client s3Client;
    private final DynamoDbTable<DocumentMetadata> documentTable;
    private final String bucketName;

    public DocumentMetadata getDocument(String documentId) {
        // Fetch document metadata from DynamoDB
        DocumentMetadata metadata = documentTable.getItem(r -> r.key(k -> k.partitionValue(documentId)));

        if (metadata == null) {
            throw new DocumentNotFoundException(documentId);
        }

        return metadata;
    }

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
            DocumentMetadata metadata = new DocumentMetadata();
            metadata.setDocumentId(fileKey);
            metadata.setFilename(file.getOriginalFilename());
            metadata.setS3Key(fileKey);
            metadata.setContentType(file.getContentType());
            metadata.setSize(file.getSize());
            metadata.setUploadDate(Instant.now());

            documentTable.putItem(metadata);

            return metadata;
        } catch (IOException e) {
            throw new FileUploadException("Failed to upload file to S3", e);
        }
    }

    public void deleteDocument(String documentId) {
        // Fetch metadata
        DocumentMetadata metadata = documentTable.getItem(r -> r.key(k -> k.partitionValue(documentId)));

        if (metadata == null) {
            throw new DocumentNotFoundException(documentId);
        }

        // Delete from S3
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(metadata.getS3Key())
                .build());

        // Delete from DynamoDB
        documentTable.deleteItem(r -> r.key(k -> k.partitionValue(documentId)));
    }
}


