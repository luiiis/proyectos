# Módulo 17: MySQL - Manual Técnico

## ¿Qué construimos?
Scripts equivalentes al módulo 16 pero para MySQL, mostrando las diferencias de sintaxis y capacidades.

## Cómo ejecutar
```bash
docker exec -i learn-mysql mysql -uroot -pmysql123 empresa_db < mysql-especifico.sql
```

## Diferencias clave con PostgreSQL
- MySQL usa `AUTO_INCREMENT` en lugar de `SERIAL`
- MySQL usa `FULLTEXT` index en lugar de `tsvector`
- MySQL JSON no es tan potente como JSONB de PostgreSQL
- MySQL no tiene CTEs recursivas tan flexibles (desde v8.0 sí las tiene)
- MySQL no tiene `LISTEN/NOTIFY` ni extensiones
