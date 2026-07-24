# Cómo Ejecutar — Mini-Proyecto 11: Enviar Email

## Con Docker
```cmd
cd mini-proyectos/11-enviar-email
docker compose up -d
```

## URLs
- Backend: http://localhost:8080
- Ver correos: http://localhost:8025

## Probar
```bash
curl -X POST http://localhost:8080/api/email/texto \
  -H "Content-Type: application/json" \
  -d '{"to":"test@example.com","subject":"Prueba","body":"Hola desde Spring Boot!"}'

curl -X POST http://localhost:8080/api/email/html \
  -H "Content-Type: application/json" \
  -d '{"to":"test@example.com","subject":"HTML","body":"Esto es <b>HTML</b>"}'
```
Abre http://localhost:8025 para ver los correos capturados.

## Parar
```cmd
docker compose down
```
