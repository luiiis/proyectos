# Ejercicios y Retos - Proyecto 22: WebSockets

## Reto 1: Chat en tiempo real
Implementa un chat simple:
- Usuarios se conectan al WebSocket
- Cada mensaje se envía a TODOS los conectados
- Mostrar quién envió qué y cuándo

**Lo que practicas:** STOMP messaging, broadcast, UI en tiempo real

---

## Reto 2: Notificaciones personalizadas
En vez de enviar a todos, envía a un usuario específico:
```java
// Solo al usuario "admin":
messaging.convertAndSendToUser("admin", "/queue/notificaciones", mensaje);
```
Cada usuario solo ve SUS notificaciones.

**Lo que practicas:** User destinations, mensajes privados, targeting

---

## Reto 3: Dashboard con métricas live
Crea un dashboard que muestre en tiempo real:
- Usuarios conectados (entra/sale)
- Ventas del minuto (se actualiza cada venta)
- Alertas de stock (aparecen instantáneamente)

**Lo que practicas:** Múltiples topics, datos en tiempo real, UI reactiva

---

## Reto 4: Reconexión automática
Si la conexión WebSocket se pierde:
- Detectar desconexión
- Intentar reconectar cada 5 segundos
- Mostrar indicador "Reconectando..."
- Al reconectar, solicitar datos perdidos

**Lo que practicas:** Error handling, resilencia, UX

---

## Reto 5: Heartbeat y timeout
Configura heartbeat para detectar conexiones muertas:
```java
registry.setApplicationDestinationPrefixes("/app");
registry.enableSimpleBroker("/topic", "/queue")
    .setHeartbeatValue(new long[]{10000, 10000})  // 10s heartbeat
    .setTaskScheduler(taskScheduler);
```
Si un cliente no responde en 30s → desconectar.

**Lo que practicas:** Connection management, heartbeats, cleanup

---

## Reto 6 (Avanzado): Escalado con Redis Pub/Sub
Si tienes 3 instancias del backend, WebSocket solo notifica a los clientes de ESA instancia. Solución:
- Cada instancia publica eventos en Redis
- Todas las instancias escuchan Redis
- Cuando llega un evento → cada instancia lo envía a SUS clientes WebSocket

**Lo que practicas:** WebSocket scaling, Redis Pub/Sub, arquitectura distribuida
