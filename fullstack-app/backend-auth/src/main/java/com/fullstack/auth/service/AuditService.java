package com.fullstack.auth.service;

import com.fullstack.auth.entity.sqlserver.AuditLog;
import com.fullstack.auth.repository.sqlserver.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de auditoría.
 * Registra acciones en SQL Server para trazabilidad.
 */
@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    @Transactional("sqlserverTransactionManager")
    public void log(String action, String entity, Long entityId, String username, String details, String ipAddress) {
        AuditLog log = AuditLog.builder()
                .action(action)
                .entity(entity)
                .entityId(entityId)
                .username(username)
                .details(details)
                .ipAddress(ipAddress)
                .build();
        auditLogRepository.save(log);
    }

    public List<AuditLog> getByUsername(String username) {
        return auditLogRepository.findByUsername(username);
    }

    public List<AuditLog> getByDateRange(LocalDateTime start, LocalDateTime end) {
        return auditLogRepository.findByCreatedAtBetween(start, end);
    }

    public List<AuditLog> getByEntity(String entity, Long entityId) {
        return auditLogRepository.findByEntityAndEntityId(entity, entityId);
    }
}
