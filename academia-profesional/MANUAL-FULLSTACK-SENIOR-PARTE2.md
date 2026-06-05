# 📘 Manual Full Stack Senior - Parte 2: Teoría Profunda

## Continuación del Manual Principal

---

# 18. Programación Orientada a Objetos (POO) - Teoría Profunda

## ¿Qué es POO?
Paradigma que modela el software como objetos del mundo real. Cada objeto tiene:
- **Estado** (atributos/datos): nombre, precio, stock
- **Comportamiento** (métodos): calcularIVA(), vender(), aplicarDescuento()

## Los 4 Pilares Explicados

### Encapsulamiento
```
¿Qué es? Ocultar los datos internos y exponer solo lo necesario.
¿Por qué? Proteger la integridad de los datos.

Ejemplo real: Una cuenta bancaria.
- NO puedes modificar el saldo directamente (saldo = 1000000)
- SÍ puedes depositar() o retirar() (con validaciones)

Sin encapsulamiento:
  cuenta.saldo = -5000;  // ¡Saldo negativo! Nadie lo impide.

Con encapsulamiento:
  cuenta.retirar(5000);  // Valida: ¿hay saldo suficiente? Si no → excepción.
```

### Herencia
```
¿Qué es? Una clase HIJA hereda atributos y métodos de una clase PADRE.
¿Por qué? Reutilizar código. No repetir lo mismo en cada clase.

Ejemplo real: Empleados de una empresa.
  Empleado (padre): nombre, salario, calcularSalario()
    ├── Vendedor (hijo): + comisión, calcularSalario() = base + comisión
    ├── Gerente (hijo): + bono, calcularSalario() = base + bono
    └── Director (hijo): + acciones, calcularSalario() = base + bono + acciones

Lo que se hereda: nombre, salario (no se repite en cada clase)
Lo que se sobreescribe: calcularSalario() (cada tipo calcula diferente)
```

### Polimorfismo
```
¿Qué es? Un mismo método se comporta DIFERENTE según el tipo de objeto.
¿Por qué? Tratar objetos diferentes de forma uniforme.

Ejemplo:
  List<Empleado> nomina = [vendedor, gerente, director];
  for (Empleado e : nomina) {
      System.out.println(e.calcularSalario());
      // Cada uno calcula diferente, pero el código es el MISMO
  }

Sin polimorfismo: necesitarías if/else para cada tipo (imposible de mantener).
Con polimorfismo: agregas un nuevo tipo (Freelancer) sin cambiar el código existente.
```

### Abstracción
```
¿Qué es? Definir QUÉ hace algo sin especificar CÓMO.
¿Por qué? Separar el contrato de la implementación.

Ejemplo: Interface "Pagable"
  interface Pagable {
      double calcularMonto();
      void procesarPago();
  }

  Cualquier clase que implemente Pagable DEBE tener esos métodos.
  No importa si es una Venta, una Factura, o una Suscripción.
  El código que usa Pagable no sabe (ni le importa) cuál es.
```

---

# 19. Patrones de Diseño - Teoría

## ¿Qué son?
Soluciones probadas a problemas recurrentes en diseño de software. No son código, son PLANTILLAS de solución.

## Los más usados en enterprise

### Singleton
```
¿Qué es? Una clase que solo puede tener UNA instancia en toda la aplicación.
¿Cuándo? Configuración global, conexión a BD, logger.
¿Cómo? Constructor privado + método estático getInstance().

Ejemplo real: La configuración de la app. No quieres 50 instancias
leyendo el archivo de config. Quieres UNA que todos compartan.

En Spring Boot: TODOS los @Service, @Repository, @Component son Singleton por defecto.
Spring los crea UNA vez y los inyecta donde se necesiten.
```

### Factory
```
¿Qué es? Un método que CREA objetos sin exponer la lógica de creación.
¿Cuándo? Cuando el tipo de objeto depende de un parámetro.

Ejemplo real: Sistema de notificaciones.
  NotificacionFactory.crear("EMAIL") → new EmailNotificacion()
  NotificacionFactory.crear("SMS")   → new SmsNotificacion()
  NotificacionFactory.crear("PUSH")  → new PushNotificacion()

El código que llama al factory NO sabe qué clase concreta se crea.
Si mañana agregas "WHATSAPP", solo cambias el factory (no todo el sistema).
```

### Strategy
```
¿Qué es? Definir una familia de algoritmos intercambiables.
¿Cuándo? Cuando el comportamiento cambia según el contexto.

Ejemplo real: Cálculo de descuentos.
  Cliente VIP     → 20% descuento
  Cliente Regular → 5% descuento
  Black Friday    → 30% descuento
  Sin descuento   → 0%

Sin Strategy: if/else gigante que crece con cada nuevo tipo.
Con Strategy: cada descuento es una clase. Se inyecta la que corresponda.
```

### Observer
```
¿Qué es? Cuando un objeto cambia, NOTIFICA automáticamente a todos los interesados.
¿Cuándo? Eventos, notificaciones, actualizaciones en tiempo real.

Ejemplo real: Sistema de stock.
  Cuando stock < mínimo:
    → Notificar al gerente (email)
    → Actualizar dashboard (websocket)
    → Crear orden de compra automática
    → Registrar en auditoría

Sin Observer: el servicio de inventario conoce TODOS los sistemas que debe notificar.
Con Observer: los interesados se "suscriben" y reciben la notificación automáticamente.
```

### Builder
```
¿Qué es? Construir objetos complejos paso a paso.
¿Cuándo? Objetos con muchos parámetros opcionales.

Sin Builder:
  new Producto("Laptop", "HP ProBook", 18999, 12000, 25, "LAP-001", 1, 1, true, null);
  // ¿Qué es cada parámetro? Imposible de leer.

Con Builder:
  Producto.builder()
    .nombre("Laptop")
    .precio(18999)
    .stock(25)
    .sku("LAP-001")
    .build();
  // Claro, legible, solo los campos que necesitas.
```

---

# 20. HTTP en Profundidad

## ¿Cómo funciona una petición HTTP?

```
PASO 1: El navegador construye la petición
┌─────────────────────────────────────────────────┐
│ POST /api/productos HTTP/1.1                     │ ← Método + Ruta + Versión
│ Host: api.empresa.com                            │ ← A qué servidor
│ Content-Type: application/json                   │ ← Formato del body
│ Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...   │ ← Token de autenticación
│ Content-Length: 85                                │ ← Tamaño del body
│                                                  │
│ {"nombre":"Laptop","precio":18999,"stock":25}    │ ← Body (datos)
└─────────────────────────────────────────────────┘

PASO 2: El servidor procesa y responde
┌─────────────────────────────────────────────────┐
│ HTTP/1.1 201 Created                             │ ← Status code
│ Content-Type: application/json                   │ ← Formato de respuesta
│ Location: /api/productos/501                     │ ← URL del recurso creado
│                                                  │
│ {"id":501,"nombre":"Laptop","precio":18999}      │ ← Body de respuesta
└─────────────────────────────────────────────────┘
```

## Métodos HTTP (verbos)
```
GET     → Obtener datos (no modifica nada, idempotente)
POST    → Crear recurso nuevo (no idempotente: cada llamada crea uno nuevo)
PUT     → Reemplazar recurso completo (idempotente)
PATCH   → Modificar parcialmente (solo los campos que envías)
DELETE  → Eliminar recurso (idempotente)
OPTIONS → Preguntar qué métodos soporta (usado por CORS)
HEAD    → Como GET pero sin body (solo headers)
```

## Idempotencia
```
Idempotente = llamar 1 vez o 100 veces produce el MISMO resultado.

GET /api/productos/5     → Siempre devuelve el mismo producto (idempotente)
DELETE /api/productos/5  → La primera vez lo borra, las siguientes no hacen nada (idempotente)
PUT /api/productos/5     → Siempre deja el recurso en el mismo estado (idempotente)
POST /api/productos      → Cada llamada CREA un producto nuevo (NO idempotente)
```

---

# 21. Concurrencia y Paralelismo

## ¿Cuál es la diferencia?
```
Concurrencia: múltiples tareas PROGRESAN en el mismo período
  (como un chef que cocina 3 platos alternando entre ellos)

Paralelismo: múltiples tareas se ejecutan AL MISMO TIEMPO
  (como 3 chefs, cada uno cocinando un plato)
```

## Problemas de concurrencia
```
Race Condition:
  Thread A lee stock = 5
  Thread B lee stock = 5
  Thread A vende 3 → stock = 2
  Thread B vende 4 → stock = 1  ← ¡ERROR! Debería fallar (solo había 5)

Solución: Locks, transacciones, optimistic locking
  SELECT stock FROM productos WHERE id = 1 FOR UPDATE;  -- Bloquea la fila
```

## Virtual Threads (Java 21+)
```
Antes: 1 request = 1 thread del SO (pesado, ~1MB RAM, máximo ~5000)
Ahora: 1 request = 1 virtual thread (ligero, ~1KB RAM, millones posibles)

Configuración en Spring Boot:
  spring.threads.virtual.enabled: true  ← UNA línea cambia todo
```

---

# 22. Testing - Teoría

## Pirámide de Testing
```
         /\
        /  \        E2E Tests (pocos, lentos, frágiles)
       /    \       Prueban el sistema COMPLETO (navegador real)
      /──────\
     /        \     Integration Tests (medios)
    /          \    Prueban componentes JUNTOS (API + BD)
   /────────────\
  /              \  Unit Tests (muchos, rápidos, estables)
 /                \ Prueban UNA clase aislada (con mocks)
/──────────────────\
```

## ¿Qué testear?
```
✅ Lógica de negocio (calcular descuento, validar stock)
✅ Casos borde (stock = 0, precio negativo, string vacío)
✅ Manejo de errores (qué pasa si la BD está caída)
✅ Seguridad (acceso sin token, rol incorrecto)

❌ Getters/setters (no tienen lógica)
❌ Código generado por frameworks
❌ Configuración trivial
```

## TDD (Test-Driven Development)
```
1. RED: Escribir el test ANTES del código (falla porque no existe)
2. GREEN: Escribir el MÍNIMO código para que pase
3. REFACTOR: Mejorar el código sin romper el test

¿Por qué? Garantiza que cada línea de código tiene un propósito (pasar un test).
```

---

# 23. Bases de Datos - Teoría Avanzada

## Normalización
```
¿Qué es? Organizar tablas para eliminar redundancia.

Sin normalizar (todo en 1 tabla):
  | venta_id | cliente_nombre | cliente_email | producto_nombre | producto_precio |
  Si el cliente cambia de email → hay que actualizar en TODAS sus ventas.

Normalizado (tablas separadas):
  clientes: id, nombre, email
  productos: id, nombre, precio
  ventas: id, cliente_id (FK), producto_id (FK)
  Si el email cambia → se actualiza en 1 solo lugar.
```

## Índices - Cómo funcionan internamente
```
Sin índice: PostgreSQL lee CADA fila de la tabla (Full Table Scan)
  10 millones de filas → lee las 10 millones → encuentra 5 resultados
  Tiempo: 30 segundos

Con índice B-Tree: estructura de árbol ordenada
  10 millones de filas → navega el árbol (log₂ de 10M = ~23 pasos) → encuentra 5
  Tiempo: 0.001 segundos

¿Cuándo NO crear índice?
  - Tablas pequeñas (< 1000 filas)
  - Columnas con pocos valores únicos (boolean: solo true/false)
  - Tablas con muchos INSERT (cada insert actualiza TODOS los índices)
```

## EXPLAIN ANALYZE
```sql
EXPLAIN ANALYZE SELECT * FROM ventas WHERE fecha > '2024-01-01';

-- Resultado:
-- Seq Scan on ventas (cost=0.00..500.00 rows=10000)  ← MALO: lee toda la tabla
--   Filter: (fecha > '2024-01-01')
--   Rows Removed by Filter: 5000
-- Execution Time: 150.000 ms

-- Después de crear índice:
CREATE INDEX idx_ventas_fecha ON ventas(fecha);

-- Seq Scan → Index Scan (cost=0.00..8.50 rows=5000)  ← BUENO: usa índice
-- Execution Time: 2.000 ms  ← 75x más rápido
```

---

# 24. Redes y Protocolos

## Modelo OSI (simplificado)
```
7. Aplicación   → HTTP, HTTPS, WebSocket, gRPC
4. Transporte   → TCP (confiable, ordenado) / UDP (rápido, sin garantía)
3. Red          → IP (direccionamiento)
2. Enlace       → Ethernet, WiFi
1. Física       → Cables, señales eléctricas
```

## TCP vs UDP
```
TCP (Transmission Control Protocol):
  - Garantiza que los datos llegan COMPLETOS y EN ORDEN
  - Si se pierde un paquete → lo reenvía
  - Más lento pero confiable
  - Uso: HTTP, email, transferencias de archivos

UDP (User Datagram Protocol):
  - NO garantiza entrega ni orden
  - Si se pierde un paquete → se pierde
  - Más rápido
  - Uso: video streaming, gaming, DNS
```

## WebSocket vs HTTP
```
HTTP: Request → Response (el servidor NO puede enviar datos sin que el cliente pregunte)
  Cliente: "¿Hay mensajes nuevos?" (cada 5 segundos)
  Servidor: "No" / "Sí, aquí están"
  Problema: muchas peticiones innecesarias (polling)

WebSocket: Conexión PERMANENTE bidireccional
  Cliente y servidor pueden enviarse datos EN CUALQUIER MOMENTO
  Uso: chat en tiempo real, notificaciones, dashboards live, gaming
```

---

# 25. Cloud Computing

## Modelos de servicio
```
IaaS (Infrastructure as a Service):
  Te dan: máquinas virtuales, redes, almacenamiento
  Tú manejas: SO, runtime, app, datos
  Ejemplo: AWS EC2, Azure VMs, Google Compute Engine

PaaS (Platform as a Service):
  Te dan: runtime, SO, escalado automático
  Tú manejas: código y datos
  Ejemplo: Heroku, Google App Engine, Azure App Service

SaaS (Software as a Service):
  Te dan: la aplicación completa
  Tú manejas: solo tus datos
  Ejemplo: Gmail, Slack, Salesforce

Serverless (FaaS):
  Te dan: ejecutar funciones individuales
  Tú manejas: solo el código de la función
  Ejemplo: AWS Lambda, Azure Functions, Google Cloud Functions
  Pagas SOLO por ejecución (no por servidor encendido)
```

## Servicios AWS más usados
```
EC2          → Máquinas virtuales
S3           → Almacenamiento de archivos (imágenes, backups)
RDS          → Base de datos managed (PostgreSQL, MySQL)
ElastiCache  → Redis/Memcached managed
EKS          → Kubernetes managed
Lambda       → Funciones serverless
SQS/SNS      → Colas de mensajes
CloudFront   → CDN (contenido cerca del usuario)
Route 53     → DNS
IAM          → Usuarios y permisos
```

---

# 26. Principios de Diseño de Software

## KISS (Keep It Simple, Stupid)
```
La solución más simple que funciona es la MEJOR.
No agregar complejidad "por si acaso" o "para el futuro".
Si no la necesitas HOY, no la construyas.
```

## YAGNI (You Aren't Gonna Need It)
```
No implementes funcionalidad que CREES que necesitarás en el futuro.
El 80% de las features "para el futuro" nunca se usan.
Implementa cuando la necesites, no antes.
```

## DRY (Don't Repeat Yourself)
```
Si copias/pegas código → algo está mal.
Extraer a un método, clase, o servicio reutilizable.
PERO: no sobre-abstraer. Si 2 cosas se parecen pero tienen razones
diferentes para cambiar → está bien que estén separadas.
```

## Separation of Concerns
```
Cada módulo/clase/función tiene UNA responsabilidad.
  Controller → recibir HTTP, validar formato
  Service → lógica de negocio
  Repository → acceso a datos
  Entity → representar datos

Si un cambio en la BD requiere cambiar el Controller → algo está mal.
```

---

# 27. System Design (Diseño de Sistemas)

## ¿Cómo diseñar un sistema que soporte millones de usuarios?

### Paso 1: Requisitos
```
¿Cuántos usuarios? ¿Cuántas peticiones/segundo?
¿Qué es más importante: consistencia o disponibilidad?
¿Qué datos son críticos? ¿Qué se puede perder?
```

### Paso 2: Componentes típicos
```
Load Balancer → distribuye tráfico entre servidores
App Servers (múltiples) → procesan peticiones
Cache (Redis) → datos frecuentes en memoria
Database (Primary + Replicas) → escritura en primary, lectura en replicas
Message Queue (Kafka) → procesar tareas async
CDN → archivos estáticos cerca del usuario
Object Storage (S3) → imágenes, videos, archivos
Search Engine (Elasticsearch) → búsqueda full-text
```

### Paso 3: Escalamiento
```
Vertical: máquina más grande (más RAM, más CPU)
  ✅ Simple
  ❌ Tiene límite físico, punto único de falla

Horizontal: más máquinas (réplicas)
  ✅ Sin límite teórico, tolerante a fallos
  ❌ Más complejo (sincronización, consistencia)
```

### Ejemplo: Diseñar un sistema de e-commerce
```
                    ┌─────────────┐
                    │     CDN     │ (imágenes de productos)
                    └──────┬──────┘
                           │
                    ┌──────┴──────┐
                    │Load Balancer│
                    └──────┬──────┘
              ┌────────────┼────────────┐
              ▼            ▼            ▼
         ┌────────┐  ┌────────┐  ┌────────┐
         │ App 1  │  │ App 2  │  │ App 3  │  (auto-scaling)
         └───┬────┘  └───┬────┘  └───┬────┘
             │            │            │
        ┌────┴────────────┴────────────┴────┐
        │              Redis Cache           │ (productos populares)
        └────────────────┬──────────────────┘
                         │
              ┌──────────┴──────────┐
              ▼                     ▼
    ┌──────────────┐      ┌──────────────┐
    │ PostgreSQL   │      │ PostgreSQL   │
    │  (Primary)   │─────→│  (Replica)   │  (lectura)
    │  (escritura) │      │  (lectura)   │
    └──────────────┘      └──────────────┘
              │
              ▼
    ┌──────────────┐
    │    Kafka     │ → Procesar pedidos async
    └──────────────┘   (email, inventario, facturación)
```

---

# 28. Soft Skills para Senior

## Lo que NO es técnico pero es ESENCIAL

### Comunicación
```
- Explicar decisiones técnicas a personas no-técnicas
- Documentar para que otros entiendan tu código
- Dar feedback constructivo en code reviews
- Saber decir "no sé" y buscar la respuesta
```

### Mentoría
```
- Ayudar a juniors sin hacer su trabajo por ellos
- Hacer preguntas que guíen al pensamiento correcto
- Compartir contexto y razones detrás de decisiones
- Crear documentación que reduzca preguntas repetitivas
```

### Estimación
```
- Descomponer tareas grandes en pequeñas
- Agregar buffer para lo inesperado (bugs, reuniones, bloqueos)
- Comunicar riesgos temprano (no al final)
- "Esto tomará 3 días" es mejor que "no sé"
```

### Toma de decisiones
```
- Evaluar trade-offs (no hay solución perfecta)
- Documentar POR QUÉ se eligió una opción
- Considerar: mantenibilidad, costo, tiempo, equipo
- "La mejor arquitectura es la que tu equipo puede mantener"
```
