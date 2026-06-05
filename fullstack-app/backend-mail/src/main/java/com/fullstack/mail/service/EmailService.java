package com.fullstack.mail.service;

import com.fullstack.mail.entity.EmailLog;
import com.fullstack.mail.repository.EmailLogRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Servicio de envío de correos electrónicos.
 * Soporta correos HTML y texto plano con registro de logs.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailLogRepository emailLogRepository;

    public void sendEmail(String to, String subject, String htmlBody) {
        EmailLog emailLog = EmailLog.builder()
                .to(to)
                .subject(subject)
                .body(htmlBody)
                .status(EmailLog.EmailStatus.PENDING)
                .build();

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(message);

            emailLog.setStatus(EmailLog.EmailStatus.SENT);
            emailLog.setSentAt(LocalDateTime.now());
            log.info("Email enviado exitosamente a: {}", to);

        } catch (MessagingException e) {
            emailLog.setStatus(EmailLog.EmailStatus.FAILED);
            emailLog.setErrorMessage(e.getMessage());
            log.error("Error enviando email a {}: {}", to, e.getMessage());
        }

        emailLogRepository.save(emailLog);
    }

    public void sendPasswordResetEmail(String to, String resetLink) {
        String subject = "Recuperación de Contraseña";
        String body = """
                <html>
                <body style="font-family: Arial, sans-serif; padding: 20px;">
                    <h2>Recuperación de Contraseña</h2>
                    <p>Has solicitado restablecer tu contraseña.</p>
                    <p>Haz clic en el siguiente enlace para crear una nueva contraseña:</p>
                    <a href="%s" style="background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">
                        Restablecer Contraseña
                    </a>
                    <p style="margin-top: 20px; color: #666;">
                        Este enlace expirará en 1 hora. Si no solicitaste este cambio, ignora este correo.
                    </p>
                </body>
                </html>
                """.formatted(resetLink);

        sendEmail(to, subject, body);
    }

    public void sendWelcomeEmail(String to, String username) {
        String subject = "Bienvenido al Sistema";
        String body = """
                <html>
                <body style="font-family: Arial, sans-serif; padding: 20px;">
                    <h2>¡Bienvenido, %s!</h2>
                    <p>Tu cuenta ha sido creada exitosamente.</p>
                    <p>Ya puedes iniciar sesión y comenzar a usar el sistema.</p>
                </body>
                </html>
                """.formatted(username);

        sendEmail(to, subject, body);
    }
}
