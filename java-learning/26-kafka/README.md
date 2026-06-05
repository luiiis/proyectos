# Módulo 26: Apache Kafka - Event-Driven Architecture

## ¿Qué es Kafka?
Sistema de mensajería distribuido. Los servicios se comunican mediante EVENTOS en lugar de llamadas directas.

```
Servicio A (Producer)  →  [Topic: pedido-creado]  →  Servicio B (Consumer)
                                                  →  Servicio C (Consumer)
```

## Producer (enviar eventos)
```java
@Service
public class PedidoEventPublisher {
    private final KafkaTemplate<String, PedidoEvent> kafka;

    public void publicarPedidoCreado(Pedido pedido) {
        var evento = new PedidoEvent(pedido.getId(), pedido.getTotal(), Instant.now());
        kafka.send("pedido-creado", pedido.getId().toString(), evento);
    }
}
```

## Consumer (recibir eventos)
```java
@Service
public class InventarioEventListener {

    @KafkaListener(topics = "pedido-creado", groupId = "inventario-service")
    public void onPedidoCreado(PedidoEvent evento) {
        // Descontar inventario cuando se crea un pedido
        inventarioService.descontar(evento.productos());
        log.info("Inventario actualizado para pedido: {}", evento.pedidoId());
    }
}
```

## Docker Compose con Kafka
```yaml
services:
  kafka:
    image: confluentinc/cp-kafka:7.6.0
    environment:
      KAFKA_NODE_ID: 1
      KAFKA_PROCESS_ROLES: broker,controller
      KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092,CONTROLLER://0.0.0.0:9093
      CLUSTER_ID: 'MkU3OEVBNTcwNTJENDM2Qk'
    ports: ["9092:9092"]
```

## Ejercicios
1. Implementa un producer que publique eventos de venta
2. Implementa un consumer que actualice inventario al recibir evento
3. Implementa un consumer que envíe email de confirmación
4. Maneja errores con Dead Letter Queue (DLQ)
