package com.order.ms_order.infrastructure.storage;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.order.ms_order.application.AuditStoragePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class AzureBlobAuditAdapter implements AuditStoragePort {

    private final BlobContainerClient containerClient;

    public AzureBlobAuditAdapter(BlobContainerClient containerClient) {
        this.containerClient = containerClient;
    }

    public AzureBlobAuditAdapter(@Value("${azure.storage.connection-string}") String connectionString,
                                 @Value("${azure.blob.container-name}") String containerName) {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

        this.containerClient = blobServiceClient.getBlobContainerClient(containerName);
        if (!containerClient.exists()) {
            containerClient.create();
        }
    }

    @Override
    public void saveAuditJson(String fileName, String jsonContent) {
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        ByteArrayInputStream dataStream = new ByteArrayInputStream(jsonContent.getBytes(StandardCharsets.UTF_8));
        blobClient.upload(dataStream, jsonContent.length(), true);
    }
}
