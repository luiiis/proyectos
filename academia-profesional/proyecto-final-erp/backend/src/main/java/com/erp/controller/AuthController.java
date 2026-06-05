package com.erp.controller;

import com.erp.entity.Usuario;
import com.erp.repository.UsuarioRepository;
import com.erp.security.JwtService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authManager;
    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthController(AuthenticationManager am, UsuarioRepository r, PasswordEncoder e, JwtService j) {
        this.authManager = am; this.repo = r; this.encoder = e; this.jwt = j;
    }

    record LoginReq(@NotBlank String username, @NotBlank String password) {}
    record RegisterReq(@NotBlank String username, @NotBlank String password, String email, String nombre) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginReq req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        var user = repo.findByUsername(req.username()).orElseThrow();
        return ResponseEntity.ok(Map.of("token", jwt.generate(user), "username", user.getUsername(), "rol", user.getAuthorities().iterator().next().getAuthority()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterReq req) {
        if (repo.existsByUsername(req.username())) return ResponseEntity.badRequest().body(Map.of("error", "Username ya existe"));
        var user = new Usuario();
        user.setUsername(req.username());
        user.setPassword(encoder.encode(req.password()));
        user.setEmail(req.email());
        user.setNombre(req.nombre());
        user.setRolId(3); // VENDEDOR por defecto
        repo.save(user);
        return ResponseEntity.ok(Map.of("token", jwt.generate(user), "username", user.getUsername()));
    }
}
