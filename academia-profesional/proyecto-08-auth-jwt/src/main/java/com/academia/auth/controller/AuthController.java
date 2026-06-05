package com.academia.auth.controller;

import com.academia.auth.entity.Usuario;
import com.academia.auth.repository.UsuarioRepository;
import com.academia.auth.security.JwtService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authManager, UsuarioRepository repo,
                          PasswordEncoder encoder, JwtService jwtService) {
        this.authManager = authManager;
        this.repo = repo;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    record LoginRequest(@NotBlank String username, @NotBlank String password) {}
    record RegisterRequest(@NotBlank @Size(min=3) String username, @NotBlank @Size(min=6) String password,
                           @Email String email, String nombre, String apellido) {}
    record AuthResponse(String token, String username, String rol, long expiresIn) {}

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        var user = repo.findByUsername(req.username()).orElseThrow();
        var token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getRol().name(), 86400000));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        if (repo.existsByUsername(req.username()))
            return ResponseEntity.badRequest().body(null);

        var user = new Usuario();
        user.setUsername(req.username());
        user.setPassword(encoder.encode(req.password()));
        user.setEmail(req.email());
        user.setNombre(req.nombre());
        user.setApellido(req.apellido());
        user.setRol(Usuario.Rol.USER);
        repo.save(user);

        var token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getRol().name(), 86400000));
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(@RequestHeader("Authorization") String header) {
        String token = header.substring(7);
        String username = jwtService.extractUsername(token);
        var user = repo.findByUsername(username).orElseThrow();
        return ResponseEntity.ok(Map.of(
            "id", user.getId(), "username", user.getUsername(),
            "email", user.getEmail() != null ? user.getEmail() : "",
            "nombre", user.getNombre() != null ? user.getNombre() : "",
            "rol", user.getRol().name()
        ));
    }
}
