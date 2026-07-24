package com.softwarelee.email;

import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final JavaMailSender mailSender;

    public EmailController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @PostMapping("/texto")
    public ResponseEntity<Map<String, Object>> enviarTexto(@RequestBody Map<String, String> body) {
        var message = new SimpleMailMessage();
        message.setTo(body.get("to"));
        message.setSubject(body.get("subject"));
        message.setText(body.get("body"));
        message.setFrom("noreply@softwarelee.com");
        mailSender.send(message);
        return ResponseEntity.ok(Map.of("success", true, "message", "Email enviado"));
    }

    @PostMapping("/html")
    public ResponseEntity<Map<String, Object>> enviarHtml(@RequestBody Map<String, String> body) throws Exception {
        var message = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(body.get("to"));
        helper.setSubject(body.get("subject"));
        helper.setText("<h1>Hola</h1><p>" + body.get("body") + "</p>", true);
        helper.setFrom("noreply@softwarelee.com");
        mailSender.send(message);
        return ResponseEntity.ok(Map.of("success", true, "message", "Email HTML enviado"));
    }
}
