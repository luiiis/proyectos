# Módulo 15: Optimización - Manual Técnico

## ¿Qué construimos?
Un laboratorio de performance donde tomas queries lentas y las haces rápidas. Incluye:
- Queries problemáticas reales (simulan producción)
- Proceso de diagnóstico con EXPLAIN ANALYZE
- Soluciones aplicadas (índices, reescritura, particionamiento)
- Mediciones antes/después

## ¿Por qué importa?
Una query que tarda 30 segundos en producción con 10 millones de registros puede:
- Bloquear otras queries
- Agotar conexiones del pool
- Hacer que la app se sienta "lenta" para el usuario
- Costar dinero (más CPU en cloud = más factura)

## Cómo se diagnostica (proceso Senior)

```
1. Identificar la query lenta (pg_stat_statements, logs, APM)
2. EXPLAIN ANALYZE → ver el plan de ejecución
3. Buscar: Seq Scan en tablas grandes, Sort sin índice, Nested Loop con tabla grande
4. Hipótesis: "Si creo un índice en X, debería usar Index Scan"
5. Crear índice
6. EXPLAIN ANALYZE de nuevo → verificar mejora
7. Medir en producción (no solo en desarrollo)
```

## Cómo ejecutar
```bash
docker exec -i learn-postgres psql -U postgres -d empresa_db < laboratorio-performance.sql
```
