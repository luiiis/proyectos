# Módulo 3: Backend Mail - Explicación Completa

## 3.1 Concepto: Servicio de Correos

### ¿Qué es SMTP?
- Simple Mail Transfer Protocol: protocolo para ENVIAR correos electrónicos.
- Tu aplicación se conecta a un servidor SMTP (Gmail, Outlook, etc.) y le dice "envía este correo".
- Es como ir a la oficina de correos y entregar una carta.

### ¿Por qué un servicio separado?
- Enviar correos es LENTO (2-5 segundos por correo).
- Si está en el mismo servicio que auth, el login se vuelve lento.
- Puede escalar independientemente (si envías miles de correos).
- Si el servicio de correos falla, el login sigue funcionando.

### Alternativas a Gmail SMTP:
| Servicio | Gratis | Límite |
|----------|--------|--------|
| Gmail | Sí | 500/día |
| SendGrid | 100/día | Ilimitado (pago) |
| Mailgun | 100/día | Ilimitado (pago) |
| Amazon SES | No | $0.10 por 1000 correos |

---

## 3.2 Flujo de Recuperación de Contraseña

```
┌──────────┐     ┌──────────┐     ┌──────────┐     ┌──────────┐     ┌──────────┐
│ Frontend │     │  Nginx   │     │  Mail    │     │  MySQL   │     │  Gmail   │
│ Angular  │     │          │     │  Service │     │          │     │  SMTP    │
└────┬─────┘     └────┬─────┘     └────┬─────┘     └────┬─────┘     └────┬─────┘
     │                 │                │                │                │
     │ 1. POST /api/mail/password-reset/request         │                │
     │    {email: "user@mail.com"}     │                │                │
     │────────────────>│───────────────>│                │                │
     │                 │                │                │                │
     │                 │                │ 2. Generar UUID token           │
     │                 │                │    "a1b2c3d4-..."              │
     │                 │                │                │                │
     │                 │                │ 3. INSERT INTO password_reset_tokens
     │                 │                │───────────────>│                │
     │                 │                │<───────────────│                │
     │                 │                │                │                │
     │                 │                │ 4. Construir email HTML         │
     │                 │                │    con link: /reset-password?token=a1b2c3d4
     │                 │                │                │                │
     │                 │                │ 5. Enviar via SMTP─────────────>│
     │                 │                │<───────────────────────────────│ OK
     │                 │                │                │                │
     │                 │                │ 6. INSERT INTO email_logs       │
     │                 │                │    status=SENT │                │
     │                 │                │───────────────>│                │
     │                 │                │                │                │
     │<────────────────│<───────────────│                │                │
     │ {success: true, message: "Correo enviado"}       │                │
     │                 │                │                │                │
     │ [Usuario revisa su email, hace clic en el link]  │                │
     │                 │                │                │                │
     │ 7. GET /reset-password?token=a1b2c3d4            │                │
     │ [Angular muestra formulario de nueva contraseña] │                │
     │                 │                │                │                │
     │ 8. POST /api/mail/password-reset/validate        │                │
     │    {token: "a1b2c3d4"}          │                │                │
     │────────────────>│───────────────>│                │                │
     │                 │                │ 9. SELECT FROM password_reset_tokens
     │                 │                │    WHERE token='a1b2c3d4'       │
     │                 │                │───────────────>│                │
     │                 │                │<───────────────│                │
     │                 │                │                │                │
     │                 │                │ 10. Verificar: │                │
     │                 │                │     ¿Expirado? (expires_at < NOW)
     │                 │                │     ¿Ya usado? (used = true)   │
     │                 │                │                │                │
     │<────────────────│<───────────────│                │                │
     │ {success: true, email: "user@mail.com"}          │                │
     │                 │                │                │                │
     │ [Usuario escribe nueva contraseña]               │                │
     │                 │                │                │                │
     │ 11. POST /api/mail/password-reset/confirm        │                │
     │     {token: "a1b2c3d4", newPassword: "nuevo123"} │                │
     │────────────────>│───────────────>│                │                │
     │                 │                │ 12. UPDATE password_reset_tokens
     │                 │                │     SET used=true               │
     │                 │                │───────────────>│                │
     │                 │                │                │                │
     │<────────────────│<───────────────│                │                │
     │ {success: true} │                │                │                │
     │                 │                │                │                │
     │ [Angular redirige a /login]      │                │                │
```

---

## 3.3 Archivo: `EmailService.java` (Línea por Línea)

```java
@Service
@RequiredArgsConstructor
@Slf4j  // Lombok: genera un logger automáticamente (log.info(), log.error())
public class EmailService {

    // JavaMailSender: interfaz de Spring para enviar correos
    // Spring la configura automáticamente con los datos de application.yml
    private final JavaMailSender mailSender;
    private final EmailLogRepository emailLogRepository;

    public void sendEmail(String to, String subject, String htmlBody) {
        // 1. Crear registro de log ANTES de enviar (estado PENDING)
        EmailLog emailLog = EmailLog.builder()
                .to(to)
                .subject(subject)
                .body(htmlBody)
                .status(EmailLog.EmailStatus.PENDING)
                .build();

        try {
            // 2. Crear mensaje MIME (soporta HTML, adjuntos, etc.)
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            // true = multipart (permite adjuntos)
            // UTF-8 = codificación (soporta acentos, ñ, etc.)

            helper.setTo(to);           // Destinatario
            helper.setSubject(subject); // Asunto
            helper.setText(htmlBody, true);  // true = es HTML (no texto plano)

            // 3. ENVIAR el correo (aquí se conecta a Gmail SMTP)
            // Esto puede tardar 2-5 segundos (conexión de red)
            mailSender.send(message);

            // 4. Si llegamos aquí, se envió correctamente
            emailLog.setStatus(EmailLog.EmailStatus.SENT);
            emailLog.setSentAt(LocalDateTime.now());
            log.info("Email enviado exitosamente a: {}", to);

        } catch (MessagingException e) {
            // 5. Si falla (servidor SMTP caído, email inválido, etc.)
            emailLog.setStatus(EmailLog.EmailStatus.FAILED);
            emailLog.setErrorMessage(e.getMessage());
            log.error("Error enviando email a {}: {}", to, e.getMessage());
        }

        // 6. SIEMPRE guardar el log (éxito o fallo)
        emailLogRepository.save(emailLog);
    }

    public void sendPasswordResetEmail(String to, String resetLink) {
        String subject = "Recuperación de Contraseña";
        // Template HTML del correo (text block de Java 17)
        String body = """
                <html>
                <body>
                    <h2>Recuperación de Contraseña</h2>
                    <p>Haz clic en el siguiente enlace:</p>
                    <a href="%s">Restablecer Contraseña</a>
                    <p>Este enlace expirará en 1 hora.</p>
                </body>
                </html>
                """.formatted(resetLink);
        // %s se reemplaza por resetLink (ej: http://localhost:4200/reset-password?token=abc)

        sendEmail(to, subject, body);
    }
}
```

---

## 3.4 Archivo: `PasswordResetService.java`

```java
@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;

    @Value("${app.frontend-url}")  // Lee de application.yml
    private String frontendUrl;    // "http://localhost:4200"

    @Value("${app.password-reset-expiration}")
    private long expirationMs;     // 3600000 (1 hora en milisegundos)

    public void requestPasswordReset(String email) {
        // 1. Invalidar tokens anteriores (solo puede haber 1 activo)
        tokenRepository.findByEmailAndUsedFalse(email)
                .ifPresent(token -> {
                    token.setUsed(true);  // Marcar como usado
                    tokenRepository.save(token);
                });

        // 2. Generar token único (UUID = Universally Unique Identifier)
        // Ejemplo: "550e8400-e29b-41d4-a716-446655440000"
        // Probabilidad de colisión: 1 en 2^122 (prácticamente imposible)
        String token = UUID.randomUUID().toString();

        // 3. Guardar en BD con fecha de expiración
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .email(email)
                .expiresAt(LocalDateTime.now().plusSeconds(expirationMs / 1000))
                // plusSeconds(3600) = expira en 1 hora
                .build();
        tokenRepository.save(resetToken);

        // 4. Construir link y enviar correo
        // El link lleva al frontend Angular con el token como query param
        String resetLink = frontendUrl + "/reset-password?token=" + token;
        emailService.sendPasswordResetEmail(email, resetLink);
    }

    public String validateToken(String token) {
        // Buscar el token en la BD
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        // Verificar que no haya expirado
        if (resetToken.isExpired()) {
            throw new RuntimeException("Token expirado");
        }

        // Verificar que no se haya usado ya
        if (resetToken.getUsed()) {
            throw new RuntimeException("Token ya fue utilizado");
        }

        // Si pasa todas las validaciones, devolver el email asociado
        return resetToken.getEmail();
    }
}
```

---

## 3.5 ¿Por qué el Backend Mail comparte BD con Backend Auth?

```
Razón: El token de reset necesita estar asociado al EMAIL del usuario.
Cuando el usuario confirma el reset, el backend-auth necesita:
1. Saber que el token es válido (backend-mail lo valida)
2. Actualizar el password del usuario (backend-auth lo hace)

Flujo real completo:
1. Frontend → backend-mail: "quiero reset para user@mail.com"
2. backend-mail: genera token, guarda en MySQL, envía correo
3. Usuario hace clic en link → Frontend muestra formulario
4. Frontend → backend-mail: "valida token abc123"
5. backend-mail: token válido, devuelve email
6. Frontend → backend-auth: "cambia password de user@mail.com a nuevo123"
7. backend-auth: actualiza en MySQL

Ambos backends leen/escriben en la MISMA base MySQL (auth_db)
porque comparten la tabla de usuarios y tokens.
```
