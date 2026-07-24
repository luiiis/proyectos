# Mini-Proyecto 11: Enviar Email con Spring Boot

## Qué aprenderás
- Configurar Spring Mail con Gmail SMTP
- Enviar email de texto plano
- Enviar email con HTML bonito (plantilla Thymeleaf)
- Enviar email con adjuntos (PDF, imagen)
- Manejo de errores (SMTP falla, email inválido)
- Envío asíncrono (@Async para no bloquear)

## Configuración (application.yml)
```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${MAIL_USER:tu-correo@gmail.com}
    password: ${MAIL_PASS:tu-app-password}
    properties:
      mail.smtp.auth: true
      mail.smtp.starttls.enable: true
```

## Versiones

### V1: Email simple (texto)
```java
@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public void enviarTexto(String to, String subject, String body) {
        var message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("noreply@sistema.com");
        mailSender.send(message);
    }
}
```

### V2: Email con HTML (plantilla)
```java
public void enviarHtml(String to, String subject, String htmlContent) {
    var message = mailSender.createMimeMessage();
    var helper = new MimeMessageHelper(message, true, "UTF-8");
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(htmlContent, true); // true = es HTML
    helper.setFrom("noreply@sistema.com");
    mailSender.send(message);
}
```

### V3: Email con adjunto
```java
public void enviarConAdjunto(String to, String subject, String body, File adjunto) {
    var message = mailSender.createMimeMessage();
    var helper = new MimeMessageHelper(message, true);
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(body);
    helper.addAttachment(adjunto.getName(), adjunto);
    mailSender.send(message);
}
```

### V4: Envío asíncrono
```java
@Async // No bloquea el hilo principal
public void enviarAsync(String to, String subject, String body) {
    // Mismo código pero se ejecuta en otro hilo
    enviarTexto(to, subject, body);
}
```

## Endpoints para probar
```
POST /api/email/texto
Body: {"to": "destino@mail.com", "subject": "Prueba", "body": "Hola mundo"}

POST /api/email/html
Body: {"to": "destino@mail.com", "subject": "Bienvenido", "template": "bienvenida", "datos": {"nombre": "Carlos"}}

POST /api/email/adjunto
Form-data: to, subject, body, file (multipart)
```

## Ejecutar
```bash
cd 11-enviar-email/backend
# Configurar variables de entorno:
set MAIL_USER=tu-correo@gmail.com
set MAIL_PASS=tu-app-password-de-16-caracteres
mvn spring-boot:run

# Probar:
curl -X POST http://localhost:8080/api/email/texto \
  -H "Content-Type: application/json" \
  -d '{"to":"tu-otro-email@mail.com","subject":"Prueba","body":"Funciona!"}'
```

## Obtener App Password de Gmail
```
1. Ir a myaccount.google.com
2. Seguridad → Verificación en 2 pasos → ACTIVAR
3. Ir a: myaccount.google.com/apppasswords
4. Seleccionar app: "Otra" → nombre: "Spring Boot"
5. Google te da 16 caracteres → ESE es tu MAIL_PASS
```
