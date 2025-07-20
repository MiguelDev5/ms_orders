package com.order.ms_order;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.models.QueueMessageItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.ms_order.application.AuditStoragePort;
import com.order.ms_order.domain.model.Order;
import com.order.ms_order.infrastructure.messaging.consumer.OrderQueueConsumer;
import com.order.ms_order.infrastructure.repository.CosmosOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class OrderQueueConsumerTest {

    private QueueClient queueClient;
    private CosmosOrderRepository repository;
    private ObjectMapper mapper;
    private OrderQueueConsumer consumer;
    private AuditStoragePort auditStoragePort;

    @BeforeEach
    void setup() {
        queueClient = mock(QueueClient.class);
        repository = mock(CosmosOrderRepository.class);
        auditStoragePort = mock(AuditStoragePort.class);
        mapper = new ObjectMapper();
        consumer = new OrderQueueConsumer("UseDevelopmentStorage=true",
                "order-queue", repository, mapper, auditStoragePort);
    }

    @Test
    void processOrders_savesOrderToRepository() throws Exception {
        Order order = new Order("1", "C001", List.of(), 100.0);
        String orderJson = mapper.writeValueAsString(mapper.writeValueAsString(order)); // doble serialización
        QueueMessageItem msg = mock(QueueMessageItem.class);

        PagedIterable<QueueMessageItem> mockedIterable = mock(PagedIterable.class);
        when(queueClient.receiveMessages(anyInt())).thenReturn(mockedIterable);

        consumer.processOrders();

        verify(repository).save(any(Order.class));
        verify(queueClient).deleteMessage(anyString(), anyString());
    }
}