package com.fullstack.mail.controller;

import com.fullstack.mail.service.EmailService;
import com.fullstack.mail.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller del servicio de correos.
 * Endpoints para envío de correos y recuperación de contraseña.
 */
@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {

    private final EmailService emailService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendEmail(@RequestBody Map<String, String> request) {
        String to = request.get("to");
        String subject = request.get("subject");
        String body = request.get("body");

        emailService.sendEmail(to, subject, body);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Correo enviado exitosamente"
        ));
    }

    @PostMapping("/password-reset/request")
    public ResponseEntity<Map<String, Object>> requestPasswordReset(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        passwordResetService.requestPasswordReset(email);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Se ha enviado un correo de recuperación"
        ));
    }

    @PostMapping("/password-reset/validate")
    public ResponseEntity<Map<String, Object>> validateResetToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String email = passwordResetService.validateToken(token);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "email", email,
                "message", "Token válido"
        ));
    }

    @PostMapping("/password-reset/confirm")
    public ResponseEntity<Map<String, Object>> confirmPasswordReset(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        passwordResetService.markTokenAsUsed(token);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Contraseña restablecida exitosamente"
        ));
    }

    @PostMapping("/welcome")
    public ResponseEntity<Map<String, Object>> sendWelcomeEmail(@RequestBody Map<String, String> request) {
        String to = request.get("email");
        String username = request.get("username");

        emailService.sendWelcomeEmail(to, username);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Correo de bienvenida enviado"
        ));
    }
}
