# Cómo Ejecutar — Mini-Proyecto 42: Caché con Redis

## Con Docker
```cmd
cd mini-proyectos/42-cache-redis
docker compose up -d
```

## Probar
```bash
# 1ra petición (va a BD, lento ~200ms):
curl http://localhost:8080/api/productos
# Log: "Consultando BD..."

# 2da petición (viene de Redis, rápido ~5ms):
curl http://localhost:8080/api/productos
# Log: (sin log, viene del caché)

# Crear producto (invalida caché):
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Nuevo","precio":999}'

# Siguiente GET vuelve a consultar BD (caché fue invalidado)
```

## Ver Redis directamente
```bash
docker exec -it mini42-redis redis-cli
> KEYS *
> GET "productos::all"
> TTL "productos::all"
```

## Parar
```cmd
docker compose down
```
