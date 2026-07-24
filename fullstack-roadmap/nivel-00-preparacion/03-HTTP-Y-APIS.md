# HTTP y APIs — Lo que necesitas ANTES del Nivel 1

## ¿Qué es una API?
Una API es un programa que RECIBE peticiones y DEVUELVE datos. Tu frontend (Angular) le pide datos al backend (Spring Boot) a través de HTTP.

### Analogía: Un restaurante
```
Cliente (frontend)  →  Mesero (API)  →  Cocina (backend + BD)
  "Quiero la carta"     lleva el pedido     prepara la respuesta
  recibe la comida  ←   trae la respuesta ←  busca en la BD
```

---

## 1. ¿Qué es HTTP?

HTTP es el **protocolo** (lenguaje) que usan los navegadores y las apps para comunicarse con servidores.

Cada comunicación tiene:
- **Request** (lo que ENVÍAS): método + URL + headers + body
- **Response** (lo que RECIBES): status code + headers + body

```
REQUEST:
┌─────────────────────────────────────────┐
│ GET /api/productos HTTP/1.1             │ ← Método + ruta
│ Host: localhost:8080                     │ ← A qué servidor
│ Authorization: Bearer eyJ...            │ ← Tu token (si es protegido)
│ Content-Type: application/json          │ ← Formato del body
│                                          │
│ (sin body en GET)                        │
└─────────────────────────────────────────┘

RESPONSE:
┌─────────────────────────────────────────┐
│ HTTP/1.1 200 OK                         │ ← Código de estado
│ Content-Type: application/json          │ ← Formato de la respuesta
│                                          │
│ {"success": true, "data": [...]}        │ ← Body (JSON)
└─────────────────────────────────────────┘
```

---

## 2. Métodos HTTP (verbos)

| Método | Para qué | Ejemplo | ¿Tiene body? |
|--------|----------|---------|:---:|
| **GET** | Obtener datos | Listar productos, buscar por ID | No |
| **POST** | Crear algo nuevo | Crear un producto, hacer login | Sí |
| **PUT** | Reemplazar completo | Actualizar un producto | Sí |
| **PATCH** | Modificar parcial | Cambiar solo el precio | Sí |
| **DELETE** | Eliminar | Borrar un producto | No |

### Regla fácil
- **GET** = leer (no cambia nada)
- **POST** = crear (agrega algo nuevo)
- **PUT** = actualizar (reemplaza todo)
- **DELETE** = eliminar

---

## 3. URLs y Endpoints

```
http://localhost:8080/api/productos/5?activo=true
│       │              │        │    │
│       │              │        │    └── Query param (filtro)
│       │              │        └── Path variable (el ID)
│       │              └── Recurso (qué estás pidiendo)
│       └── Host:puerto (dónde está el servidor)
└── Protocolo
```

### Convenciones de endpoints REST
```
GET    /api/productos          → Listar todos
GET    /api/productos/5        → Obtener el producto con ID 5
GET    /api/productos?nombre=laptop → Buscar por nombre
POST   /api/productos          → Crear nuevo producto
PUT    /api/productos/5        → Actualizar producto 5
DELETE /api/productos/5        → Eliminar producto 5
```

### Partes de la URL
| Parte | Qué es | Ejemplo |
|-------|--------|---------|
| Base URL | Dirección del servidor | `http://localhost:8080` |
| Path | Ruta al recurso | `/api/productos` |
| Path Variable | Dato dentro de la ruta | `/api/productos/{id}` → `/api/productos/5` |
| Query Param | Filtro después del `?` | `?nombre=laptop&page=0&size=10` |

---

## 4. Códigos de Estado HTTP

### Los que usarás todos los días

| Código | Nombre | Significado | Cuándo |
|--------|--------|-------------|--------|
| **200** | OK | Todo bien | GET exitoso, PUT exitoso |
| **201** | Created | Se creó algo | POST exitoso |
| **204** | No Content | Éxito, sin body | DELETE exitoso |
| **400** | Bad Request | Datos inválidos | Validación falló |
| **401** | Unauthorized | No estás autenticado | Sin token o token inválido |
| **403** | Forbidden | No tienes permiso | Token válido pero sin rol |
| **404** | Not Found | No existe | ID no encontrado |
| **500** | Internal Server Error | Bug en el servidor | Error no manejado |

### Cómo recordarlos
- **2xx** = ✅ Todo bien
- **4xx** = ❌ Error del CLIENTE (tú mandaste algo mal)
- **5xx** = 💥 Error del SERVIDOR (bug en el código)

---

## 5. JSON — El formato de datos

JSON (JavaScript Object Notation) es el formato universal para intercambiar datos entre frontend y backend.

```json
{
  "id": 1,
  "nombre": "Laptop HP ProBook",
  "precio": 18999.00,
  "existencia": 25,
  "activo": true,
  "categoriaId": 1,
  "categoriaNombre": "Electrónica",
  "createdAt": "2026-01-15T10:30:00"
}
```

### Reglas de JSON
- Las claves van entre comillas: `"nombre"`
- Texto entre comillas: `"Laptop HP"`
- Números sin comillas: `18999.00`
- Booleanos: `true` o `false`
- Null: `null`
- Arrays: `[1, 2, 3]` o `[{"id": 1}, {"id": 2}]`
- Objetos: `{"clave": "valor"}`

### JSON para listas (array de objetos)
```json
{
  "success": true,
  "data": [
    {"id": 1, "nombre": "Laptop", "precio": 18999},
    {"id": 2, "nombre": "Mouse", "precio": 899},
    {"id": 3, "nombre": "Monitor", "precio": 12499}
  ],
  "total": 3
}
```

### JSON para crear un producto (lo que ENVÍAS en POST)
```json
{
  "nombre": "Teclado Mecánico",
  "precio": 2500.00,
  "existencia": 30,
  "categoriaId": 2,
  "sku": "TEC-MEC-001"
}
```

---

## 6. Headers (Encabezados)

Los headers son metadatos que acompañan cada petición/respuesta.

### Headers que usarás
| Header | Para qué | Ejemplo |
|--------|----------|---------|
| `Content-Type` | Formato del body | `application/json` |
| `Authorization` | Tu token de acceso | `Bearer eyJhbGciOi...` |
| `Accept` | Qué formato esperas recibir | `application/json` |

### Ejemplo completo con Authorization
```
POST /api/productos HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiJ9.abc123

{"nombre": "Nuevo Producto", "precio": 999}
```

---

## 7. Probar APIs con curl

```bash
# GET simple
curl http://localhost:8080/api/productos

# GET con formato bonito (pipe a jq si lo tienes)
curl http://localhost:8080/api/productos | jq

# POST (crear)
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Tablet","precio":8999,"existencia":12,"categoriaId":1}'

# PUT (actualizar)
curl -X PUT http://localhost:8080/api/productos/1 \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Laptop HP v2","precio":19999,"existencia":20,"categoriaId":1}'

# DELETE
curl -X DELETE http://localhost:8080/api/productos/5

# Con autenticación (JWT)
curl http://localhost:8080/api/productos \
  -H "Authorization: Bearer TU_TOKEN_AQUI"
```

---

## 8. Probar APIs con Postman

### Paso a paso:
```
1. Abrir Postman
2. Click "+" (nueva petición)
3. Seleccionar método (GET/POST/PUT/DELETE)
4. Pegar URL: http://localhost:8080/api/productos
5. Si es POST/PUT:
   - Tab "Body" → raw → JSON
   - Escribir el JSON
6. Si necesita token:
   - Tab "Headers"
   - Key: Authorization
   - Value: Bearer tu_token_aqui
7. Click "Send"
8. Ver la respuesta abajo
```

---

## 9. REST — Las reglas de una buena API

REST es un conjunto de convenciones para diseñar APIs limpias:

| Regla | Ejemplo bueno | Ejemplo malo |
|-------|---------------|--------------|
| Usar sustantivos en plural | `/api/productos` | `/api/getProductos` |
| IDs en la URL | `/api/productos/5` | `/api/productos?id=5` |
| Método HTTP define la acción | `DELETE /api/productos/5` | `POST /api/borrarProducto` |
| Respuestas consistentes | Siempre JSON con misma estructura | A veces texto, a veces JSON |
| Códigos HTTP correctos | 201 al crear, 404 si no existe | Siempre 200 con error en body |

### Buena estructura REST
```
/api/productos              → GET (listar), POST (crear)
/api/productos/{id}         → GET (uno), PUT (actualizar), DELETE (eliminar)
/api/productos/buscar       → GET con query params
/api/productos/{id}/ventas  → GET ventas de un producto
/api/categorias             → GET, POST
/api/categorias/{id}        → GET, PUT, DELETE
```

---

## 10. CORS — Comunicación Frontend ↔ Backend

Cuando Angular (puerto 4200) pide datos a Spring Boot (puerto 8080), el navegador bloquea la petición por seguridad. Esto se llama **CORS** (Cross-Origin Resource Sharing).

```
Angular (localhost:4200)  →  "Quiero datos de localhost:8080"
Navegador: "¡BLOQUEADO! Son diferentes orígenes (puertos)"
```

**Solución** (en el backend Spring Boot):
```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
```

---

## Resumen visual

```
┌──────────────┐         HTTP          ┌──────────────┐
│   CLIENTE    │ ────────────────────→ │   SERVIDOR   │
│              │ ← Request (JSON)      │              │
│ - Navegador  │                       │ - Spring Boot│
│ - Postman    │ ← Response (JSON)     │ - Puerto 8080│
│ - Angular    │ ←────────────────────│              │
│ - curl       │         HTTP          │              │
└──────────────┘                       └──────────────┘

Request = Método + URL + Headers + Body
Response = Status Code + Headers + Body (JSON)
```

---

## Siguiente paso
Cuando entiendas GET/POST/PUT/DELETE, JSON y status codes → ve al **Nivel 1: API de Saludos** con confianza.
