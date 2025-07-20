package com.order.ms_order.application.ports.in;

import com.order.ms_order.domain.model.Order;

public interface PlaceOrderUseCase {
    void placeOrder(Order order);
}
