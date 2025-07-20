package com.order.ms_order.adapters;

import com.order.ms_order.application.ports.in.PlaceOrderUseCase;
import com.order.ms_order.domain.model.Order;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final PlaceOrderUseCase service;

    public OrderController(PlaceOrderUseCase service) {
        this.service = service;
    }

    @Operation(summary = "Crear nueva orden")
    @ApiResponse(responseCode = "202", description = "Orden aceptada y enviada a la cola")
    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody Order order) {
        service.placeOrder(order);
        return ResponseEntity.accepted().build();
    }
}
