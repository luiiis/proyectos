# Nivel 6: Funciones Profesionales — Proyecto 6: Perfiles y Recuperación

## Objetivo
Agregar funciones que toda app real necesita: perfil, correo, recuperación de password, auditoría.

## Funcionalidades
- Perfil de usuario (ver/editar)
- Cambiar contraseña
- Recuperar contraseña (correo con token)
- Confirmar correo electrónico
- Bloquear usuarios por intentos fallidos
- Activar/desactivar cuentas
- Auditoría de accesos
- Tareas programadas (limpiar tokens expirados)

## Versiones del correo
| Versión | Implementación |
|---------|----------------|
| V1 | Correo simulado (imprime en consola) |
| V2 | SMTP de Gmail (desarrollo) |
| V3 | Plantillas HTML para correos |
| V4 | Cola de correos (retry si falla) |

## Documentación
- Configuración SMTP
- Flujo de recuperación de contraseña
- Manual de plantillas de correo
- Pruebas de expiración de tokens
- Manual de seguridad
