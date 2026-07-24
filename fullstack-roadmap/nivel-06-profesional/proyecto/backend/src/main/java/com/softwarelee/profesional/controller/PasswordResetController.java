package com.softwarelee.profesional.controller;

import com.softwarelee.profesional.service.PasswordResetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {

    private final PasswordResetService resetService;

    public PasswordResetController(PasswordResetService resetService) {
        this.resetService = resetService;
    }

    /**
     * POST /api/auth/forgot-password
     * Público: solicita recuperación de contraseña.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, Object>> solicitarRecuperacion(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        resetService.solicitarRecuperacion(email);
        // Siempre responder OK (no revelar si el email existe)
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Si el correo existe, recibirás instrucciones para recuperar tu contraseña"
        ));
    }

    /**
     * POST /api/auth/reset-password
     * Público: restablece la contraseña con el token.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, Object>> restablecerPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String nuevoPassword = body.get("nuevoPassword");
        resetService.restablecerPassword(token, nuevoPassword);
        return ResponseEntity.ok(Map.of("success", true, "message", "Contraseña restablecida exitosamente"));
    }
}
