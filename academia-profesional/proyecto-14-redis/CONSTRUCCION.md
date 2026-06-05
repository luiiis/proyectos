# Bitácora de Construcción - Proyecto 14: Redis

## ¿Qué problema resuelve Redis?
Sin caché: cada petición consulta PostgreSQL (100-500ms por query).
Con caché: la primera vez consulta PostgreSQL, las siguientes leen de Redis (1-5ms).

## 3 usos de Redis en este proyecto

### 1. Caché de datos (@Cacheable)
```java
@Cacheable(value = "productos", key = "'all'")
public List<Producto> listar() {
    return repository.findAll(); // Solo se ejecuta si no está en caché
}

@CacheEvict(value = "productos", allEntries = true)
public Producto crear(Producto p) {
    return repository.save(p); // Borra el caché (datos cambiaron)
}
```

### 2. Rate Limiting (protección contra abuso)
```java
public boolean permitir(String ip) {
    String key = "rate:" + ip;
    Long count = redis.opsForValue().increment(key);
    if (count == 1) redis.expire(key, Duration.ofMinutes(1));
    return count <= 100; // Máximo 100 requests/minuto por IP
}
```

### 3. Sesiones distribuidas
Si tienes 3 instancias del backend, todas comparten sesiones via Redis.
Sin Redis: si el load balancer te manda a otra instancia, pierdes la sesión.

## Decisiones técnicas
| Decisión | Razón |
|----------|-------|
| maxmemory 256mb | Limitar RAM usada por Redis |
| allkeys-lru | Cuando se llena, elimina lo menos usado recientemente |
| appendonly yes | Persistir datos en disco (sobrevive reinicios) |
| TTL 5 minutos | Balance entre frescura y performance |
| Redis Commander | GUI para ver qué hay en caché sin CLI |

## Métricas de mejora
```
Sin caché:
  GET /api/productos → 180ms (PostgreSQL query)
  100 requests/segundo → 100 queries/segundo a la BD

Con caché:
  GET /api/productos → 3ms (Redis)
  100 requests/segundo → 1 query/5min a la BD (99.99% menos carga)
```
