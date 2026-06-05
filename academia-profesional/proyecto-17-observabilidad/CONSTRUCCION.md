# Bitácora de Construcción - Proyecto 17: Observabilidad

## ¿Qué problema resuelve?
Sin observabilidad: "La app está lenta" → ¿Dónde? ¿Por qué? ¿Desde cuándo? No sabes.
Con observabilidad: ves en tiempo real: requests/segundo, latencia, errores, memoria, BD.

## Los 3 pilares de observabilidad

### 1. Métricas (Prometheus)
Números que cambian en el tiempo: requests/s, latencia p95, memoria, CPU.
```
¿Cuántas peticiones por segundo recibo?
¿Cuál es el tiempo de respuesta promedio?
¿Cuánta memoria usa la JVM?
¿Cuántas conexiones de BD están activas?
```

### 2. Logs (ya los tienes con SLF4J)
Texto con contexto: qué pasó, cuándo, quién.
```
2026-05-30 14:30:00 INFO  ProductoService - Producto creado: id=501
2026-05-30 14:30:01 ERROR VentaService - Stock insuficiente: producto=1, stock=0
```

### 3. Traces (OpenTelemetry - futuro)
Seguir una petición a través de múltiples servicios.
```
Request → Gateway (2ms) → Backend (150ms) → PostgreSQL (45ms) → Redis (2ms)
Total: 199ms. Cuello de botella: PostgreSQL (45ms de 199ms = 23%)
```

## Métricas que Spring Boot expone automáticamente
- `http_server_requests_seconds` → latencia de cada endpoint
- `jvm_memory_used_bytes` → memoria heap/non-heap
- `hikaricp_connections_active` → pool de conexiones BD
- `jvm_threads_live_threads` → threads activos
- `process_cpu_usage` → uso de CPU
- `logback_events_total` → errores en logs

## Alertas configuradas
| Alerta | Condición | Acción |
|--------|-----------|--------|
| Alta latencia | p95 > 2 segundos por 5 min | Notificar equipo |
| Errores 5xx | > 10 errores/min | Notificar urgente |
| Memoria alta | Heap > 80% por 10 min | Investigar memory leak |
| BD saturada | Conexiones activas > 80% pool | Escalar o optimizar queries |
