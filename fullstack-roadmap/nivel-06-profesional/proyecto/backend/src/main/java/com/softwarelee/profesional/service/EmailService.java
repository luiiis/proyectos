package com.softwarelee.profesional.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * Servicio de envío de correos electrónicos.
 * Usa Thymeleaf para plantillas HTML profesionales.
 */
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * Envía correo de recuperación de contraseña.
     */
    public void enviarCorreoRecuperacion(String destinatario, String nombre, String token) {
        Context context = new Context();
        context.setVariable("nombre", nombre);
        context.setVariable("token", token);
        context.setVariable("url", "http://localhost:4200/reset-password?token=" + token);

        String contenidoHtml = templateEngine.process("email-recuperacion", context);
        enviarHtml(destinatario, "Recuperación de contraseña", contenidoHtml);
    }

    /**
     * Envía correo de bienvenida (registro).
     */
    public void enviarCorreoBienvenida(String destinatario, String nombre) {
        Context context = new Context();
        context.setVariable("nombre", nombre);

        String contenidoHtml = templateEngine.process("email-bienvenida", context);
        enviarHtml(destinatario, "Bienvenido a la plataforma", contenidoHtml);
    }

    /**
     * Envío genérico de correo HTML.
     */
    private void enviarHtml(String destinatario, String asunto, String contenidoHtml) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(contenidoHtml, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            // En desarrollo, logear el error pero no detener la app
            System.err.println("Error enviando correo a " + destinatario + ": " + e.getMessage());
        }
    }
}
