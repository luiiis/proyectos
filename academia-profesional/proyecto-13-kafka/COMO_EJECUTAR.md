# Cómo Ejecutar - Proyecto 13: Kafka

## Requisitos
- Docker 24+ con Docker Compose v2
- 4GB+ RAM asignados a Docker (Kafka necesita memoria)

## Ejecutar

```bash
cd academia-profesional/proyecto-13-kafka

# Levantar TODO: Kafka + PostgreSQL + Producer + Consumer + UI
docker compose up --build -d

# Esperar ~45 segundos (Kafka tarda en arrancar)
# Verificar que kafka está healthy:
docker compose ps
```

## Probar

```bash
# 1. Publicar un evento de prueba rápido:
curl http://localhost:8080/api/ventas/test

# 2. Ver que el Consumer lo recibió:
docker compose logs -f consumer
# Deberías ver: "📧 NUEVO EVENTO RECIBIDO"

# 3. Publicar evento completo:
curl -X POST http://localhost:8080/api/ventas/evento \
  -H "Content-Type: application/json" \
  -d '{"ventaId":1,"clienteEmail":"cliente@mail.com","total":18999.99,"productos":["Laptop HP","Mouse"]}'

# 4. Ver en Kafka UI (interfaz web):
# http://localhost:8090
# → Topics → ventas.creada → Messages
```

## Accesos

| Servicio | URL | Descripción |
|----------|-----|-------------|
| Producer API | http://localhost:8080 | Publica eventos |
| Kafka UI | http://localhost:8090 | Ver topics, mensajes, consumers |
| Kafka Broker | localhost:29092 | Conexión directa |

## Cómo funciona

```
1. Tú haces POST /api/ventas/evento → Producer
2. Producer envía mensaje a Kafka (topic: ventas.creada)
3. Kafka almacena el mensaje
4. Consumer (suscrito al topic) recibe el mensaje automáticamente
5. Consumer "procesa" (imprime en logs, en prod enviaría email)
```

## Detener

```bash
docker compose down       # Detener
docker compose down -v    # Detener y borrar datos
```
