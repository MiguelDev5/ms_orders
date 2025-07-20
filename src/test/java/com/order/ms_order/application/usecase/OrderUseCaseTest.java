package com.order.ms_order.application.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.ms_order.application.ports.out.SendOrderPort;
import com.order.ms_order.application.service.OrderUseCase;
import com.order.ms_order.domain.model.Order;
import com.order.ms_order.domain.model.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class OrderUseCaseTest {

    private SendOrderPort sendOrderPort;
    private ObjectMapper objectMapper;
    private OrderUseCase orderUseCase;

    @BeforeEach
    void setUp() {
        sendOrderPort = mock(SendOrderPort.class);
        objectMapper = new ObjectMapper();
        orderUseCase = new OrderUseCase(sendOrderPort, objectMapper);
    }

    @Test
    void testPlaceOrder_sendsOrderToQueue() throws Exception {
        // Arrange
        Order order = new Order("1", "customer-123",
                List.of(new OrderItem("prod-1", 2)), 50.0);
        String expectedJson = objectMapper.writeValueAsString(order);

        // Act
        orderUseCase.placeOrder(order);

        // Assert
        verify(sendOrderPort, times(1)).sendOrder(expectedJson);
    }

    @Test
    void placeOrder_sendsSerializedOrderToQueue() throws JsonProcessingException {
        // Arrange
        Order order = new Order("1", "C001", List.of(), 100.0);

        // Act
        orderUseCase.placeOrder(order);

        // Assert
        ArgumentCaptor<String> jsonCaptor = ArgumentCaptor.forClass(String.class);
        verify(sendOrderPort).sendOrder(jsonCaptor.capture());

        String jsonSent = jsonCaptor.getValue();
        Order deserialized = objectMapper.readValue(jsonSent, Order.class);

        assertEquals(order.getOrderId(), deserialized.getOrderId());
        assertEquals(order.getCustomerId(), deserialized.getCustomerId());
        assertEquals(order.getTotalAmount(), deserialized.getTotalAmount());
    }

    @Test
    void placeOrder_throwsRuntimeException_onSerializationError() {
        // Arrange
        Order order = mock(Order.class);
        when(order.getOrderId()).thenThrow(new RuntimeException("Fallo interno"));

        // Falla
        OrderUseCase faultyUseCase = new OrderUseCase(sendOrderPort, objectMapper);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> faultyUseCase.placeOrder(order));
    }
}

