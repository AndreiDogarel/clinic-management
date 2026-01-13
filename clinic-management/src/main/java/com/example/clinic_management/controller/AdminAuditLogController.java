package com.example.clinic_management.controller;

import com.example.clinic_management.dto.AuditLogResponse;
import com.example.clinic_management.service.AuditService;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/admin/audit-logs")
public class AdminAuditLogController {

    private final AuditService auditService;

    public AdminAuditLogController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    public List<AuditLogResponse> search(
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) Long entityId,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) Instant from,
            @RequestParam(required = false) Instant to
    ) {
        return auditService.search(entityType, entityId, action, from, to);
    }
}
