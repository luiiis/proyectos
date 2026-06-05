# Cómo Ejecutar - Proyecto 17: Observabilidad

## Arquitectura
```
┌──────────┐     ┌────────────┐     ┌──────────┐     ┌──────────────┐
│ App      │────→│ Prometheus │────→│ Grafana  │     │ Alertmanager │
│ (Spring) │     │ (recolecta)│     │(visualiza)│     │  (notifica)  │
│ :8080    │     │   :9090    │     │  :3000   │     │    :9093     │
└──────────┘     └────────────┘     └──────────┘     └──────────────┘
  /actuator/       cada 15s           dashboards        email/slack
  prometheus       scrape             gráficas          cuando alerta
```

## Levantar
```bash
cd academia-profesional/proyecto-17-observabilidad
docker compose up -d

# Esperar 30 segundos
```

## Acceder
```
App:          http://localhost:8080/api/productos
Métricas raw: http://localhost:8080/actuator/prometheus
Prometheus:   http://localhost:9090
Grafana:      http://localhost:3000 (admin / admin123)
Alertmanager: http://localhost:9093
```

## Configurar Grafana
```
1. Abrir http://localhost:3000
2. Login: admin / admin123
3. Datasource ya está configurado (Prometheus)
4. Crear Dashboard → Add Panel
5. Query: rate(http_server_requests_seconds_count[1m])
6. Esto muestra: peticiones por segundo
```

## Queries útiles en Prometheus
```promql
# Peticiones por segundo
rate(http_server_requests_seconds_count{application="api-productos"}[1m])

# Tiempo de respuesta p95
histogram_quantile(0.95, rate(http_server_requests_seconds_bucket[5m]))

# Memoria JVM usada
jvm_memory_used_bytes{area="heap"} / 1024 / 1024

# Conexiones de BD activas
hikaricp_connections_active

# Errores HTTP (status 5xx)
rate(http_server_requests_seconds_count{status=~"5.."}[5m])
```

## Generar tráfico para ver métricas
```bash
# Hacer 100 peticiones para generar datos
for i in $(seq 1 100); do curl -s http://localhost:8080/api/productos > /dev/null; done

# Ahora ver en Grafana las gráficas con datos reales
```
