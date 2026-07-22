# Ejercicios y Retos - Proyecto 13: Kafka

## Reto 1: Múltiples topics
Crea topics separados para diferentes eventos:
- `ventas.creada` → notificar al cliente
- `stock.bajo` → alertar al equipo de compras
- `usuario.registrado` → email de bienvenida

Cada consumer escucha su topic específico.

**Lo que practicas:** Topics, routing de eventos, organización

---

## Reto 2: Dead Letter Queue (DLQ)
Si un consumer no puede procesar un mensaje (ej: email inválido):
- Reintentar 3 veces
- Si sigue fallando → enviar a un topic `.dlq` (dead letter queue)
- Crear un endpoint admin que muestre mensajes fallidos

**Lo que practicas:** Error handling, retry, DLQ, resiliencia

---

## Reto 3: Consumer Groups
Escala el consumer a 3 instancias:
- Las 3 comparten el mismo `group-id`
- Kafka distribuye particiones entre ellas (cada mensaje se procesa 1 sola vez)
- Si una se cae, las otras se rebalancean

**Lo que practicas:** Consumer groups, particiones, escalado horizontal

---

## Reto 4: Event Sourcing simple
En vez de UPDATE en la BD, publica eventos:
- `ProductoCreado`, `StockActualizado`, `PrecioModificado`
- Un consumer lee los eventos y construye el estado actual
- Puedes "rebobinar" y reconstruir el estado desde el inicio

**Lo que practicas:** Event sourcing, replay, estado derivado

---

## Reto 5: Monitoreo con Kafka UI
Configura Kafka UI y monitorea:
- Messages/segundo por topic
- Consumer lag (mensajes pendientes)
- Particiones y rebalanceo
- Offset de cada consumer group

**Lo que practicas:** Observabilidad, monitoring, troubleshooting

---

## Reto 6 (Avanzado): Exactly-once semantics
Configura producer y consumer para garantizar que cada mensaje se procesa EXACTAMENTE una vez (no 0, no 2):
```properties
# Producer
spring.kafka.producer.acks=all
spring.kafka.producer.enable-idempotence=true

# Consumer
spring.kafka.consumer.enable-auto-commit=false
# Manual commit después de procesar
```

**Lo que practicas:** Garantías de entrega, idempotencia, transacciones Kafka
