# Cómo Ejecutar - Proyecto 15: Microservicios

## Arquitectura
```
                    ┌─────────────────┐
                    │   API Gateway   │ :8080
                    │ (Spring Cloud)  │
                    └────────┬────────┘
            ┌────────────────┼────────────────┐
            ▼                ▼                ▼
   ┌────────────────┐ ┌────────────┐ ┌────────────────┐
   │ Usuarios Svc   │ │Productos   │ │  Ventas Svc    │
   │    :8081       │ │ Svc :8082  │ │    :8083       │
   └───────┬────────┘ └─────┬──────┘ └───────┬────────┘
           ▼                 ▼                 ▼
   ┌────────────────┐ ┌────────────┐ ┌────────────────┐
   │ PostgreSQL     │ │ PostgreSQL │ │  PostgreSQL    │
   │ usuarios_db    │ │productos_db│ │  ventas_db     │
   └────────────────┘ └────────────┘ └────────────────┘
```

## Levantar
```bash
cd academia-profesional/proyecto-15-microservicios
docker compose up --build -d

# Esperar ~60 segundos
docker compose ps
```

## Probar via Gateway (punto de entrada único)
```bash
# Usuarios (gateway rutea a usuarios-service)
curl http://localhost:8080/api/usuarios

# Productos (gateway rutea a productos-service)
curl http://localhost:8080/api/productos

# Ventas (gateway rutea a ventas-service)
curl http://localhost:8080/api/ventas

# Crear venta (ventas-service llama a productos-service internamente)
curl -X POST http://localhost:8080/api/ventas \
  -H "Content-Type: application/json" \
  -d '{"clienteId":1,"productos":[{"productoId":1,"cantidad":2}]}'
```

## Probar servicios individuales (sin gateway)
```bash
curl http://localhost:8081/api/usuarios    # Directo a usuarios
curl http://localhost:8082/api/productos   # Directo a productos
curl http://localhost:8083/api/ventas      # Directo a ventas
```

## Comunicación entre servicios
```
Ventas necesita validar que el producto existe y tiene stock.
En lugar de acceder a la BD de productos directamente:
  ventas-service → HTTP GET → productos-service → responde JSON
Esto mantiene cada servicio INDEPENDIENTE (puede desplegarse solo).
```
