package com.order.ms_order.infrastructure.repository;

import com.azure.cosmos.CosmosContainer;
import com.order.ms_order.domain.model.Order;
import org.springframework.stereotype.Repository;

@Repository
public class CosmosOrderRepository {
    private final CosmosContainer container;

    public CosmosOrderRepository(CosmosContainer container) {
        this.container = container;
    }

    public void save(Order order) {
        container.createItem(order);
    }
}
