package com.clinicaltrial.document.repository;

import com.clinicaltrial.document.entity.DocumentMetadata;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class DocumentRepository {

    private final DynamoDbTable<DocumentMetadata> documentTable;

    public DocumentRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.documentTable = dynamoDbEnhancedClient.table("documents", TableSchema.fromBean(DocumentMetadata.class));
    }

    public void save(DocumentMetadata document) {
        documentTable.putItem(document);
    }

    public Optional<DocumentMetadata> findById(String documentId) {
        return Optional.ofNullable(documentTable.getItem(r -> r.key(k -> k.partitionValue(documentId))));
    }

    public void delete(String documentId) {
        documentTable.deleteItem(r -> r.key(k -> k.partitionValue(documentId)));
    }
}
