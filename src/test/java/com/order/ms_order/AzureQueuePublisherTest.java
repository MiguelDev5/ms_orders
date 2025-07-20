package com.order.ms_order;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.order.ms_order.infrastructure.storage.AzureBlobAuditAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AzureBlobAuditAdapterTest {

    private BlobContainerClient containerClient;
    private BlobClient blobClient;
    private AzureBlobAuditAdapter adapter;

    @BeforeEach
    void setUp() {
        containerClient = mock(BlobContainerClient.class);
        blobClient = mock(BlobClient.class);

        when(containerClient.getBlobClient(anyString())).thenReturn(blobClient);
        adapter = new AzureBlobAuditAdapter(containerClient); // usa el constructor de pruebas
    }

    @Test
    void saveAuditJson_uploadsToBlobStorage() throws IOException {
        adapter.saveAuditJson("test.json", "{\"ok\": true}");

        verify(containerClient).getBlobClient("test.json");
        verify(blobClient).upload(any(InputStream.class), anyLong(), eq(true));
    }
}
