package com.order.ms_order;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.order.ms_order.application.AuditStoragePort;
import com.order.ms_order.infrastructure.storage.AzureBlobAuditAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuditBlobStorageAdapterTest {

    private BlobContainerClient containerClient;
    private BlobClient blobClient;
    private AuditStoragePort adapter;

    @BeforeEach
    void setUp() {
        containerClient = mock(BlobContainerClient.class);
        blobClient = mock(BlobClient.class);
        when(containerClient.getBlobClient(anyString())).thenReturn(blobClient);

        adapter = new AzureBlobAuditAdapter(containerClient);
    }

    @Test
    void saveAuditJson_uploadsToBlobStorage() throws IOException {
        adapter.saveAuditJson("audit.json", "{\"test\":1}");
        verify(blobClient).upload(any(InputStream.class), anyLong(), eq(true));
    }
}

