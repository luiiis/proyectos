package com.academia.ventas.controller;

import com.academia.ventas.entity.Usuario;
import com.academia.ventas.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authManager, JwtService jwtService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    record LoginRequest(String username, String password) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        var user = (Usuario) auth.getPrincipal();
        String token = jwtService.generate(user);
        return ResponseEntity.ok(Map.of(
            "token", token,
            "username", user.getUsername(),
            "nombre", user.getNombre(),
            "rol", user.getRol().getNombre()
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth) {
        var user = (Usuario) auth.getPrincipal();
        return ResponseEntity.ok(Map.of(
            "username", user.getUsername(),
            "nombre", user.getNombre(),
            "rol", user.getRol().getNombre()
        ));
    }
}
