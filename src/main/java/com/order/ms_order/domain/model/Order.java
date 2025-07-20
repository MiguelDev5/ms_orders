package com.order.ms_order.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Order {
    @JsonProperty("id")
    private String orderId;
    private String customerId;
    private List<OrderItem> items;
    private double totalAmount;

    public Order() {
    }

    public Order(String orderId, String customerId, List<OrderItem> items, double totalAmount) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = items;
        this.totalAmount = totalAmount;
    }
}
