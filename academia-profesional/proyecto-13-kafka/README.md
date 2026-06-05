# Proyecto 13: Kafka - Notificaciones Event-Driven

## ¿Qué construimos?
Sistema de notificaciones desacoplado: cuando se crea una venta, se publica un evento en Kafka. Un consumer lo recibe y envía la notificación.

## Arquitectura
```
Producer (API) → publica evento → Kafka (topic) → Consumer (Notificador) → envía email/SMS
```

## Tecnologías
- Apache Kafka (KRaft, sin Zookeeper)
- Spring Kafka (producer + consumer)
- Kafka UI (visualizar topics)

## Ejecutar
```bash
cd academia-profesional/proyecto-13-kafka
docker compose up --build -d
# Esperar 30s (Kafka tarda)

# Publicar evento:
curl -X POST localhost:8080/api/ventas/evento -H "Content-Type: application/json" \
  -d '{"ventaId":1,"clienteEmail":"test@mail.com","total":18999.99,"productos":["Laptop HP"]}'

# Ver que el consumer lo procesó:
docker compose logs -f consumer

# Kafka UI: http://localhost:8090
```

## Código clave
- `producer/src/.../VentaEventPublisher.java` → kafkaTemplate.send()
- `consumer/src/.../NotificacionConsumer.java` → @KafkaListener
