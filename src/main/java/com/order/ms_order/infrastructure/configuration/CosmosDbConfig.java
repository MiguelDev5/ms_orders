package com.order.ms_order.infrastructure.configuration;

import com.azure.cosmos.*;
import com.azure.cosmos.models.CosmosContainerProperties;
import com.azure.cosmos.models.CosmosContainerResponse;
import com.azure.cosmos.models.ThroughputProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CosmosDbConfig {

    @Value("${azure.cosmos.uri}")
    private String uri;

    @Value("${azure.cosmos.key}")
    private String key;

    @Value("${azure.cosmos.database}")
    private String databaseName;

    @Bean
    public CosmosClient cosmosClient() {
        return new CosmosClientBuilder()
                .endpoint(uri)
                .key(key)
                .consistencyLevel(ConsistencyLevel.EVENTUAL)
                .gatewayMode()
                .buildClient();
    }

    @Bean
    public CosmosContainer cosmosContainer(CosmosClient cosmosClient) {
        CosmosDatabase database = cosmosClient.getDatabase("orders-db");
        database.createContainerIfNotExists(new CosmosContainerProperties("orders", "/orderId"), ThroughputProperties.createManualThroughput(400));
        return database.getContainer("orders");
    }
}

