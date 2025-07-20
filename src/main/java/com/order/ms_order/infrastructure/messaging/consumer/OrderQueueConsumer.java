package com.order.ms_order.infrastructure.messaging.consumer;

import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueClientBuilder;
import com.azure.storage.queue.QueueServiceVersion;
import com.azure.storage.queue.models.QueueStorageException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.ms_order.application.AuditStoragePort;
import com.order.ms_order.domain.model.Order;
import com.order.ms_order.infrastructure.repository.CosmosOrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderQueueConsumer {

    private final QueueClient queueClient;
    private final CosmosOrderRepository repository;
    private final ObjectMapper objectMapper;
    private final AuditStoragePort auditStoragePort;

    public OrderQueueConsumer(@Value("${azure.storage.connection-string}") String connectionString,
                              @Value("${azure.storage.queue-name}") String queueName,
                              CosmosOrderRepository repository,
                              ObjectMapper objectMapper,
                              AuditStoragePort auditStoragePort) {
        this.queueClient = new QueueClientBuilder()
                .connectionString(connectionString)
                .queueName(queueName)
                .serviceVersion(QueueServiceVersion.V2022_11_02)
                .buildClient();

        this.repository = repository;
        this.objectMapper = objectMapper;
        this.auditStoragePort = auditStoragePort;
    }

    @Scheduled(fixedRate = 30000)
    public void processOrders() {
        if (queueExists()) {
            queueClient.receiveMessages(5).forEach(msg -> {
                String json = msg.getMessageText();
                System.out.println("Procesando mensaje de la cola: " + json);

                try {
                    Order order = objectMapper.readValue(json, Order.class);
                    if (order.getOrderId() == null || order.getOrderId().isEmpty()) {
                        order.setOrderId(UUID.randomUUID().toString());
                    }
                    repository.save(order);
                    System.out.println("Orden guardada en Cosmos DB: " + order);

                    String fileName = "order-" + order.getOrderId() + ".json";
                    String jsonAudit = objectMapper.writeValueAsString(order);
                    auditStoragePort.saveAuditJson(fileName, jsonAudit);
                } catch (JsonProcessingException e) {
                    System.out.println("Error al procesar el mensaje: " + e.getMessage());
                    throw new RuntimeException(e);
                }

                queueClient.deleteMessage(msg.getMessageId(), msg.getPopReceipt());
                System.out.println("Mensaje eliminado de la cola");
            });
        } else {
            System.out.println("La cola no existe.");
        }
    }

    public boolean queueExists() {
        try {
            queueClient.getProperties();
            return true;
        } catch (QueueStorageException e) {
            if (e.getStatusCode() == 404) {
                return false;
            }
            throw e;
        }
    }
}
