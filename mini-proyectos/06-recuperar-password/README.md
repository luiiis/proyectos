# Mini-Proyecto 06: Recuperar Contraseña por Email

## Qué aprenderás
- Generar token temporal (UUID + expiración)
- Enviar correo con link de recuperación
- Validar que el token no haya expirado
- Cambiar la contraseña de forma segura
- Invalidar el token después de usarlo

## Flujo completo
```
1. Usuario olvida su password
2. Va a /forgot-password → escribe su email
3. Backend genera token UUID + fecha expiración (30 min)
4. Backend guarda token en tabla "password_reset_tokens"
5. Backend envía email con link: /reset-password?token=abc-123
6. Usuario abre el email → click en link
7. Frontend muestra formulario de nueva contraseña
8. Usuario escribe nueva contraseña
9. Backend valida: ¿token existe? ¿no expiró? ¿no fue usado?
10. Si OK → actualiza password (BCrypt) → invalida token
11. Redirige a login con mensaje "Contraseña actualizada"
```

## Tabla SQL
```sql
CREATE TABLE password_reset_tokens (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id  BIGINT NOT NULL REFERENCES usuarios(id),
    token       VARCHAR(255) NOT NULL UNIQUE,
    expira_en   TIMESTAMP NOT NULL,
    usado       BOOLEAN DEFAULT FALSE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## Endpoints
```
POST /api/auth/forgot-password
Body: {"email": "usuario@mail.com"}
Resp: {"message": "Si el email existe, recibirás instrucciones"}

POST /api/auth/reset-password
Body: {"token": "abc-123-def", "nuevoPassword": "NuevaPass123!"}
Resp: {"message": "Contraseña actualizada exitosamente"}

GET /api/auth/validate-token?token=abc-123
Resp: {"valid": true, "email": "us***@mail.com"}
```

## Ejecutar
```bash
cd 06-recuperar-password/backend
mvn spring-boot:run

# Probar:
curl -X POST http://localhost:8080/api/auth/forgot-password \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@sistema.com"}'
# Revisa la consola: verás el email simulado con el token
```

## Seguridad implementada
- Token UUID aleatorio (no predecible)
- Expira en 30 minutos
- Solo se puede usar 1 vez
- No revela si el email existe o no (siempre responde igual)
- Password nueva se hashea con BCrypt
- Rate limiting: máximo 3 solicitudes por email por hora
