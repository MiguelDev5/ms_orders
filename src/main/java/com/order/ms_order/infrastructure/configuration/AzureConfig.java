package com.order.ms_order.infrastructure.configuration;

import com.order.ms_order.application.AuditStoragePort;
import com.order.ms_order.infrastructure.messaging.AzureQueuePublisher;
import com.order.ms_order.infrastructure.storage.AzureBlobAuditAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureConfig {

    @Value("${azure.storage.connection-string}")
    private String connectionString;

    @Value("${azure.storage.queue-name}")
    private String queueName;

    @Bean
    public AzureQueuePublisher azureQueuePublisher() {
        return new AzureQueuePublisher(connectionString, queueName);
    }

    @Bean
    public AuditStoragePort auditStoragePort() {
        return new AzureBlobAuditAdapter(
                "UseDevelopmentStorage=true",
                "audit-container"
        );
    }
}
