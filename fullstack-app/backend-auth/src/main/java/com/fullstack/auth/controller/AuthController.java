package com.fullstack.auth.controller;

import com.fullstack.auth.dto.*;
import com.fullstack.auth.service.AuthService;
import com.fullstack.auth.service.AuditService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller de autenticación.
 * Endpoints públicos para registro, login y refresh token.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuditService auditService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest) {

        AuthResponse response = authService.register(request);
        auditService.log("REGISTER", "User", null, request.getUsername(),
                "Nuevo usuario registrado", httpRequest.getRemoteAddr());

        return ResponseEntity.ok(ApiResponse.ok("Usuario registrado exitosamente", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody AuthRequest request,
            HttpServletRequest httpRequest) {

        AuthResponse response = authService.login(request);
        auditService.log("LOGIN", "User", null, request.getUsername(),
                "Inicio de sesión", httpRequest.getRemoteAddr());

        return ResponseEntity.ok(ApiResponse.ok("Login exitoso", response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @RequestBody Map<String, String> request) {

        String refreshToken = request.get("refreshToken");
        AuthResponse response = authService.refreshToken(refreshToken);

        return ResponseEntity.ok(ApiResponse.ok("Token renovado", response));
    }
}
