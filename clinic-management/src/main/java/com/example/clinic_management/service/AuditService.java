package com.example.clinic_management.service;

import com.example.clinic_management.dto.AuditLogResponse;

import java.time.Instant;
import java.util.Map;
import java.util.List;

public interface AuditService {
    void log(String action, String entityType, Long entityId, Map<String, Object> details);
    List<AuditLogResponse> search(String entityType, Long entityId, String action, Instant from, Instant to);
}
