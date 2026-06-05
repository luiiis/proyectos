# Cómo Ejecutar - Proyecto 22: WebSockets

## Ejecutar (no necesita Docker, todo en memoria)
```bash
cd academia-profesional/proyecto-22-websockets
mvn spring-boot:run
```

## Probar
1. Abrir http://localhost:8080 en el navegador
2. Clic en "Conectar WebSocket" (estado cambia a verde)
3. Clic en "Simular Venta" → ves la notificacion EN TIEMPO REAL
4. Abrir OTRA pestaña en http://localhost:8080
5. Conectar en la segunda pestaña
6. Simular venta en la primera → AMBAS pestanas reciben la notificacion

## Probar con curl (desde otra terminal)
```bash
# Registrar venta (todos los clientes WebSocket conectados la reciben)
curl -X POST http://localhost:8080/api/ventas/registrar \
  -H "Content-Type: application/json" \
  -d '{"producto":"Monitor Dell","total":12499,"cliente":"Maria"}'

# Enviar alerta custom
curl -X POST http://localhost:8080/api/notificar \
  -H "Content-Type: application/json" \
  -d '{"mensaje":"Servidor de BD al 90% de capacidad"}'
```

## Qué demuestra
- Conexion WebSocket persistente (no polling)
- Broadcast a TODOS los conectados (1 evento → N clientes)
- Chat bidireccional (cliente envia → servidor reenvía a todos)
- Integración REST + WebSocket (endpoint REST dispara notificacion WS)
- Frontend vanilla JS con SockJS + STOMP
```
