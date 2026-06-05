# Proyecto 22: WebSockets (Tiempo Real)

## ¿Qué construimos?
Sistema de notificaciones en tiempo real: cuando se registra una venta, TODOS los dashboards conectados se actualizan instantaneamente (sin recargar).

## HTTP vs WebSocket
```
HTTP (lo que ya sabes):
  Cliente pregunta → Servidor responde → conexion se cierra
  Si quieres datos nuevos → preguntar de nuevo (polling cada 5 segundos)
  Problema: muchas peticiones innecesarias, delay de hasta 5 segundos

WebSocket:
  Cliente conecta → conexion PERMANENTE bidireccional
  Servidor puede enviar datos EN CUALQUIER MOMENTO (sin que el cliente pregunte)
  Uso: chat, notificaciones, dashboards live, juegos, trading
```

## Arquitectura
```
┌──────────────┐     WebSocket      ┌──────────────┐
│  Dashboard 1 │◄──────────────────►│              │
└──────────────┘                    │              │
┌──────────────┐     WebSocket      │  Spring Boot │
│  Dashboard 2 │◄──────────────────►│  (STOMP)     │
└──────────────┘                    │              │
┌──────────────┐     WebSocket      │              │
│  Dashboard 3 │◄──────────────────►│              │
└──────────────┘                    └──────┬───────┘
                                           │
Cuando se registra una venta:              │
  VentaService → envía mensaje a TODOS ────┘
  los dashboards conectados se actualizan INSTANTANEAMENTE
```

## Ejecutar
```bash
cd academia-profesional/proyecto-22-websockets
mvn spring-boot:run

# Abrir 2 pestanas del navegador en: http://localhost:8080
# En una: registrar venta
# En la otra: ver que el dashboard se actualiza EN TIEMPO REAL
```

## Codigo clave (backend)
```java
@Controller
public class NotificacionController {

    private final SimpMessagingTemplate messaging;

    // Cuando se crea una venta, notificar a TODOS los conectados
    public void notificarNuevaVenta(Venta venta) {
        messaging.convertAndSend("/topic/ventas", venta);
        // TODOS los clientes suscritos a "/topic/ventas" reciben esto instantaneamente
    }
}
```

## Codigo clave (frontend Angular)
```typescript
// Conectar a WebSocket
const socket = new SockJS('http://localhost:8080/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, () => {
    // Suscribirse a notificaciones de ventas
    stompClient.subscribe('/topic/ventas', (message) => {
        const venta = JSON.parse(message.body);
        console.log('Nueva venta!', venta);
        // Actualizar dashboard automaticamente
        this.ventas.update(v => [venta, ...v]);
    });
});
```
