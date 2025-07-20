package com.order.ms_order.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.ms_order.application.ports.in.PlaceOrderUseCase;
import com.order.ms_order.application.ports.out.SendOrderPort;
import com.order.ms_order.domain.model.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderUseCase implements PlaceOrderUseCase {
    private final SendOrderPort sendOrderPort;
    private final ObjectMapper objectMapper;

    public OrderUseCase(SendOrderPort sendOrderPort, ObjectMapper objectMapper){
        this.sendOrderPort = sendOrderPort;
        this.objectMapper = objectMapper;
    }

    @Override
    public void placeOrder(Order order) {
        try {
            String json = objectMapper.writeValueAsString(order);
            sendOrderPort.sendOrder(json);
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir a JSON", e);
        }
    }
}
