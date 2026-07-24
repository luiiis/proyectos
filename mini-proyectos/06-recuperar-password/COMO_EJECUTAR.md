# Cómo Ejecutar — Mini-Proyecto 06: Recuperar Password

## Con Docker
```cmd
cd mini-proyectos/06-recuperar-password
docker compose up -d
```

## URLs
- Backend: http://localhost:8080
- Correos capturados: http://localhost:8025

## Probar
```bash
# Solicitar recuperación
curl -X POST http://localhost:8080/api/auth/forgot-password \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@sistema.com"}'

# Ver correo en MailHog: http://localhost:8025
# Copiar el token del correo

# Restablecer contraseña
curl -X POST http://localhost:8080/api/auth/reset-password \
  -H "Content-Type: application/json" \
  -d '{"token":"TOKEN_DEL_CORREO","nuevoPassword":"NuevaPass123!"}'
```

## Parar
```cmd
docker compose down
```
