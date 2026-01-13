package com.example.clinic_management.repository;

import com.example.clinic_management.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Sort;

import java.time.OffsetDateTime;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    @Query("""
        select a
        from AuditLog a
        where (:entityType is null or a.entityType = :entityType)
          and (:entityId is null or a.entityId = :entityId)
          and (:action is null or a.action = :action)
          and a.createdAt >= coalesce(:from, a.createdAt)
          and a.createdAt <= coalesce(:to, a.createdAt)
        order by a.createdAt desc
    """)
    List<AuditLog> search(
            @Param("entityType") String entityType,
            @Param("entityId") Long entityId,
            @Param("action") String action,
            @Param("from") java.time.Instant from,
            @Param("to") java.time.Instant to
    );

    List<AuditLog> findAll(Sort sort);
}
