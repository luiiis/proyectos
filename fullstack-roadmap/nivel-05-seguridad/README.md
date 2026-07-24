# Nivel 5: Seguridad Básica — Proyecto 5: Usuarios y Acceso

## Objetivo
Implementar autenticación (¿quién eres?) y autorización (¿qué puedes hacer?) con Spring Security y JWT.

## 6 Versiones

| Versión | Qué implementa |
|---------|-----------------|
| V1 | Seguridad en memoria (usuarios hardcoded) |
| V2 | Usuarios en base de datos (MyBatis) |
| V3 | Autenticación con sesión (Spring Security tradicional) |
| V4 | Autenticación con JWT (access token + filtro) |
| V5 | Refresh token (renovación + revocación) |
| V6 | Roles y permisos granulares |

## Tablas
```sql
usuarios (id, username, email, password_hash, activo, created_at)
roles (id, nombre, descripcion)
permisos (id, nombre, descripcion, modulo)
usuarios_roles (usuario_id, rol_id)
roles_permisos (rol_id, permiso_id)
```

## Roles de ejemplo
- ADMIN, SUPERVISOR, CAJERO, ALMACENISTA

## Permisos de ejemplo
- PRODUCTO_CREAR, PRODUCTO_EDITAR, PRODUCTO_ELIMINAR
- VENTA_CREAR, REPORTE_CONSULTAR

## Documentación
- Matriz de roles y permisos
- Flujo de autenticación (diagrama)
- Política de contraseñas
- Pruebas de acceso permitido y denegado
- Manual de administración de usuarios
