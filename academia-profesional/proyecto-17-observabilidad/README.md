# Proyecto 17: Observabilidad

## ¿Qué construimos?
Monitoreo completo: Prometheus recolecta métricas, Grafana las visualiza, Alertmanager notifica cuando algo está mal.

## Stack
- Prometheus (recolecta métricas cada 15s)
- Grafana (dashboards con gráficas)
- Alertmanager (envía alertas)
- Spring Boot Actuator (expone métricas)

## Ejecutar
```bash
cd academia-profesional/proyecto-17-observabilidad
docker compose up -d

# Generar tráfico:
for i in $(seq 1 50); do curl -s localhost:8080/api/productos > /dev/null; done

# Ver métricas:
# Prometheus: http://localhost:9090
# Grafana: http://localhost:3000 (admin/admin123)
# Métricas raw: http://localhost:8080/actuator/prometheus
```

## Alertas configuradas
- Latencia p95 > 2 segundos → warning
- Errores 5xx > 10% → critical
- Memoria heap > 80% → warning
