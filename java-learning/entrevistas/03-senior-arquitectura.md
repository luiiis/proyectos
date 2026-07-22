# Entrevista Técnica Java - Nivel Senior (Arquitectura + System Design)

## 1. ¿Cómo diseñarías un sistema de e-commerce para 10K req/segundo?
**Respuesta:**
```
                    CDN (frontend)
                        │
                    Load Balancer
                   ┌────┴────┐
                   │ API GW  │ (rate limiting, auth)
              ┌────┴────┬────┴────┐
              ▼         ▼         ▼
          Users SVC  Products  Orders SVC
              │         │         │
              ▼         ▼         ▼
           PostgreSQL  Redis    PostgreSQL
                     (cache)      │
                                  ▼
                               Kafka
                              (eventos)
                                  │
                         ┌────────┴────────┐
                         ▼                 ▼
                   Notifications      Inventory
```

Decisiones clave:
- **Cache Redis**: productos y catálogo (lectura 90% del tráfico)
- **CQRS**: separar lectura de escritura (read replicas)
- **Event-driven**: crear pedido → evento → actualizar inventario async
- **API Gateway**: rate limiting, JWT validation, routing
- **Auto-scaling**: K8s HPA basado en CPU/requests

---

## 2. ¿Qué es Hexagonal Architecture y cómo la implementas en Spring Boot?
**Respuesta:**
Separar la lógica de negocio de la infraestructura mediante PUERTOS (interfaces) y ADAPTADORES (implementaciones).

```
src/main/java/com/empresa/
├── domain/                    ← NÚCLEO (sin dependencias Spring)
│   ├── model/Producto.java
│   ├── port/in/CrearProductoUseCase.java     ← Puerto entrada (interface)
│   ├── port/out/ProductoRepository.java      ← Puerto salida (interface)
│   └── service/ProductoService.java          ← Lógica (implementa UseCases)
│
├── infrastructure/            ← ADAPTADORES (implementan puertos)
│   ├── adapter/in/web/ProductoController.java    ← Adaptador REST
│   ├── adapter/out/persistence/ProductoJpaRepo.java  ← Adaptador BD
│   └── config/BeanConfig.java
│
└── application/               ← Composición (wiring)
    └── Application.java
```

Beneficios: testear dominio SIN Spring, cambiar BD sin tocar lógica, independencia de frameworks.

---

## 3. ¿Cómo manejarías una migración de monolito a microservicios?
**Respuesta:**
**Strangler Fig Pattern** (migración incremental):

1. Identificar **Bounded Contexts** (DDD): usuarios, productos, pedidos, pagos
2. Extraer el módulo MÁS independiente primero (ej: notificaciones)
3. Coexistencia: API Gateway rutea tráfico viejo al monolito, nuevo al microservicio
4. Duplicar datos necesarios (evento o CDC - Change Data Capture)
5. Repetir hasta que el monolito esté vacío

NUNCA reescribir de golpe ("Big Bang rewrite" = riesgo máximo).

---

## 4. ¿Cómo implementas Event Sourcing + CQRS?
**Respuesta:**
```java
// En vez de guardar ESTADO actual:
// productos.stock = 22

// Guardas TODOS LOS EVENTOS:
// StockInicial(25) → VentaRealizada(-3) → CompraRecibida(+10) → VentaRealizada(-10)
// Estado actual: 25 - 3 + 10 - 10 = 22

// WRITE side (Event Store):
@Service
public class PedidoCommandHandler {
    public void handle(CrearPedidoCommand cmd) {
        var event = new PedidoCreadoEvent(cmd.clienteId(), cmd.items(), Instant.now());
        eventStore.save(event);
        kafkaTemplate.send("pedidos", event);  // Publicar para proyecciones
    }
}

// READ side (Projection):
@KafkaListener(topics = "pedidos")
public void onPedidoCreado(PedidoCreadoEvent event) {
    // Actualizar vista materializada optimizada para lectura
    pedidoReadRepo.save(new PedidoView(event));
}
```

Cuándo usarlo: auditoría obligatoria, necesitas "time travel", alta escala.
Cuándo NO: CRUD simples, equipos pequeños.

---

## 5. ¿Cómo manejas la consistencia eventual entre microservicios?
**Respuesta:**
Con el patrón **Saga** (secuencia de transacciones locales con compensaciones):

```
1. OrderService: crear pedido (PENDING) ✓
2. PaymentService: cobrar pago ✓
3. InventoryService: reservar stock ← FALLA
4. Compensación: PaymentService → reembolsar ✓
5. Compensación: OrderService → cancelar pedido ✓
```

Tipos:
- **Choreography**: cada servicio escucha eventos y reacciona (simple, desacoplado)
- **Orchestration**: un coordinador central dirige la saga (más control, más complejo)

---

## 6. ¿Cómo implementas observabilidad en un sistema distribuido?
**Respuesta:**
Los 3 pilares:

1. **Logs** (qué pasó):
   - Structured logging (JSON) con traceId
   - ELK Stack o Loki+Grafana
   - `MDC.put("traceId", correlationId)` en cada request

2. **Metrics** (cuánto):
   - Micrometer + Prometheus + Grafana
   - Métricas RED: Rate, Errors, Duration
   - Custom: ventas/minuto, stock_bajo_count

3. **Traces** (dónde):
   - OpenTelemetry → Jaeger/Zipkin
   - Seguir un request a través de 5+ servicios
   - Identificar cuál servicio es el cuello de botella

---

## 7. ¿Cuál es tu estrategia de testing para un equipo de 10 devs?
**Respuesta:**
Pirámide de testing:
```
         /  E2E  \         ← Pocos (10): flujos críticos (Playwright)
        /  Integr. \       ← Medios (100): API + BD (Testcontainers)
       /   Unitarios \     ← Muchos (1000): lógica (JUnit + Mockito)
```

Proceso:
- PR no se mergea sin tests que pasen
- Coverage mínimo: 80% en services, 60% en controllers
- Tests de integración con Testcontainers (BD real, no H2)
- E2E solo en flujos de negocio críticos (login, compra, pago)
- CI ejecuta tests en cada PR (GitHub Actions)

---

## 8. ¿Cómo manejas secretos en producción?
**Respuesta:**
NUNCA en código ni en variables de entorno del Dockerfile.

Opciones:
1. **HashiCorp Vault**: secretos dinámicos con TTL, rotación automática
2. **AWS Secrets Manager**: integrado con ECS/K8s, rotación automática
3. **K8s Secrets + External Secrets Operator**: sincroniza desde Vault/AWS
4. **Spring Cloud Config** + encriptación: para configuración centralizada

```yaml
# application-prod.yml: referencia a Vault
spring:
  cloud:
    vault:
      uri: https://vault.empresa.com
      token: ${VAULT_TOKEN}
      kv:
        backend: secret
        default-context: mi-app
```

---

## 9. ¿Cómo harías un deploy zero-downtime?
**Respuesta:**
1. **Rolling Update** (K8s): reemplaza pods uno a uno
2. **Readiness probe**: no envía tráfico hasta que el pod esté ready
3. **Graceful shutdown**: Spring Boot termina requests en curso (30s grace period)
4. **DB migrations backward-compatible**: no borrar columnas en el mismo deploy
5. **Feature flags**: nueva funcionalidad oculta hasta activar

```yaml
# deployment.yaml
spec:
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxUnavailable: 0      # Nunca tener 0 pods
      maxSurge: 1            # Crear 1 extra durante update
  template:
    spec:
      containers:
        - readinessProbe:
            httpGet: { path: /actuator/health/readiness, port: 8080 }
            initialDelaySeconds: 30
```

---

## 10. Diseña un rate limiter distribuido
**Respuesta:**
Sliding Window con Redis:

```java
@Component
public class RateLimiter {
    private final StringRedisTemplate redis;
    private static final int MAX_REQUESTS = 100;
    private static final int WINDOW_SECONDS = 60;

    public boolean isAllowed(String clientId) {
        String key = "rate:" + clientId;
        long now = Instant.now().toEpochMilli();
        long windowStart = now - (WINDOW_SECONDS * 1000);

        // Atomic operation en Redis (Lua script)
        redis.execute(connection -> {
            connection.zRemRangeByScore(key.getBytes(), 0, windowStart);  // Limpiar expirados
            long count = connection.zCard(key.getBytes());                // Contar en ventana
            if (count < MAX_REQUESTS) {
                connection.zAdd(key.getBytes(), now, String.valueOf(now).getBytes());
                connection.expire(key.getBytes(), WINDOW_SECONDS);
                return true;
            }
            return false;
        });
    }
}
```

Distribuido: Redis centralizado, todos los nodos consultan el mismo counter.
