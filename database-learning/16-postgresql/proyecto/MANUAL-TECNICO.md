# Módulo 16: PostgreSQL Avanzado - Manual Técnico

## ¿Qué construimos?
Funcionalidades exclusivas de PostgreSQL que no existen en MySQL/Oracle:
- JSONB para datos flexibles (NoSQL dentro de SQL)
- Full-Text Search en español
- CTEs recursivas para jerarquías
- Extensiones (pg_trgm para búsqueda fuzzy)
- LISTEN/NOTIFY para eventos en tiempo real

## ¿Por qué PostgreSQL tiene esto?
PostgreSQL es la BD relacional más EXTENSIBLE. Permite agregar tipos de datos, operadores, funciones e índices personalizados. Esto la hace ideal para:
- Datos semi-estructurados (JSONB reemplaza MongoDB en muchos casos)
- Búsqueda de texto (reemplaza Elasticsearch para casos simples)
- Datos geoespaciales (PostGIS reemplaza servicios de mapas)

## Cómo ejecutar
```bash
docker exec -i learn-postgres psql -U postgres -d empresa_db < features-avanzadas.sql
```
