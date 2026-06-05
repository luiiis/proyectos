package com.fullstack.mail.service;

import com.fullstack.mail.entity.PasswordResetToken;
import com.fullstack.mail.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Servicio de recuperación de contraseña.
 * Genera tokens únicos y envía correos de recuperación.
 */
@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${app.password-reset-expiration}")
    private long expirationMs;

    public void requestPasswordReset(String email) {
        // Invalidar tokens anteriores
        tokenRepository.findByEmailAndUsedFalse(email)
                .ifPresent(token -> {
                    token.setUsed(true);
                    tokenRepository.save(token);
                });

        // Generar nuevo token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .email(email)
                .expiresAt(LocalDateTime.now().plusSeconds(expirationMs / 1000))
                .build();

        tokenRepository.save(resetToken);

        // Enviar correo
        String resetLink = frontendUrl + "/reset-password?token=" + token;
        emailService.sendPasswordResetEmail(email, resetLink);
    }

    public String validateToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (resetToken.isExpired()) {
            throw new RuntimeException("Token expirado");
        }

        if (resetToken.getUsed()) {
            throw new RuntimeException("Token ya fue utilizado");
        }

        return resetToken.getEmail();
    }

    public void markTokenAsUsed(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
    }
}
