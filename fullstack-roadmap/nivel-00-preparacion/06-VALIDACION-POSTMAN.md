# Validación con Postman — Cómo probar que tu API funciona

## ¿Qué es Postman?
Postman es una herramienta para ENVIAR peticiones HTTP a tu API y ver las respuestas. Es como un navegador especializado para APIs.

---

## 1. Crear una Colección

Una colección agrupa todas las peticiones de un proyecto:

```
📁 Proyecto 3: API Productos
├── 📁 Categorías
│   ├── GET Listar categorías
│   ├── GET Buscar por ID
│   ├── POST Crear categoría
│   ├── PUT Actualizar categoría
│   └── DELETE Eliminar categoría
├── 📁 Productos
│   ├── GET Listar productos
│   ├── GET Buscar por ID
│   ├── GET Buscar por nombre
│   ├── GET Filtrar por categoría
│   ├── GET Stock bajo
│   ├── GET Paginado
│   ├── POST Crear producto
│   ├── PUT Actualizar producto
│   └── DELETE Eliminar producto
└── 📁 Auth (nivel 5+)
    ├── POST Login
    ├── POST Refresh token
    └── GET Mi perfil
```

---

## 2. Variables de Entorno

Configura variables para no repetir URLs ni tokens:

### Crear ambiente "Local"
```
Variable        | Valor
base_url        | http://localhost:8080
token           | (se llena automáticamente al hacer login)
refresh_token   | (se llena automáticamente)
producto_id     | (se llena al crear un producto)
```

### Usar variables en las peticiones
```
URL: {{base_url}}/api/productos
Header: Authorization: Bearer {{token}}
```

---

## 3. Peticiones — Ejemplos Completos

### GET — Listar todos los productos
```
Método: GET
URL: {{base_url}}/api/productos
Headers: (ninguno si es público)

Response esperada (200 OK):
{
  "success": true,
  "data": [
    {
      "id": 1,
      "nombre": "Laptop HP ProBook",
      "precio": 18999.00,
      "existencia": 25,
      "categoriaId": 1,
      "categoriaNombre": "Electrónica"
    }
  ],
  "total": 15
}
```

### GET — Buscar por ID
```
Método: GET
URL: {{base_url}}/api/productos/1

Response (200 OK):
{
  "success": true,
  "data": {
    "id": 1,
    "nombre": "Laptop HP ProBook",
    "precio": 18999.00
  }
}

Response (404 Not Found - ID no existe):
{
  "success": false,
  "error": "Producto no encontrado con ID: 999"
}
```

### GET — Buscar por nombre
```
Método: GET
URL: {{base_url}}/api/productos/buscar?nombre=laptop

Response (200 OK):
{
  "success": true,
  "data": [
    {"id": 1, "nombre": "Laptop HP ProBook", ...},
    {"id": 3, "nombre": "MacBook Air M3", ...}
  ],
  "total": 2
}
```

### GET — Paginación
```
Método: GET
URL: {{base_url}}/api/productos/paginado?page=0&size=5

Response (200 OK):
{
  "content": [...],
  "page": 0,
  "size": 5,
  "totalElements": 15,
  "totalPages": 3
}
```

### POST — Crear producto
```
Método: POST
URL: {{base_url}}/api/productos
Headers:
  Content-Type: application/json
  Authorization: Bearer {{token}}
Body (raw JSON):
{
  "nombre": "Webcam Logitech",
  "descripcion": "Cámara 1080p para reuniones",
  "precio": 1899.00,
  "existencia": 20,
  "categoriaId": 2,
  "sku": "CAM-LOG-001"
}

Response (201 Created):
{
  "success": true,
  "message": "Producto creado",
  "data": {
    "id": 16,
    "nombre": "Webcam Logitech",
    ...
  }
}
```

### PUT — Actualizar producto
```
Método: PUT
URL: {{base_url}}/api/productos/16
Headers:
  Content-Type: application/json
  Authorization: Bearer {{token}}
Body (raw JSON):
{
  "nombre": "Webcam Logitech Pro",
  "precio": 2199.00,
  "existencia": 18,
  "categoriaId": 2,
  "sku": "CAM-LOG-001"
}

Response (200 OK):
{
  "success": true,
  "message": "Producto actualizado",
  "data": { ... }
}
```

### DELETE — Eliminar producto
```
Método: DELETE
URL: {{base_url}}/api/productos/16
Headers:
  Authorization: Bearer {{token}}

Response (200 OK):
{
  "success": true,
  "message": "Producto eliminado"
}
```

---

## 4. Autenticación — Login y Token

### POST — Login
```
Método: POST
URL: {{base_url}}/api/auth/login
Headers:
  Content-Type: application/json
Body:
{
  "username": "admin",
  "password": "Admin123!"
}

Response (200 OK):
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "admin",
  "roles": ["ROLE_ADMIN"]
}
```

### Guardar token automáticamente (Script en Postman)
En la pestaña "Scripts" → "Post-response" del request de Login:
```javascript
// Guardar token en la variable de entorno
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    pm.environment.set("token", jsonData.accessToken);
    pm.environment.set("refresh_token", jsonData.refreshToken);
    console.log("Token guardado exitosamente");
}
```

Ahora todas las demás peticiones usan `{{token}}` automáticamente.

---

## 5. Tests Automáticos en Postman

En cada petición, pestaña "Scripts" → "Post-response":

### Test: Status code correcto
```javascript
pm.test("Status code es 200", function () {
    pm.response.to.have.status(200);
});
```

### Test: Respuesta tiene formato correcto
```javascript
pm.test("Respuesta tiene success = true", function () {
    var json = pm.response.json();
    pm.expect(json.success).to.be.true;
});

pm.test("Data es un array", function () {
    var json = pm.response.json();
    pm.expect(json.data).to.be.an("array");
});

pm.test("Tiene al menos 1 producto", function () {
    var json = pm.response.json();
    pm.expect(json.data.length).to.be.above(0);
});
```

### Test: Validar estructura del producto
```javascript
pm.test("Producto tiene campos requeridos", function () {
    var json = pm.response.json();
    var producto = json.data[0];
    pm.expect(producto).to.have.property("id");
    pm.expect(producto).to.have.property("nombre");
    pm.expect(producto).to.have.property("precio");
    pm.expect(producto.precio).to.be.a("number");
    pm.expect(producto.precio).to.be.above(0);
});
```

### Test: Crear producto devuelve 201
```javascript
pm.test("Status 201 Created", function () {
    pm.response.to.have.status(201);
});

pm.test("Producto creado tiene ID", function () {
    var json = pm.response.json();
    pm.expect(json.data.id).to.be.a("number");
    // Guardar ID para usar en PUT/DELETE después
    pm.environment.set("producto_id", json.data.id);
});
```

### Test: Sin token devuelve 401
```javascript
pm.test("Sin token devuelve 401", function () {
    pm.response.to.have.status(401);
});
```

### Test: Tiempo de respuesta aceptable
```javascript
pm.test("Respuesta en menos de 500ms", function () {
    pm.expect(pm.response.responseTime).to.be.below(500);
});
```

---

## 6. Ejecutar Colección Completa (Runner)

1. Click derecho en la colección → "Run collection"
2. Selecciona el ambiente (Local)
3. Click "Run"
4. Postman ejecuta TODAS las peticiones en orden
5. Muestra resultados: ✅ pasó / ❌ falló

### Orden recomendado para el Runner:
```
1. POST Login (obtiene token)
2. GET Listar productos (verifica conexión)
3. POST Crear producto (obtiene producto_id)
4. GET Buscar por ID (usa producto_id)
5. PUT Actualizar producto (usa producto_id)
6. DELETE Eliminar producto (usa producto_id)
7. GET Verificar que ya no existe (404)
```

---

## 7. Exportar/Importar Colección

### Exportar (compartir con el equipo):
```
Collection → ... → Export → Collection v2.1 → Save
```
Genera un archivo `.json` que cualquiera puede importar.

### Importar:
```
Import → Upload files → Seleccionar el .json
```

---

## 8. Evidencias para Documentación

Postman puede generar evidencias:
- **Screenshots**: captura de pantalla de cada respuesta
- **Export Run Results**: resultado del Runner como HTML
- **Console log**: ver los logs de tus scripts

### Estructura de evidencias:
```
docs/pruebas/
├── postman/
│   ├── coleccion-productos-v1.json   ← Exportar la colección
│   ├── evidencia-01-listar.png       ← Screenshot
│   ├── evidencia-02-crear.png
│   ├── evidencia-03-actualizar.png
│   ├── evidencia-04-eliminar.png
│   ├── evidencia-05-login.png
│   ├── evidencia-06-sin-token-401.png
│   └── resultado-runner.png          ← Todas las pruebas en verde
```

---

## Siguiente paso
Con Postman dominado, puedes validar CUALQUIER API que construyas en el roadmap.
