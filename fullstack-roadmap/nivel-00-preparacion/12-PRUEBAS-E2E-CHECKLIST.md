# Pruebas End-to-End y Checklist de Validación

## ¿Qué es una prueba E2E?
Una prueba End-to-End valida un FLUJO COMPLETO desde la perspectiva del usuario:
- Abrir la app → login → navegar → crear producto → ver en la lista → editar → eliminar

No es un test unitario (una función aislada). Es el flujo REAL.

---

## 1. Flujos E2E que debes validar por proyecto

### Nivel 3: API de Productos
```
Flujo 1: CRUD completo de producto
1. GET /api/productos → lista vacía o con datos iniciales ✅
2. POST /api/productos → crear producto → 201 + ID ✅
3. GET /api/productos/{id} → verificar que existe ✅
4. PUT /api/productos/{id} → actualizar nombre/precio ✅
5. GET /api/productos/{id} → verificar que cambió ✅
6. DELETE /api/productos/{id} → soft delete ✅
7. GET /api/productos/{id} → sigue existiendo pero activo=false ✅

Flujo 2: Búsquedas
1. GET /api/productos/buscar?nombre=laptop → encuentra ✅
2. GET /api/productos/buscar?nombre=xyz → lista vacía ✅
3. GET /api/productos/categoria/1 → solo de esa categoría ✅
4. GET /api/productos/paginado?page=0&size=5 → 5 resultados ✅
5. GET /api/productos/paginado?page=99&size=5 → lista vacía ✅
```

### Nivel 5: Seguridad
```
Flujo 1: Login exitoso
1. POST /api/auth/login → token ✅
2. GET /api/auth/me con token → datos del usuario ✅
3. Usar token en otros endpoints → acceso ✅

Flujo 2: Login fallido
1. POST /api/auth/login con password incorrecto → 401 ✅
2. Intentar 5 veces → cuenta bloqueada ✅
3. Esperar 30 min → cuenta desbloqueada ✅

Flujo 3: Refresh token
1. POST /api/auth/login → accessToken + refreshToken ✅
2. Esperar que expire el accessToken ✅
3. POST /api/auth/refresh con refreshToken → nuevo accessToken ✅
4. Usar nuevo accessToken → funciona ✅
```

### Nivel 10: Full Stack (Angular + Backend)
```
Flujo 1: Login visual
1. Abrir http://localhost:4200 → redirige a /login ✅
2. Ingresar credenciales → clic "Ingresar" ✅
3. Redirige a /dashboard ✅
4. Ver nombre de usuario en la barra ✅

Flujo 2: CRUD visual
1. Navegar a /productos ✅
2. Ver tabla con productos ✅
3. Clic "Nuevo" → formulario ✅
4. Llenar datos → clic "Guardar" ✅
5. Ver toast "Producto creado" ✅
6. Ver producto en la tabla ✅
7. Clic "Editar" → formulario pre-llenado ✅
8. Modificar precio → "Guardar" ✅
9. Clic "Eliminar" → modal de confirmación ✅
10. Confirmar → producto desaparece de la tabla ✅

Flujo 3: Validaciones visuales
1. Formulario vacío → clic "Guardar" ✅
2. Ver mensajes de error en rojo bajo cada campo ✅
3. Botón deshabilitado hasta que sea válido ✅
4. Ingresar precio negativo → "El precio debe ser mayor a 0" ✅
```

---

## 2. Checklist de validación por nivel

### Nivel 1: Backend Básico
```
[ ] mvn spring-boot:run arranca sin errores
[ ] GET /api/saludos devuelve texto
[ ] GET /api/saludos/Carlos devuelve saludo personalizado
[ ] GET /api/saludos/json/Maria devuelve JSON válido
[ ] GET /api/saludos/calculadora/sumar?a=10&b=20 devuelve 30
[ ] GET /api/saludos/hora devuelve fecha actual
```

### Nivel 2: CRUD en Memoria
```
[ ] POST /api/tareas → crea tarea (201)
[ ] GET /api/tareas → lista todas
[ ] GET /api/tareas/{id} → una tarea
[ ] PUT /api/tareas/{id} → actualiza
[ ] DELETE /api/tareas/{id} → elimina (204)
[ ] PATCH /api/tareas/{id}/completar → marca completada
[ ] POST con body vacío → error 400 con detalles
[ ] GET /api/tareas/999 → 404 con mensaje
```

### Nivel 3: Base de Datos
```
[ ] Docker MySQL corriendo (o instalación local)
[ ] mvn spring-boot:run → Flyway crea tablas automáticamente
[ ] V1__crear_tablas.sql se ejecutó (ver en DBeaver)
[ ] V2__datos_iniciales.sql insertó 15 productos y 5 categorías
[ ] GET /api/productos → 15 productos con categoriaNombre
[ ] GET /api/categorias → 5 categorías
[ ] POST /api/productos → se persiste en MySQL (verificar con DBeaver)
[ ] DELETE /api/productos/1 → activo=false en BD (no se borra)
[ ] Reiniciar app → datos siguen ahí (persistencia real)
```

### Nivel 4: Tests
```
[ ] mvn test → todos los tests pasan (0 failures)
[ ] ProductoServiceTest: 12 tests en verde
[ ] ProductoControllerTest: 10 tests en verde
[ ] JaCoCo genera reporte (target/site/jacoco/index.html)
[ ] Cobertura ≥ 70%
```

### Nivel 5: Seguridad
```
[ ] POST /api/auth/login con credenciales correctas → token
[ ] POST /api/auth/login con credenciales incorrectas → 401
[ ] GET /api/auth/me con token → datos del usuario
[ ] GET /api/auth/me sin token → 401
[ ] Token expirado → 401
[ ] Password en BD está hasheado (BCrypt)
[ ] POST /api/auth/refresh → nuevo accessToken
```

### Nivel 8: Angular
```
[ ] ng serve arranca sin errores
[ ] http://localhost:4200 muestra la app
[ ] Navegación entre mini-apps funciona
[ ] Contador: incrementar/decrementar/reset
[ ] Tareas: agregar, completar, eliminar
[ ] Calculadora: operaciones básicas
[ ] Formulario: validaciones en tiempo real
```

### Nivel 11-12: Sistema completo
```
[ ] docker-compose up -d levanta todo
[ ] Login funciona
[ ] CRUD productos completo
[ ] Transacción de compra (cabecera + detalle + stock)
[ ] Reporte de existencias
[ ] Auditoría registra acciones
[ ] Roles limitan acceso correctamente
[ ] Paginación server-side funciona
```

---

## 3. Cómo generar evidencias

### Screenshots (manuales)
```
Para cada flujo:
1. Abrir la herramienta (Postman/Navegador/DBeaver)
2. Ejecutar el paso
3. Captura de pantalla (Win+Shift+S)
4. Guardar como: evidencia-XX-descripcion.png
```

### Video corto (opcional pero impresiona)
```
1. OBS Studio o grabador de Windows (Win+G)
2. Grabar el flujo completo (30-60 segundos)
3. Guardar como: demo-crud-productos.mp4
```

### Exportar resultado del Runner de Postman
```
1. Correr la colección completa
2. Screenshot del resultado (todo en verde)
3. Guardar como: runner-resultado-nivel-03.png
```

---

## 4. Estructura de evidencias por proyecto

```
nivel-XX/
├── proyecto/
│   └── ...
├── evidencias/
│   ├── 01-app-arranca.png
│   ├── 02-flyway-ejecutado.png
│   ├── 03-listar-productos.png
│   ├── 04-crear-producto.png
│   ├── 05-buscar-nombre.png
│   ├── 06-paginacion.png
│   ├── 07-error-validacion-400.png
│   ├── 08-error-no-encontrado-404.png
│   ├── 09-login-exitoso.png
│   ├── 10-sin-token-401.png
│   ├── 11-bd-datos-persistidos.png
│   ├── 12-runner-postman.png
│   └── 13-cobertura-jacoco.png
└── postman/
    └── coleccion-nivel-XX.json
```

---

## 5. Validar en la UI (Angular)

### Qué verificar en el navegador
```
[ ] La app carga sin errores en consola (F12 → Console)
[ ] Las rutas navegan correctamente
[ ] Los formularios muestran errores de validación
[ ] Los botones se deshabilitan cuando el form es inválido
[ ] Los mensajes toast aparecen y desaparecen
[ ] La tabla carga datos del backend
[ ] La paginación funciona (cambiar de página)
[ ] El buscador filtra resultados
[ ] El login redirige al dashboard
[ ] Sin sesión redirige al login
[ ] Los botones de acciones se ocultan según el rol
```

### Herramientas del navegador (F12)
```
- Console: ver errores JavaScript
- Network: ver peticiones HTTP (status, payload, response)
- Application → LocalStorage: ver token guardado
```

---

## 6. Validar la base de datos (DBeaver)

### Qué verificar directamente en BD
```
[ ] Tablas creadas por Flyway (ver flyway_schema_history)
[ ] Datos de prueba insertados
[ ] Después de POST → nueva fila en la tabla
[ ] Después de DELETE → activo = false (no se borró la fila)
[ ] Password hasheado (empieza con $2a$10$)
[ ] Timestamps se generan automáticamente
[ ] Foreign keys funcionan (no puedes insertar categoria_id inválido)
[ ] Índices creados (SHOW INDEX FROM productos)
```

---

## Siguiente paso
Usa este checklist cada vez que termines un nivel. Si todo pasa → avanza al siguiente con confianza.
