# API Reference - Endpoints JSON

## Base URLs
- Auth Service: `http://localhost:8080/api`
- Mail Service: `http://localhost:8081/api`

## Formato de Respuesta Estándar

```json
{
  "success": true,
  "message": "Operación exitosa",
  "data": { ... },
  "timestamp": "2024-01-15T10:30:00"
}
```

---

## Autenticación

### POST /api/auth/register
Registrar nuevo usuario.

**Request:**
```json
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "miPassword123",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "+52 555 1234567"
}
```

**Response (200):**
```json
{
  "success": true,
  "message": "Usuario registrado exitosamente",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
    "username": "johndoe",
    "email": "john@example.com",
    "roles": ["USER"],
    "expiresIn": 86400000
  }
}
```

### POST /api/auth/login
Iniciar sesión.

**Request:**
```json
{
  "username": "johndoe",
  "password": "miPassword123"
}
```

**Response (200):**
```json
{
  "success": true,
  "message": "Login exitoso",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
    "username": "johndoe",
    "email": "john@example.com",
    "roles": ["USER", "ADMIN"],
    "expiresIn": 86400000
  }
}
```

### POST /api/auth/refresh
Renovar access token.

**Request:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

## Usuarios

> Requiere header: `Authorization: Bearer <accessToken>`

### GET /api/users
Listar todos los usuarios (solo ADMIN).

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "username": "admin",
      "email": "admin@example.com",
      "firstName": "Admin",
      "lastName": "System",
      "phone": null,
      "isActive": true,
      "roles": ["ADMIN", "USER"],
      "createdAt": "2024-01-15T08:00:00"
    }
  ]
}
```

### PUT /api/users/{id}
Actualizar usuario.

**Request:**
```json
{
  "firstName": "John Updated",
  "lastName": "Doe Updated",
  "phone": "+52 555 9876543",
  "email": "newemail@example.com"
}
```

### POST /api/users/{id}/roles
Asignar rol a usuario (solo ADMIN).

**Request:**
```json
{
  "role": "MANAGER"
}
```

### POST /api/users/{id}/change-password
Cambiar contraseña.

**Request:**
```json
{
  "oldPassword": "passwordActual",
  "newPassword": "nuevoPassword123"
}
```

---

## Productos

> Requiere header: `Authorization: Bearer <accessToken>`

### GET /api/products
Listar productos activos.

### GET /api/products/search?name=laptop
Buscar por nombre.

### GET /api/products/category/electronica
Filtrar por categoría.

### GET /api/products/low-stock?threshold=10
Productos con stock bajo (ADMIN/MANAGER).

### POST /api/products
Crear producto (ADMIN/MANAGER).

**Request:**
```json
{
  "name": "Laptop HP ProBook",
  "description": "Laptop empresarial 14 pulgadas",
  "price": 15999.99,
  "stock": 50,
  "category": "electronica",
  "sku": "HP-PB-450-G9"
}
```

### PUT /api/products/{id}
Actualizar producto (ADMIN/MANAGER).

### DELETE /api/products/{id}
Eliminar producto - soft delete (ADMIN).

### POST /api/products/{id}/stock/add
Agregar stock (ADMIN/MANAGER).

**Request:**
```json
{
  "quantity": 25,
  "reason": "Compra a proveedor #12345"
}
```

### POST /api/products/{id}/stock/remove
Retirar stock (ADMIN/MANAGER).

**Request:**
```json
{
  "quantity": 5,
  "reason": "Venta orden #67890"
}
```

---

## Servicio de Correos

### POST /api/mail/password-reset/request
Solicitar recuperación de contraseña (público).

**Request:**
```json
{
  "email": "john@example.com"
}
```

### POST /api/mail/password-reset/validate
Validar token de recuperación.

**Request:**
```json
{
  "token": "uuid-token-recibido-por-email"
}
```

### POST /api/mail/password-reset/confirm
Confirmar nueva contraseña.

**Request:**
```json
{
  "token": "uuid-token",
  "newPassword": "miNuevoPassword123"
}
```

### POST /api/mail/send
Enviar correo personalizado (autenticado).

**Request:**
```json
{
  "to": "destinatario@example.com",
  "subject": "Asunto del correo",
  "body": "<h1>Contenido HTML</h1><p>Mensaje</p>"
}
```

---

## Códigos de Error

| Código | Significado |
|--------|-------------|
| 200 | Éxito |
| 400 | Error de validación o lógica de negocio |
| 401 | No autenticado (token inválido/expirado) |
| 403 | No autorizado (sin permisos) |
| 404 | Recurso no encontrado |
| 500 | Error interno del servidor |

**Ejemplo error 400:**
```json
{
  "success": false,
  "message": "Errores de validación",
  "data": {
    "username": "Username debe tener entre 3 y 50 caracteres",
    "email": "Email debe ser válido"
  }
}
```

**Ejemplo error 401:**
```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required",
  "path": "/api/users"
}
```
