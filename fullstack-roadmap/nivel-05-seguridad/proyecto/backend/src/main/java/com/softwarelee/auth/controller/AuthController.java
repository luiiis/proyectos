package com.softwarelee.auth.controller;

import com.softwarelee.auth.dto.LoginRequest;
import com.softwarelee.auth.dto.LoginResponse;
import com.softwarelee.auth.dto.RefreshRequest;
import com.softwarelee.auth.model.Usuario;
import com.softwarelee.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * POST /api/auth/login
     * Público: recibe username + password, devuelve tokens.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/auth/refresh
     * Público: recibe refreshToken, devuelve nuevo accessToken.
     */
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        LoginResponse response = authService.refresh(request);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/auth/me
     * Protegido: devuelve info del usuario autenticado.
     * Requiere header: Authorization: Bearer <token>
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(Authentication authentication) {
        String username = authentication.getName();
        Usuario usuario = authService.getUsuarioActual(username);
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", usuario
        ));
    }

    /**
     * GET /api/auth/admin
     * Solo accesible para usuarios con rol ADMIN.
     */
    @GetMapping("/admin")
    public ResponseEntity<Map<String, Object>> adminOnly(Authentication authentication) {
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Bienvenido al panel de administración",
            "usuario", authentication.getName()
        ));
    }
}
