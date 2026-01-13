package com.example.clinic_management.service.impl;

import com.example.clinic_management.dto.AuditLogResponse;
import com.example.clinic_management.entity.AuditLog;
import com.example.clinic_management.repository.AuditLogRepository;
import com.example.clinic_management.service.AuditService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    public AuditServiceImpl(AuditLogRepository auditLogRepository, ObjectMapper objectMapper) {
        this.auditLogRepository = auditLogRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void log(String action, String entityType, Long entityId, Map<String, Object> details) {
        AuditLog a = new AuditLog();
        a.setAction(action);
        a.setEntityType(entityType);
        a.setEntityId(entityId);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            a.setActorEmail(auth.getName());
            String roles = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
            a.setActorRoles(roles);
        }

        if (details != null && !details.isEmpty()) {
            try {
                a.setDetails(objectMapper.writeValueAsString(details));
            } catch (Exception e) {
                a.setDetails("{\"error\":\"details_serialization_failed\"}");
            }
        }

        auditLogRepository.save(a);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogResponse> search(String entityType, Long entityId, String action, Instant from, Instant to) {

        boolean noFilters =
                entityType == null &&
                        entityId == null &&
                        action == null &&
                        from == null &&
                        to == null;

        List<AuditLog> logs;
        if (noFilters) {
            logs = auditLogRepository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt"));
        } else {
            logs = auditLogRepository.search(entityType, entityId, action, from, to);
        }

        return logs.stream().map(this::toResponse).toList();
    }


    private AuditLogResponse toResponse(AuditLog a) {
        AuditLogResponse r = new AuditLogResponse();
        r.setId(a.getId());
        r.setActorEmail(a.getActorEmail());
        r.setActorRoles(a.getActorRoles());
        r.setAction(a.getAction());
        r.setEntityType(a.getEntityType());
        r.setEntityId(a.getEntityId());
        r.setDetails(a.getDetails());
        r.setCreatedAt(a.getCreatedAt());
        return r;
    }
}