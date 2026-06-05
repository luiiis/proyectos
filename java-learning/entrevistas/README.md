# Simulación de Entrevistas Técnicas Java

## Nivel Junior (0-2 años)

### Pregunta 1: ¿Cuál es la diferencia entre == y .equals()?
**Respuesta**: `==` compara REFERENCIAS (si apuntan al mismo objeto en memoria). `.equals()` compara CONTENIDO (si los valores son iguales). Para Strings y objetos, SIEMPRE usar `.equals()`.

### Pregunta 2: ¿Qué es la diferencia entre ArrayList y LinkedList?
**Respuesta**: ArrayList usa un array interno (acceso O(1) por índice, inserción O(n) al inicio). LinkedList usa nodos enlazados (inserción O(1) al inicio/final, acceso O(n) por índice). Usar ArrayList en 95% de los casos.

### Pregunta 3: ¿Qué es una excepción checked vs unchecked?
### Pregunta 4: Explica los 4 pilares de POO con ejemplos
### Pregunta 5: ¿Qué es el Garbage Collector?

---

## Nivel Semi-Senior (2-4 años)

### Pregunta 1: ¿Cómo funciona HashMap internamente?
**Respuesta**: Usa un array de buckets. Calcula hash de la key → determina el bucket (índice). Si hay colisión (dos keys en mismo bucket), usa linked list (o tree si > 8 elementos). get/put son O(1) promedio, O(n) peor caso.

### Pregunta 2: Explica el patrón Repository en Spring
### Pregunta 3: ¿Qué es el problema N+1 en JPA y cómo resolverlo?
### Pregunta 4: ¿Cómo implementarías paginación en una API REST?
### Pregunta 5: Explica ACID en transacciones

---

## Nivel Senior (4-8 años)

### Pregunta 1: Diseña un sistema de e-commerce que soporte 10K requests/segundo
**Respuesta esperada**: Discutir: load balancer, microservicios, caché (Redis), BD read replicas, CDN, message queue (Kafka), auto-scaling, circuit breakers.

### Pregunta 2: ¿Cómo manejarías una migración de monolito a microservicios?
### Pregunta 3: Explica Hexagonal Architecture y cuándo la usarías
### Pregunta 4: ¿Cómo implementarías eventual consistency entre microservicios?
### Pregunta 5: Diseña un sistema de rate limiting distribuido

---

## Nivel Tech Lead (8+ años)

### Pregunta 1: ¿Cómo evaluarías si un equipo debe usar microservicios o monolito?
### Pregunta 2: ¿Cómo implementarías observabilidad en un sistema distribuido?
### Pregunta 3: Diseña la estrategia de testing para un equipo de 10 developers
### Pregunta 4: ¿Cómo manejarías deuda técnica en un proyecto legacy?
### Pregunta 5: Diseña un proceso de code review efectivo

---

## Coding Challenges

### Challenge 1 (Junior): Implementa un método que determine si un string es anagrama de otro
### Challenge 2 (Mid): Implementa un LRU Cache con O(1) para get y put
### Challenge 3 (Senior): Diseña e implementa un rate limiter con sliding window
### Challenge 4 (Architect): Diseña un sistema de notificaciones que soporte email, SMS, push, con retry y DLQ
