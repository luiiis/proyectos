# Preguntas de Entrevista - Módulo 06: Funciones SQL

## Nivel Junior

### 1. ¿Cuáles son las funciones de agregación más comunes?
**Respuesta:**
- `COUNT()`: contar filas
- `SUM()`: sumar valores
- `AVG()`: promedio
- `MAX()` / `MIN()`: valor máximo/mínimo
- Todas ignoran NULLs (excepto COUNT(*))

### 2. ¿Cuál es la diferencia entre funciones escalares y de agregación?
**Respuesta:**
- **Escalar:** opera en UNA fila, devuelve UN valor por fila (`UPPER('hola')` → 'HOLA')
- **Agregación:** opera en MÚLTIPLES filas, devuelve UN valor por grupo (`SUM(precio)` → total)

### 3. ¿Qué hace CAST y por qué se necesita?
**Respuesta:**
Convierte un tipo de dato a otro:
```sql
SELECT CAST('42' AS INTEGER);         -- texto → número
SELECT CAST(3.14 AS INTEGER);         -- 3 (trunca)
SELECT '2026-07-21'::DATE;            -- PostgreSQL shorthand
SELECT precio::TEXT || ' MXN' FROM productos;  -- número → texto para concatenar
```

### 4. Nombra 3 funciones de fecha útiles
**Respuesta:**
```sql
NOW()                          -- Fecha y hora actual
DATE_TRUNC('month', fecha)     -- Truncar a inicio de mes
EXTRACT(YEAR FROM fecha)       -- Extraer año
AGE(fecha_ingreso)             -- Diferencia con hoy (interval)
fecha + INTERVAL '30 days'     -- Sumar días
```

### 5. ¿Qué es CASE WHEN y cuándo usarlo?
**Respuesta:**
Es el IF/ELSE de SQL:
```sql
SELECT nombre, salario,
  CASE
    WHEN salario >= 50000 THEN 'Alto'
    WHEN salario >= 30000 THEN 'Medio'
    ELSE 'Bajo'
  END AS nivel_salarial
FROM empleados;
```
Útil para categorizar, calcular columnas condicionales, pivotar datos.
