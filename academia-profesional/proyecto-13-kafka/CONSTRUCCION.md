# Bitácora de Construcción - Proyecto 13: Kafka

## ¿Qué problema resuelve Kafka?
Sin Kafka: el servicio de ventas LLAMA directamente al servicio de notificaciones.
- Si notificaciones está caído → la venta falla (acoplamiento)
- Si hay 10,000 ventas/segundo → notificaciones se satura

Con Kafka: el servicio de ventas PUBLICA un evento. Kafka lo almacena. El consumer lo procesa cuando pueda.
- Si notificaciones está caído → los eventos se acumulan en Kafka (no se pierden)
- Si hay 10,000 ventas/segundo → Kafka las almacena y el consumer las procesa a su ritmo

## Conceptos clave
```
Producer: publica mensajes en un Topic
Topic: cola de mensajes (como un buzón)
Consumer: lee mensajes del Topic
Consumer Group: múltiples consumers leyendo el mismo topic (paralelismo)
Partition: subdivisión de un topic (permite paralelismo)
Offset: posición del consumer en el topic (sabe dónde se quedó)
```

## Decisiones técnicas
| Decisión | Razón |
|----------|-------|
| KRaft (sin Zookeeper) | Kafka 3.3+ no necesita Zookeeper (más simple) |
| Kafka UI | Visualizar topics y mensajes sin CLI |
| Servicios separados (producer/consumer) | Demuestra desacoplamiento real |
| JSON como formato de mensaje | Simple, legible, compatible con cualquier lenguaje |
| Auto-create topics | Simplicidad en desarrollo (en producción: crear manualmente) |

## Flujo de un evento
```
1. Usuario compra → POST /api/ventas
2. VentaService guarda en BD
3. VentaService publica evento: kafkaTemplate.send("venta-creada", evento)
4. Kafka almacena el mensaje en el topic "venta-creada"
5. NotificacionConsumer escucha el topic
6. @KafkaListener recibe el mensaje
7. Consumer envía email/SMS al cliente
8. Consumer registra en BD que la notificación fue enviada
```
