package com.order.ms_order.application.ports.out;

public interface SendOrderPort {
    void sendOrder(String orderJson);
}
