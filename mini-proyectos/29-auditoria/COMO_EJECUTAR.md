# Cómo Ejecutar — Mini-Proyecto 29: Auditoría Automática

## Con Docker
```cmd
cd mini-proyectos/29-auditoria
docker compose up -d
```

## Probar
```bash
# Crear un producto (se registra automáticamente en auditoría)
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Laptop","precio":18999}'

# Ver historial de auditoría
curl http://localhost:8080/api/auditoria

# Filtrar por tabla
curl "http://localhost:8080/api/auditoria?tabla=productos"
```

## Parar
```cmd
docker compose down
```
