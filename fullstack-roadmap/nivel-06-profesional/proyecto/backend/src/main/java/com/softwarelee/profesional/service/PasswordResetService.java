package com.softwarelee.profesional.service;

import com.softwarelee.profesional.mapper.PasswordResetMapper;
import com.softwarelee.profesional.mapper.UsuarioMapper;
import com.softwarelee.profesional.model.PasswordResetToken;
import com.softwarelee.profesional.model.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Servicio de recuperación de contraseña.
 *
 * Flujo:
 * 1. Usuario solicita recuperación con su email
 * 2. Se genera un token UUID y se guarda en BD con expiración
 * 3. Se envía un correo con un link que contiene el token
 * 4. Usuario hace click en el link → llega al frontend con el token
 * 5. Frontend envía: token + nueva contraseña
 * 6. Backend valida token, cambia password, marca token como usado
 */
@Service
public class PasswordResetService {

    private final UsuarioMapper usuarioMapper;
    private final PasswordResetMapper resetMapper;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(UsuarioMapper usuarioMapper,
                                 PasswordResetMapper resetMapper,
                                 EmailService emailService,
                                 PasswordEncoder passwordEncoder) {
        this.usuarioMapper = usuarioMapper;
        this.resetMapper = resetMapper;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Solicita recuperación de contraseña.
     */
    public void solicitarRecuperacion(String email) {
        Usuario usuario = usuarioMapper.findByEmail(email);
        if (usuario == null) {
            // No revelar si el email existe o no (seguridad)
            return;
        }

        // Invalidar tokens anteriores del usuario
        resetMapper.invalidarTokensPorUsuario(usuario.getId());

        // Generar nuevo token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUsuarioId(usuario.getId());
        resetToken.setExpiracion(LocalDateTime.now().plusHours(1));
        resetMapper.insert(resetToken);

        // Enviar correo
        emailService.enviarCorreoRecuperacion(email, usuario.getNombre(), token);
    }

    /**
     * Restablece la contraseña con el token.
     */
    public void restablecerPassword(String token, String nuevoPassword) {
        PasswordResetToken resetToken = resetMapper.findByToken(token);

        if (resetToken == null) {
            throw new RuntimeException("Token inválido");
        }
        if (resetToken.isUsado()) {
            throw new RuntimeException("Este token ya fue utilizado");
        }
        if (resetToken.getExpiracion().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado");
        }

        // Cambiar password
        String hash = passwordEncoder.encode(nuevoPassword);
        usuarioMapper.actualizarPassword(resetToken.getUsuarioId(), hash);

        // Marcar token como usado
        resetMapper.marcarComoUsado(resetToken.getId());
    }
}
