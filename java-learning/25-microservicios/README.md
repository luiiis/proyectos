# Módulo 25: Microservicios con Spring Cloud

## Arquitectura
```
                    ┌─────────────┐
                    │ API Gateway │ (Spring Cloud Gateway)
                    └──────┬──────┘
           ┌───────────────┼───────────────┐
           ▼               ▼               ▼
    ┌─────────────┐ ┌─────────────┐ ┌─────────────┐
    │  Productos  │ │   Ventas    │ │  Usuarios   │
    │  Service    │ │  Service    │ │  Service    │
    └──────┬──────┘ └──────┬──────┘ └──────┬──────┘
           │               │               │
           ▼               ▼               ▼
    ┌─────────────┐ ┌─────────────┐ ┌─────────────┐
    │ PostgreSQL  │ │ PostgreSQL  │ │ PostgreSQL  │
    └─────────────┘ └─────────────┘ └─────────────┘

    + Service Discovery (Consul/Eureka)
    + Config Server (Spring Cloud Config)
    + Circuit Breaker (Resilience4j)
    + Distributed Tracing (OpenTelemetry)
```

## Componentes clave
- **API Gateway**: punto de entrada único, routing, rate limiting
- **Service Discovery**: los servicios se registran y se encuentran
- **Config Server**: configuración centralizada
- **Circuit Breaker**: si un servicio falla, no tumba todo
- **Event Bus**: comunicación asíncrona (Kafka)

## Comunicación entre servicios
```java
// Síncrona: RestClient (Spring 6.1+)
RestClient client = RestClient.create("http://productos-service");
ProductoDto producto = client.get()
    .uri("/api/productos/{id}", productoId)
    .retrieve()
    .body(ProductoDto.class);

// Asíncrona: Kafka (ver módulo 26)
kafkaTemplate.send("pedido-creado", new PedidoEvent(pedidoId, items));
```

## Ejercicios
1. Divide el monolito en 3 microservicios (productos, ventas, usuarios)
2. Implementa API Gateway con Spring Cloud Gateway
3. Agrega Circuit Breaker con Resilience4j
4. Implementa comunicación entre servicios con RestClient
