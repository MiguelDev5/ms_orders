package com.order.ms_order.infrastructure.messaging;

import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueClientBuilder;
import com.azure.storage.queue.QueueServiceVersion;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.ms_order.application.ports.out.SendOrderPort;

public class AzureQueuePublisher implements SendOrderPort {

    private final QueueClient queueClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public AzureQueuePublisher(String connectionString, String queueName) {
        this.queueClient = new QueueClientBuilder()
                .connectionString(connectionString)
                .queueName(queueName)
                .serviceVersion(QueueServiceVersion.V2022_11_02)
                .buildClient();
        queueClient.createIfNotExists();
    }


    @Override
    public void sendOrder(String orderJson) {
        try {
            queueClient.sendMessage(orderJson);
        } catch (Exception e) {
            throw new RuntimeException("Error sending message to Azure Queue", e);
        }
    }
}
