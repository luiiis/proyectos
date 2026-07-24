# Manual Técnico — Mini-Proyecto 11: Enviar Email

## Stack
| Componente | Tecnología |
|-----------|-----------|
| Backend | Spring Boot 3.3 + Spring Mail + Thymeleaf |
| SMTP Local | MailHog (captura correos sin enviarlos) |

## Servicios Docker
| Servicio | Puerto | Función |
|----------|--------|---------|
| backend | 8080 | API REST para enviar emails |
| mailhog | 8025 | UI web para ver correos |
| mailhog | 1025 | SMTP (recibe correos del backend) |

## Endpoints
| Método | URL | Función |
|--------|-----|---------|
| POST | /api/email/texto | Enviar email de texto plano |
| POST | /api/email/html | Enviar email con HTML |
