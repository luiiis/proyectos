# Módulo 27: Redis - Cache y Sesiones

## Caché con Spring Boot
```java
@Service
public class ProductoService {

    @Cacheable(value = "productos", key = "#id")
    public ProductoDto buscarPorId(Long id) {
        return repository.findById(id).map(this::toDto).orElseThrow();
    }

    @CacheEvict(value = "productos", allEntries = true)
    public ProductoDto crear(CrearProductoDto dto) { ... }

    @CachePut(value = "productos", key = "#id")
    public ProductoDto actualizar(Long id, ActualizarDto dto) { ... }
}
```

## Rate Limiting con Redis
```java
public boolean permitirRequest(String clienteIp) {
    String key = "rate:" + clienteIp;
    Long requests = redisTemplate.opsForValue().increment(key);
    if (requests == 1) {
        redisTemplate.expire(key, Duration.ofMinutes(1));
    }
    return requests <= 100;  // Máximo 100 requests por minuto
}
```

## Sesiones distribuidas
```yaml
spring:
  session:
    store-type: redis
    redis:
      flush-mode: on-save
```

## Ejercicios
1. Implementa caché en el servicio de productos (TTL 5 min)
2. Implementa rate limiting: 100 requests/min por IP
3. Implementa un ranking de productos más vistos con Redis Sorted Set
4. Compara rendimiento: con caché vs sin caché (medir tiempos)
