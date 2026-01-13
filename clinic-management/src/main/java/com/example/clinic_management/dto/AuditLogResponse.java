package com.example.clinic_management.dto;

import java.time.Instant;

public class AuditLogResponse {
    private Long id;
    private String actorEmail;
    private String actorRoles;
    private String action;
    private String entityType;
    private Long entityId;
    private String details;
    private Instant createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getActorEmail() { return actorEmail; }
    public void setActorEmail(String actorEmail) { this.actorEmail = actorEmail; }

    public String getActorRoles() { return actorRoles; }
    public void setActorRoles(String actorRoles) { this.actorRoles = actorRoles; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }

    public Long getEntityId() { return entityId; }
    public void setEntityId(Long entityId) { this.entityId = entityId; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
