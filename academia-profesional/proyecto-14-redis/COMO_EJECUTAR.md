# Cómo Ejecutar - Proyecto 14: Redis

## Arquitectura
```
Cliente → API (8080) → ¿Está en Redis?
                         ├── SÍ → Devolver de Redis (2ms)
                         └── NO → Consultar PostgreSQL (200ms) → Guardar en Redis → Devolver
```

## Levantar
```bash
cd academia-profesional/proyecto-14-redis
docker compose up -d

# API: http://localhost:8080
# Redis Commander (GUI): http://localhost:8081
```

## Probar el caché
```bash
# Primera petición: consulta PostgreSQL (~200ms)
time curl http://localhost:8080/api/productos

# Segunda petición: lee de Redis (~5ms) ← 40x más rápido
time curl http://localhost:8080/api/productos

# Ver claves en Redis
docker exec -it redis-cache redis-cli KEYS "*"
# Resultado: "productos::all"

# Ver TTL (tiempo restante antes de expirar)
docker exec -it redis-cache redis-cli TTL "productos::all"
# Resultado: ~300 (5 minutos)

# Borrar caché manualmente
docker exec -it redis-cache redis-cli FLUSHALL
```

## Probar rate limiting
```bash
# Hacer 10 peticiones rápidas (límite: 5/minuto para login)
for i in {1..10}; do
  echo "Request $i:"
  curl -s -o /dev/null -w "%{http_code}" -X POST http://localhost:8080/api/auth/login \
    -H "Content-Type: application/json" -d '{"username":"test","password":"wrong"}'
  echo ""
done
# Las primeras 5: 401 (credenciales inválidas)
# Las siguientes: 429 (Too Many Requests)
```

## Redis Commander
Abrir http://localhost:8081 para ver:
- Claves almacenadas
- TTL de cada clave
- Valores (JSON cacheado)
- Estadísticas de memoria
