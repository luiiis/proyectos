# Módulo 18: Oracle - Manual Técnico

## ¿Qué construimos?
Scripts PL/SQL que demuestran las capacidades enterprise de Oracle:
- Packages (agrupación de procedures/functions)
- Analytic functions avanzadas
- Tablespaces y gestión de almacenamiento
- Diferencias de sintaxis con PostgreSQL/MySQL

## Cómo ejecutar
```bash
docker exec -it learn-oracle sqlplus empresa_user/empresa123@XEPDB1 @oracle-especifico.sql
```

## ¿Cuándo se justifica Oracle?
- Sistemas bancarios con millones de transacciones/segundo
- Gobierno con requisitos de certificación
- Cuando ya tienes licencia y equipo capacitado
- RAC (Real Application Clusters) para alta disponibilidad extrema
