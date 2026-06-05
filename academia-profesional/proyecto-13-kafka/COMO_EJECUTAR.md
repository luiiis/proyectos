# Cómo Ejecutar - Proyecto 13: Kafka Notificaciones

## Arquitectura
```
┌──────────────┐     ┌─────────────┐     ┌──────────────┐
│   Producer   │────→│    KAFKA    │────→│   Consumer   │
│ (API ventas) │     │   (Topic)   │     │(Notificador) │
│   :8080      │     │   :29092    │     │              │
└──────────────┘     └─────────────┘     └──────────────┘
                                                │
                                         ┌──────┴──────┐
                                         │  Enviar     │
                                         │  Email/SMS  │
                                         │  Log en BD  │
                                         └─────────────┘
```

## Levantar
```bash
cd academia-profesional/proyecto-13-kafka
docker compose up -d

# Esperar ~30 segundos (Kafka tarda en arrancar)
docker compose ps

# Kafka UI: http://localhost:8090 (ver topics y mensajes)
# Producer API: http://localhost:8080
```

## Probar
```bash
# Publicar evento de venta (el producer lo envía a Kafka)
curl -X POST http://localhost:8080/api/ventas/evento \
  -H "Content-Type: application/json" \
  -d '{"ventaId":1,"clienteEmail":"pedro@mail.com","total":18999.99,"productos":["Laptop HP"]}'

# Ver logs del consumer (procesa el evento)
docker compose logs -f consumer
# Debe mostrar: "Notificación enviada a pedro@mail.com: Venta $18,999.99"

# Ver topics en Kafka UI
# http://localhost:8090 → Topics → venta-creada → Messages
```

## Topics
| Topic | Producer | Consumer | Propósito |
|-------|----------|----------|-----------|
| venta-creada | API Ventas | Notificador | Enviar confirmación al cliente |
| stock-bajo | Inventario | Alertas | Notificar al gerente |
| usuario-registrado | Auth | Welcome | Enviar email de bienvenida |
