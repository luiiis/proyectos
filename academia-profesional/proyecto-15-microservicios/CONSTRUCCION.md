# Bitácora de Construcción - Proyecto 15: Microservicios

## ¿Cuándo usar microservicios vs monolito?

| Criterio | Monolito | Microservicios |
|----------|----------|----------------|
| Equipo < 5 personas | ✅ Mejor | ❌ Overkill |
| Equipo > 10 personas | ⚠️ Conflictos | ✅ Equipos independientes |
| Escalar todo igual | ✅ Simple | ❌ Innecesario |
| Escalar partes diferentes | ❌ Desperdicio | ✅ Escalar solo lo que necesita |
| Deploy rápido | ✅ 1 deploy | ✅ Deploy independiente |
| Complejidad operativa | ✅ Baja | ❌ Alta (redes, logs, tracing) |

## Decisiones técnicas

| Decisión | Razón |
|----------|-------|
| 1 BD por servicio | Independencia total (cada equipo maneja su BD) |
| API Gateway | Punto de entrada único, routing centralizado |
| RestClient entre servicios | Comunicación síncrona simple |
| Docker Compose | Simula el entorno distribuido localmente |
| Sin Eureka/Consul | Docker DNS resuelve nombres (más simple para aprender) |

## Patrones implementados

### 1. API Gateway
```
Cliente → Gateway → Rutea según path:
  /api/usuarios/**  → usuarios-service:8080
  /api/productos/** → productos-service:8080
  /api/ventas/**    → ventas-service:8080
```

### 2. Database per Service
Cada servicio tiene su propia BD. No comparten tablas.
Si ventas necesita datos de productos → llama al API de productos.

### 3. Circuit Breaker (Resilience4j)
Si productos-service está caído, ventas-service no se queda esperando infinitamente.
Después de 5 fallos → abre el circuito → devuelve respuesta fallback.

## Flujo: Registrar Venta
```
1. Cliente → POST /api/ventas al Gateway
2. Gateway → rutea a ventas-service
3. ventas-service → GET /api/productos/{id} a productos-service (validar stock)
4. productos-service → responde con producto + stock
5. ventas-service → verifica stock suficiente
6. ventas-service → INSERT en ventas_db
7. ventas-service → POST /api/productos/{id}/stock/remove a productos-service
8. productos-service → UPDATE stock en productos_db
9. ventas-service → responde 201 al cliente
```
