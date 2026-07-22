# Ejercicios y Retos - Proyecto 17: Observabilidad

## Reto 1: Métricas custom
Crea métricas de negocio personalizadas:
```java
@Bean
MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
    Counter ventasCounter = Counter.builder("ventas.total")
        .description("Total de ventas registradas")
        .register(registry);
    
    ventasCounter.increment();  // Al registrar venta
}
```
Visualiza en Grafana: "Ventas por minuto".

**Lo que practicas:** Métricas custom, Micrometer, Prometheus

---

## Reto 2: Dashboard en Grafana
Crea un dashboard con paneles:
- Requests/segundo (rate)
- Latencia p50, p95, p99
- Error rate (% de 5xx)
- JVM memory usage
- Active DB connections

Importa un dashboard pre-hecho: ID 4701 (Spring Boot Statistics).

**Lo que practicas:** Grafana, PromQL, visualización

---

## Reto 3: Alertas
Configura alertas en Alertmanager:
```yaml
groups:
  - name: api-alerts
    rules:
      - alert: HighErrorRate
        expr: rate(http_server_requests_seconds_count{status=~"5.."}[5m]) > 0.1
        for: 2m
        labels:
          severity: critical
        annotations:
          summary: "Error rate alto en API"
```

**Lo que practicas:** Alerting, PromQL, Alertmanager

---

## Reto 4: Logging estructurado
Configura logs en formato JSON:
```json
{"timestamp":"2026-07-21T10:30:00","level":"ERROR","service":"api","traceId":"abc123","message":"Producto no encontrado","productoId":42}
```
Beneficio: se pueden parsear, filtrar y buscar automáticamente.

**Lo que practicas:** Structured logging, JSON logs, correlación

---

## Reto 5: SLOs y SLIs
Define Service Level Objectives para tu API:
- **Disponibilidad**: 99.9% (máximo 8.7 horas de downtime/año)
- **Latencia**: p95 < 500ms
- **Error rate**: < 0.1%

Crea un panel en Grafana que muestre "error budget" restante.

**Lo que practicas:** SRE concepts, SLOs, error budgets

---

## Reto 6 (Avanzado): Distributed Tracing
Agrega OpenTelemetry para seguir un request a través de múltiples servicios:
- Cada servicio propaga el traceId
- Visualiza en Jaeger/Zipkin el árbol completo de calls
- Identifica cuál servicio es el cuello de botella

**Lo que practicas:** Tracing, OpenTelemetry, spans, troubleshooting
