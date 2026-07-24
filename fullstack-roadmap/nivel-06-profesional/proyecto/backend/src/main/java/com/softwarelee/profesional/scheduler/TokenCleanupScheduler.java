package com.softwarelee.profesional.scheduler;

import com.softwarelee.profesional.mapper.PasswordResetMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Tarea programada: limpia tokens expirados cada hora.
 * Evita que la tabla crezca indefinidamente.
 */
@Component
public class TokenCleanupScheduler {

    private final PasswordResetMapper resetMapper;

    public TokenCleanupScheduler(PasswordResetMapper resetMapper) {
        this.resetMapper = resetMapper;
    }

    @Scheduled(fixedRate = 3600000) // Cada hora
    public void limpiarTokensExpirados() {
        resetMapper.eliminarExpirados();
        System.out.println("[Scheduler] Tokens expirados eliminados");
    }
}
