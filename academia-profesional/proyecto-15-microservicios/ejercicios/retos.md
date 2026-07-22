# Ejercicios y Retos - Proyecto 15: Microservicios

## Reto 1: Agregar un tercer servicio
Crea `clientes-service` (:8083) con su propia base de datos PostgreSQL:
- CRUD de clientes
- Docker Compose con su propia BD
- Ruta en Gateway: `/api/clientes/**` → clientes-service

**Lo que practicas:** Crear microservicio desde cero, routing en Gateway

---

## Reto 2: Comunicación entre servicios
Cuando se registra una venta, `ventas-service` necesita verificar que el producto existe en `productos-service`:
```java
// ventas-service llama a productos-service via HTTP:
restClient.get()
    .uri("http://productos-service:8082/api/productos/{id}", productoId)
    .retrieve()
    .body(ProductoResponse.class);
```

**Lo que practicas:** Comunicación inter-service, RestClient, DTOs compartidos

---

## Reto 3: Circuit Breaker con Resilience4j
Si `productos-service` se cae, `ventas-service` no debe bloquearse. Implementa circuit breaker:
```java
@CircuitBreaker(name = "productos", fallbackMethod = "productoFallback")
public ProductoResponse obtenerProducto(Long id) {
    return restClient.get().uri("...").retrieve().body(ProductoResponse.class);
}

public ProductoResponse productoFallback(Long id, Exception e) {
    return new ProductoResponse(id, "Producto temporal", 0.0);  // Respuesta degradada
}
```

**Lo que practicas:** Resilience4j, tolerancia a fallos, fallbacks

---

## Reto 4: Service Discovery con Eureka
En vez de URLs hardcodeadas, implementa descubrimiento de servicios:
- Eureka Server (registry central)
- Cada servicio se registra al arrancar
- El Gateway descubre servicios por nombre (no por host:port)

**Lo que practicas:** Eureka, service discovery, desacoplamiento

---

## Reto 5: Configuración centralizada
Usa Spring Cloud Config Server:
- Todas las configuraciones en un repo Git
- Los servicios las cargan al arrancar
- Cambiar config → reiniciar servicio (o usar @RefreshScope)

**Lo que practicas:** Spring Cloud Config, configuración externa, 12-Factor App

---

## Reto 6: Tracing distribuido
Cuando un request pasa por Gateway → Service A → Service B, ¿cómo rastrear el flujo completo?
- Agrega `spring-boot-starter-actuator` + Micrometer Tracing
- Cada request tiene un traceId único que se propaga
- Visualiza en Zipkin/Jaeger: `docker run -p 9411:9411 openzipkin/zipkin`

**Lo que practicas:** Observabilidad, distributed tracing, correlación de logs

---

## Reto 7: Event-driven con Kafka
En vez de HTTP síncrono, usa Kafka para comunicación asíncrona:
- `ventas-service` publica evento "VentaCreada" en topic
- `productos-service` consume el evento y descuenta stock
- Si productos-service está caído → el evento espera en Kafka (no se pierde)

**Lo que practicas:** Comunicación asíncrona, desacoplamiento temporal, eventual consistency

---

## Reto 8 (Avanzado): Saga Pattern
Implementa una saga para "registrar venta":
1. `ventas-service`: crear venta (estado PENDIENTE)
2. `productos-service`: descontar stock
3. `pagos-service`: procesar pago
4. Si el pago falla → compensar: devolver stock + cancelar venta

**Lo que practicas:** Saga pattern, compensación, consistencia eventual, transacciones distribuidas
