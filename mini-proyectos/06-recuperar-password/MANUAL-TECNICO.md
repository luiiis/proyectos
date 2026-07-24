# Manual Técnico — Mini-Proyecto 06: Recuperar Password

## Arquitectura
```
Cliente → POST /forgot-password → Genera token UUID → Guarda en BD → Envía email
Cliente → POST /reset-password → Valida token → Cambia password (BCrypt) → Invalida token
```

## Stack
| Componente | Tecnología |
|-----------|-----------|
| Backend | Spring Boot 3.3 + Spring Mail |
| BD | MySQL 8 + Flyway |
| SMTP | MailHog (simulador local) |

## Servicios Docker
| Servicio | Puerto | Función |
|----------|--------|---------|
| backend | 8080 | API REST |
| mysql | 3306 | Base de datos |
| mailhog | 8025 | UI correos capturados |

## Seguridad
- Token UUID no predecible
- Expira en 30 minutos
- Solo se usa 1 vez
- Password nuevo hasheado con BCrypt
- No revela si el email existe
