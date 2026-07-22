# Ejercicios y Retos - Proyecto 14: Redis

## Reto 1: Cache con TTL personalizado
Configura diferentes TTL para diferentes datos:
- Productos: cache 1 hora (cambian poco)
- Stock: cache 5 minutos (cambia frecuentemente)
- Dashboard KPIs: cache 30 segundos

**Lo que practicas:** @Cacheable con configuración, TTL, cache policy

---

## Reto 2: Rate Limiting con Redis
Implementa rate limiting usando Redis INCR + EXPIRE:
```java
String key = "rate:" + ip;
Long count = redisTemplate.opsForValue().increment(key);
if (count == 1) redisTemplate.expire(key, 1, TimeUnit.MINUTES);
if (count > 100) throw new TooManyRequestsException();
```
Máximo 100 requests por minuto por IP.

**Lo que practicas:** Redis como contador, rate limiting, seguridad

---

## Reto 3: Sesiones distribuidas
Almacena sesiones de usuario en Redis:
- Si tienes 3 instancias del backend detrás de un load balancer
- El usuario puede llegar a CUALQUIER instancia y su sesión existe
- Spring Session + Redis lo resuelve automáticamente

**Lo que practicas:** Spring Session, Redis como session store, escalabilidad

---

## Reto 4: Pub/Sub con Redis
Implementa notificaciones en tiempo real con Redis Pub/Sub:
- Backend publica "nueva-venta" cuando se registra una venta
- Múltiples subscribers reciben la notificación

**Lo que practicas:** Redis Pub/Sub, messaging simple, real-time

---

## Reto 5: Leaderboard (Sorted Set)
Crea un ranking de productos más vendidos usando Redis Sorted Sets:
```java
redisTemplate.opsForZSet().incrementScore("ranking:productos", productoId, cantidad);
// Top 10:
redisTemplate.opsForZSet().reverseRangeWithScores("ranking:productos", 0, 9);
```

**Lo que practicas:** Redis data structures, Sorted Sets, rankings

---

## Reto 6: Cache-aside vs Write-through
Implementa ambas estrategias y compara:
- **Cache-aside**: lees de cache, si no está → lees de BD y guardas en cache
- **Write-through**: cada write va a BD + cache simultáneamente

¿Cuál es mejor para tu caso de uso?

**Lo que practicas:** Patrones de caching, trade-offs, consistencia
