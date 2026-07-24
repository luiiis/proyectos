package com.softwarelee.auth.service;

import com.softwarelee.auth.dto.LoginRequest;
import com.softwarelee.auth.dto.LoginResponse;
import com.softwarelee.auth.dto.RefreshRequest;
import com.softwarelee.auth.mapper.UsuarioMapper;
import com.softwarelee.auth.model.Rol;
import com.softwarelee.auth.model.Usuario;
import com.softwarelee.auth.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final UsuarioMapper usuarioMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioMapper usuarioMapper, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.usuarioMapper = usuarioMapper;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Proceso de login:
     * 1. Buscar usuario por username
     * 2. Verificar que el password sea correcto (BCrypt)
     * 3. Obtener sus roles
     * 4. Generar access token + refresh token
     * 5. Devolver la respuesta
     */
    public LoginResponse login(LoginRequest request) {
        // 1. Buscar usuario
        Usuario usuario = usuarioMapper.findByUsername(request.getUsername());
        if (usuario == null) {
            throw new RuntimeException("Credenciales inválidas");
        }

        // 2. Verificar password
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPasswordHash())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        // 3. Obtener roles
        List<Rol> roles = usuarioMapper.findRolesByUsuarioId(usuario.getId());
        List<String> roleNames = roles.stream()
                .map(r -> "ROLE_" + r.getNombre())
                .toList();

        // 4. Generar tokens
        String accessToken = jwtService.generateAccessToken(usuario.getUsername(), roleNames);
        String refreshToken = jwtService.generateRefreshToken(usuario.getUsername());

        // 5. Devolver respuesta
        return new LoginResponse(accessToken, refreshToken, usuario.getUsername(), roleNames);
    }

    /**
     * Renovar el access token usando el refresh token.
     */
    public LoginResponse refresh(RefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        // Validar refresh token
        if (!jwtService.isTokenValid(refreshToken)) {
            throw new RuntimeException("Refresh token inválido o expirado");
        }

        // Extraer username del refresh token
        String username = jwtService.extractUsername(refreshToken);

        // Obtener usuario y roles
        Usuario usuario = usuarioMapper.findByUsername(username);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        List<Rol> roles = usuarioMapper.findRolesByUsuarioId(usuario.getId());
        List<String> roleNames = roles.stream()
                .map(r -> "ROLE_" + r.getNombre())
                .toList();

        // Generar nuevo access token (el refresh token se mantiene)
        String newAccessToken = jwtService.generateAccessToken(username, roleNames);

        return new LoginResponse(newAccessToken, refreshToken, username, roleNames);
    }

    /**
     * Obtener información del usuario autenticado.
     */
    public Usuario getUsuarioActual(String username) {
        Usuario usuario = usuarioMapper.findByUsername(username);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuario.setRoles(usuarioMapper.findRolesByUsuarioId(usuario.getId()));
        // No exponer el hash del password
        usuario.setPasswordHash(null);
        return usuario;
    }
}
