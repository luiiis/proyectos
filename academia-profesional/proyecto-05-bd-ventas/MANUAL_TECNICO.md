# Manual Técnico - Proyecto 05: Base de Datos de Ventas

## Objetivo de Aprendizaje
Dominar SQL completo: diseño de esquemas normalizados, queries complejos, triggers, procedures, views e índices. Este es el proyecto MÁS importante para un Full Stack: el 90% de las apps son CRUD contra una BD relacional.

---

## Modelo Entidad-Relación

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│  categorias  │     │ proveedores  │     │  sucursales  │
│  id, nombre  │     │ id, nombre,  │     │ id, nombre,  │
│              │     │ contacto     │     │ ciudad       │
└──────┬───────┘     └──────┬───────┘     └──────┬───────┘
       │                    │                    │
       │ 1:N                │ 1:N                │ 1:N
       ▼                    ▼                    ▼
┌─────────────────────────────────┐     ┌──────────────┐
│           productos             │     │  empleados   │
│ id, nombre, precio, stock,      │     │ id, nombre,  │
│ categoria_id, proveedor_id      │     │ sucursal_id  │
└──────────────┬──────────────────┘     └──────┬───────┘
               │                               │
               │ N:M (via detalle)             │ 1:N
               ▼                               ▼
┌──────────────────────────────────────────────────────┐
│                      ventas                           │
│ id, fecha, total, cliente_id, empleado_id,           │
│ sucursal_id                                          │
└──────────────────────┬───────────────────────────────┘
                       │ 1:N
                       ▼
┌──────────────────────────────────────────────────────┐
│                  detalle_venta                        │
│ id, venta_id, producto_id, cantidad, precio_unitario │
└──────────────────────────────────────────────────────┘

┌──────────────┐
│   clientes   │
│ id, nombre,  │
│ email, tel   │──────► referenciado por ventas.cliente_id
└──────────────┘

┌──────────────┐
│  auditoria   │ ← triggers escriben aquí automáticamente
│ tabla, op,   │
│ datos, fecha │
└──────────────┘
```

---

## Conceptos SQL Clave

### Normalización (3FN)
```
1FN: Cada campo es atómico (no "producto1, producto2" en un campo)
2FN: Todo depende de TODA la clave primaria
3FN: Ningún campo depende de otro campo no-clave

Ejemplo de violación 3FN:
  ventas(id, cliente_id, cliente_nombre, cliente_email)
  ← cliente_nombre depende de cliente_id, no de venta.id
  Solución: separar en tabla clientes

Beneficio: si el cliente cambia su email, lo cambias en 1 lugar
```

### Triggers
```sql
-- Se ejecuta AUTOMÁTICAMENTE cuando insertas en ventas:
CREATE TRIGGER trg_venta_auditoria
AFTER INSERT ON ventas
FOR EACH ROW
EXECUTE FUNCTION fn_registrar_auditoria();
-- No necesitas llamarlo manualmente, la BD lo hace sola
```

### Stored Procedures
```sql
-- Operación compleja en 1 llamada (transaccional):
CALL registrar_venta(cliente_id, empleado_id, items[]);
-- Internamente: crea venta + detalles + descuenta stock + auditoría
-- Si algo falla → ROLLBACK de todo
```

### Views
```sql
-- Query guardada que se reutiliza como si fuera tabla:
CREATE VIEW v_ventas_resumen AS
SELECT v.id, c.nombre AS cliente, e.nombre AS vendedor, v.total, v.fecha
FROM ventas v
JOIN clientes c ON v.cliente_id = c.id
JOIN empleados e ON v.empleado_id = e.id;

-- Usar como tabla:
SELECT * FROM v_ventas_resumen WHERE fecha > '2026-01-01';
```

### Índices
```sql
-- Sin índice: buscar en 1 millón de filas = recorrer todas (lento)
-- Con índice: ir directo a la fila correcta (rápido)
CREATE INDEX idx_ventas_fecha ON ventas(fecha);
CREATE INDEX idx_productos_categoria ON productos(categoria_id);

-- Verificar que se usa:
EXPLAIN ANALYZE SELECT * FROM ventas WHERE fecha > '2026-07-01';
-- Debe decir "Index Scan" no "Seq Scan"
```

---

## Cómo Ejecutar

```bash
# 1. Levantar PostgreSQL + Adminer (GUI web)
cd proyecto-05-bd-ventas
docker compose up -d

# 2. Conectar con psql:
docker exec -it ventas-postgres psql -U postgres -d ventas_db

# 3. O usar Adminer (interfaz web):
# http://localhost:8081
# Sistema: PostgreSQL | Servidor: postgres | User: postgres | Pass: postgres123 | BD: ventas_db

# 4. Explorar:
\dt              -- ver todas las tablas
\d productos     -- ver estructura de una tabla
SELECT COUNT(*) FROM ventas;  -- contar ventas
```
