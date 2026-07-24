# Mini-Proyecto 42: Caché con Redis

## Qué aprenderás
- Configurar Redis con Spring Boot
- @Cacheable: guardar resultado en caché
- @CacheEvict: invalidar caché cuando datos cambian
- TTL (Time-To-Live): caché que expira automáticamente
- Ver qué hay en Redis (Redis CLI)
- Medir la diferencia de performance (con vs sin caché)

## Docker para Redis
```bash
docker run --name mini-redis -p 6379:6379 -d redis:7-alpine
```

## Dependencia
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```

## Configuración
```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
  cache:
    type: redis
    redis:
      time-to-live: 300000  # 5 minutos
```

## Código
```java
@Service
public class ProductoService {

    @Cacheable(value = "productos", key = "'all'")
    public List<Producto> listarTodos() {
        log.info("⚡ Consultando BD (NO está en caché)");
        return productoMapper.findAll(); // Solo se ejecuta si NO está en Redis
    }

    @CacheEvict(value = "productos", allEntries = true)
    public Producto crear(Producto p) {
        log.info("💾 Creando + invalidando caché");
        productoMapper.insert(p);
        return p;
    }
}
```

## Probar
```bash
# 1ra petición (va a BD, ~200ms):
curl http://localhost:8080/api/productos
# Log: "⚡ Consultando BD"

# 2da petición (viene de Redis, ~5ms):
curl http://localhost:8080/api/productos
# Log: (no hay log, viene del caché)

# Ver en Redis:
docker exec -it mini-redis redis-cli
> KEYS *
> GET "productos::all"
> TTL "productos::all"
```

## Resultado esperado
```
Sin caché:  GET /api/productos → 150-300ms (query a BD cada vez)
Con caché:  GET /api/productos → 2-10ms (Redis, 100x más rápido)
```
