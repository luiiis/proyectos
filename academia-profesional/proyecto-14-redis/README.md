# Proyecto 14: Redis - Caché y Rate Limiting

## ¿Qué construimos?
API de productos con caché Redis. La primera petición consulta PostgreSQL (lento). Las siguientes leen de Redis (100x más rápido).

## Tecnologías
- Spring Boot + Spring Data Redis
- @Cacheable / @CacheEvict (anotaciones de caché)
- Redis Commander (GUI para ver el caché)
- PostgreSQL (datos persistentes)

## Ejecutar
```bash
cd academia-profesional/proyecto-14-redis
docker compose up --build -d

# 1ra petición (lenta ~200ms, va a PostgreSQL):
time curl localhost:8080/api/productos

# 2da petición (rápida ~5ms, viene de Redis):
time curl localhost:8080/api/productos

# Ver caché en Redis Commander:
# http://localhost:8081
```

## Cómo funciona
```java
@Cacheable(value = "productos", key = "'all'")  // Si está en Redis → devolver
public List<Producto> listar() {
    return repo.findAll();  // Solo se ejecuta si NO está en caché
}

@CacheEvict(value = "productos", allEntries = true)  // Borrar caché
public Producto crear(Producto p) {
    return repo.save(p);  // Datos cambiaron → invalidar caché
}
```
