package com.softwarelee.profesional.controller;

import com.softwarelee.profesional.mapper.UsuarioMapper;
import com.softwarelee.profesional.model.Usuario;
import com.softwarelee.profesional.service.AuditoriaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class PerfilController {

    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuditoriaService auditoriaService;

    public PerfilController(UsuarioMapper usuarioMapper,
                            PasswordEncoder passwordEncoder,
                            AuditoriaService auditoriaService) {
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
        this.auditoriaService = auditoriaService;
    }

    @GetMapping("/perfil")
    public ResponseEntity<Map<String, Object>> perfil(Authentication auth) {
        Usuario usuario = usuarioMapper.findByUsername(auth.getName());
        usuario.setPasswordHash(null); // No exponer hash
        return ResponseEntity.ok(Map.of("success", true, "data", usuario));
    }

    @PutMapping("/perfil")
    public ResponseEntity<Map<String, Object>> actualizarPerfil(
            @RequestBody Map<String, String> datos,
            Authentication auth,
            HttpServletRequest request) {

        Usuario usuario = usuarioMapper.findByUsername(auth.getName());
        usuarioMapper.actualizarPerfil(
                usuario.getId(),
                datos.getOrDefault("nombre", usuario.getNombre()),
                datos.getOrDefault("email", usuario.getEmail())
        );

        auditoriaService.registrar(usuario.getId(), "PERFIL_ACTUALIZADO",
                "Perfil actualizado", request.getRemoteAddr());

        return ResponseEntity.ok(Map.of("success", true, "message", "Perfil actualizado"));
    }

    @PostMapping("/cambiar-password")
    public ResponseEntity<Map<String, Object>> cambiarPassword(
            @RequestBody Map<String, String> datos,
            Authentication auth,
            HttpServletRequest request) {

        Usuario usuario = usuarioMapper.findByUsername(auth.getName());

        String passwordActual = datos.get("passwordActual");
        String passwordNuevo = datos.get("passwordNuevo");

        if (!passwordEncoder.matches(passwordActual, usuario.getPasswordHash())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "error", "Contraseña actual incorrecta"));
        }

        String nuevoHash = passwordEncoder.encode(passwordNuevo);
        usuarioMapper.actualizarPassword(usuario.getId(), nuevoHash);

        auditoriaService.registrar(usuario.getId(), "PASSWORD_CAMBIADO",
                "Contraseña cambiada por el usuario", request.getRemoteAddr());

        return ResponseEntity.ok(Map.of("success", true, "message", "Contraseña actualizada"));
    }
}
