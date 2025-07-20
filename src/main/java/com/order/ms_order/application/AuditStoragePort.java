package com.order.ms_order.application;

public interface AuditStoragePort {
    void saveAuditJson(String fileName, String jsonContent);
}
