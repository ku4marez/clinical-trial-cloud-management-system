package com.clinicaltrial.document.entity;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class DocumentMetadata {

    private String documentId;
    private String filename;
    private String s3Key;
    private String contentType;
    private long size;
    private String ownerId;
    private Instant uploadDate;

    @DynamoDbPartitionKey
    public String getDocumentId() {
        return documentId;
    }

    @DynamoDbSortKey
    public String getFilename() {
        return filename;
    }
}
