# Sistema de Ejercicios

## Estructura
- `basicos/` → 300 ejercicios nivel principiante
- `intermedios/` → 300 ejercicios nivel intermedio
- `avanzados/` → 300 ejercicios nivel avanzado

## Reglas
1. Intenta resolver ANTES de ver la solución
2. Las soluciones están en `/soluciones/`
3. Usa la base de datos `empresa_db` (levantada con Docker)
4. Ejecuta los ejercicios en PostgreSQL primero, luego intenta en MySQL/Oracle

## Ejercicios Básicos (Muestra - Módulos 1-4)

### B001 - Seleccionar todos los empleados
Muestra todos los campos de la tabla empleados.

### B002 - Seleccionar nombres de clientes
Muestra solo nombre y apellido de todos los clientes.

### B003 - Filtrar por ciudad
Muestra los clientes que viven en 'Ciudad de México'.

### B004 - Ordenar por salario
Muestra empleados ordenados por salario de mayor a menor.

### B005 - Contar registros
¿Cuántos productos hay en total?

### B006 - Filtrar por rango
Muestra productos con precio entre 1000 y 5000.

### B007 - Buscar por patrón
Muestra empleados cuyo nombre empiece con 'M'.

### B008 - Valores únicos
¿Cuántas ciudades diferentes hay en la tabla clientes?

### B009 - Agrupar y contar
¿Cuántos empleados hay por sucursal?

### B010 - Filtrar grupos
¿Qué categorías tienen más de 30 productos?

### B011-B050 - SELECT básico
(Continúa con variaciones de SELECT, WHERE, ORDER BY)

### B051-B100 - Funciones
(Ejercicios con UPPER, LOWER, ROUND, COUNT, SUM, AVG)

### B101-B150 - JOINs básicos
(INNER JOIN entre 2 tablas)

### B151-B200 - JOINs múltiples
(JOIN entre 3+ tablas)

### B201-B250 - Subconsultas simples
(IN, EXISTS, subconsultas escalares)

### B251-B300 - DML
(INSERT, UPDATE, DELETE con condiciones)
