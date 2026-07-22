# Preguntas de Entrevista - Tema: Microservicios

## Nivel Junior

### 1. ¿Qué es un microservicio?
**Respuesta:**
Un servicio pequeño, independiente, que hace UNA cosa bien:
- Su propia base de datos (no comparte BD con otros)
- Se despliega independientemente
- Se comunica con otros via HTTP/mensajería
- Puede estar en diferente lenguaje/tecnología

Monolito: 1 app con todo. Microservicios: N apps pequeñas que colaboran.

---

### 2. ¿Cuándo usar microservicios vs monolito?
**Respuesta:**
**Monolito cuando:**
- Equipo pequeño (< 5 devs)
- Dominio simple
- MVP/startup (velocidad > arquitectura)

**Microservicios cuando:**
- Equipos grandes independientes
- Necesitas escalar partes del sistema por separado
- Alta disponibilidad (un servicio caído no tumba todo)
- Dominios de negocio muy distintos

---

### 3. ¿Qué es un API Gateway?
**Respuesta:**
Punto de entrada único que rutea requests al servicio correcto:
```
Cliente → Gateway (:8080) → /api/productos/** → productos-service
                          → /api/usuarios/**  → usuarios-service
                          → /api/ventas/**    → ventas-service
```
También puede: autenticación, rate limiting, logging, load balancing, CORS.

---

### 4. ¿Por qué cada microservicio tiene su propia BD?
**Respuesta:**
Para ser verdaderamente independiente:
- Si comparten BD → cambiar una tabla puede romper otro servicio
- Si comparten BD → no puedes desplegar uno sin el otro
- Si comparten BD → un query lento de un servicio bloquea al otro

Cada servicio es dueño de sus datos. Si necesita datos de otro → le pregunta via API.

---

### 5. ¿Cómo se comunican los microservicios?
**Respuesta:**
- **Síncrono (HTTP/gRPC)**: Service A llama a Service B y ESPERA respuesta
- **Asíncrono (Kafka/RabbitMQ)**: Service A publica evento, Service B lo consume cuando pueda

Síncrono: simple, pero crea acoplamiento temporal (si B está caído, A falla).
Asíncrono: más resiliente, pero más complejo (eventual consistency).

---

## Nivel Mid

### 6. ¿Qué es el Circuit Breaker pattern?
**Respuesta:**
Previene llamadas a un servicio que está fallando (como un fusible eléctrico):
- **Closed** (normal): requests pasan normalmente
- **Open** (servicio caído): requests fallan inmediatamente (sin esperar timeout)
- **Half-Open** (probando): deja pasar algunos requests para verificar recuperación

Evita: cascada de fallos, saturar un servicio enfermo, timeouts largos.

---

### 7. ¿Qué es eventual consistency y por qué es necesaria?
**Respuesta:**
En microservicios NO puedes tener transacciones ACID entre servicios (no hay una BD compartida).

Eventual consistency: los datos serán consistentes EVENTUALMENTE (no inmediatamente):
```
1. ventas-service crea venta ✓
2. Publica evento "VentaCreada" ✓
3. productos-service recibe evento (puede ser 100ms después)
4. Descuenta stock ✓
→ Por 100ms, la venta existe pero el stock no se descontó (inconsistencia temporal)
```

Trade-off: aceptas inconsistencia temporal a cambio de disponibilidad y desacoplamiento.

---

### 8. ¿Qué es Service Discovery?
**Respuesta:**
Los servicios se registran en un registry central (Eureka, Consul). Otros servicios los descubren por NOMBRE, no por IP:
```
productos-service se registra: "soy productos, estoy en 10.0.1.5:8082"
ventas-service pregunta: "¿dónde está productos?" → "en 10.0.1.5:8082"
```
Si productos se mueve a otra IP o escala a 3 instancias → el discovery lo sabe automáticamente.

---

### 9. ¿Cómo manejas transacciones distribuidas?
**Respuesta:**
Patrón **Saga**: secuencia de transacciones locales con compensaciones:
```
1. Crear pedido (pedidos-service) ← si falla en paso 3: cancelar pedido
2. Reservar stock (inventario-service) ← si falla en paso 3: devolver stock
3. Cobrar pago (pagos-service)
```
Si un paso falla → ejecutar compensaciones en orden inverso.

Tipos: Choreography (eventos) vs Orchestration (coordinador central).

---

### 10. ¿Qué problemas trae la arquitectura de microservicios?
**Respuesta:**
- **Complejidad operacional**: N servicios = N deploys, N logs, N monitores
- **Latencia de red**: llamadas HTTP entre servicios (vs llamada directa en monolito)
- **Consistencia eventual**: transacciones distribuidas son complejas
- **Testing**: tests de integración entre servicios son difíciles
- **Debugging**: un request toca 5 servicios (necesitas tracing distribuido)
- **Data management**: datos particionados, joins imposibles entre servicios

---

## Nivel Senior

### 11. ¿Cómo diseñarías la migración de un monolito a microservicios?
**Respuesta (Strangler Fig Pattern):**
1. Identificar bounded contexts (DDD) en el monolito
2. Extraer UN módulo como servicio (el más independiente)
3. Redirigir tráfico: Gateway envía requests al nuevo servicio
4. El monolito y el servicio coexisten
5. Repetir hasta vaciar el monolito

NUNCA reescribir todo de golpe. Migración incremental, módulo por módulo.

---

### 12. ¿Qué patrones de observabilidad son esenciales en microservicios?
**Respuesta:**
Los 3 pilares:
1. **Logs** (qué pasó): structured logging (JSON), correlación con traceId
2. **Metrics** (cuánto): latencia p95, error rate, throughput por servicio
3. **Traces** (dónde): seguir un request a través de múltiples servicios (Zipkin/Jaeger)

Sin estos 3 → debugging en microservicios es imposible.
Tools: OpenTelemetry (estándar), Prometheus (metrics), Grafana (visualización), ELK/Loki (logs).
