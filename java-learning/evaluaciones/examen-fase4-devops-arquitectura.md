# Examen Fase 4: DevOps + Arquitectura (Módulos 24-29)

## Instrucciones
- Tiempo: 120 minutos
- Puedes usar IDE y Docker
- Algunas respuestas son documentos/diagramas, no solo código

---

## Sección A: Teoría (30 puntos, 3 pts cada una)

1. ¿Cuál es la diferencia entre Docker image y container? ¿Y entre Dockerfile y docker-compose?
2. Explica el patrón Circuit Breaker. ¿Qué estados tiene y cuándo transiciona?
3. ¿Qué es eventual consistency y por qué es necesaria en microservicios?
4. ¿Cuál es la diferencia entre comunicación síncrona (HTTP) y asíncrona (Kafka) entre servicios?
5. Explica Hexagonal Architecture: puertos, adaptadores, dominio.
6. ¿Qué es un API Gateway y qué responsabilidades tiene?
7. ¿Cuándo usarías CQRS? ¿Cuáles son sus desventajas?
8. ¿Qué es DDD (Domain-Driven Design)? Nombra 3 conceptos clave.
9. ¿Cuál es la diferencia entre un Pod, un Deployment y un Service en Kubernetes?
10. ¿Cómo manejas secretos en producción? (no .env, no hardcoded)

---

## Sección B: Práctica (70 puntos)

### B1. Dockerizar aplicación (15 pts)
Escribe un Dockerfile multi-stage para una app Spring Boot que:
1. Stage 1: compile con Maven (cacheando dependencias)
2. Stage 2: runtime con JRE Alpine
3. Use usuario no-root
4. Tenga health check
5. Optimice JVM para contenedores

Escribe un `docker-compose.yml` que levante:
- La app (depende de postgres healthy)
- PostgreSQL con volumen persistente
- Redis para caché

### B2. Diseñar Microservicios (20 pts)
Dado un sistema de e-commerce, diseña la arquitectura:

1. Identifica 4 microservicios (bounded contexts)
2. Dibuja diagrama de comunicación (quién habla con quién, sync vs async)
3. Para cada servicio define: responsabilidad, BD propia, endpoints principales
4. Explica cómo manejas:
   - Autenticación (¿centralizada o distribuida?)
   - Una venta que necesita: verificar stock → cobrar → crear pedido
   - Qué pasa si el pago falla DESPUÉS de descontar stock

### B3. Implementar Saga (20 pts)
Implementa (pseudocódigo o Java) el flujo de una Saga Orchestrated para "Crear Pedido":

```
Pasos:
1. OrderService: crear pedido (PENDING)
2. InventoryService: reservar stock
3. PaymentService: procesar pago
4. OrderService: confirmar pedido (CONFIRMED)

Compensaciones (si falla paso 3):
3c. PaymentService: (no necesita, no se cobró)
2c. InventoryService: liberar stock reservado
1c. OrderService: cancelar pedido (CANCELLED)
```

Incluye: clase SagaOrchestrator, manejo de estados, retry logic.

### B4. Kubernetes Manifests (15 pts)
Escribe los manifiestos YAML para desplegar tu API:
1. `Deployment` con 3 réplicas, resources limits, probes
2. `Service` de tipo ClusterIP
3. `HorizontalPodAutoscaler` que escale de 3 a 10 pods si CPU > 70%
4. `ConfigMap` con la configuración de la app
5. `Secret` con las credenciales de BD

---

## Sección C: System Design (Bono, 20 pts extra)

### Diseña un sistema de notificaciones push que:
- Soporte 1 millón de usuarios
- Envíe notificaciones en < 5 segundos
- Soporte: email, SMS, push mobile, in-app
- Sea tolerante a fallos (si email falla, no afecta SMS)
- Tenga retry con backoff exponencial
- Registre auditoría de cada envío

Entrega: diagrama de arquitectura + tecnologías elegidas + justificación.

---

## Aprobación
- 50+ = Aprobado (entiende conceptos DevOps)
- 70+ = Puede trabajar con microservicios
- 85+ = Puede diseñar arquitectura de un sistema
- 100+ (con bono) = Nivel architect/tech lead
