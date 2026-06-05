# Módulo 13: Seguridad en Bases de Datos

## Conceptos: Usuarios, Roles, Permisos

```sql
-- ═══════ PostgreSQL ═══════

-- Crear usuario
CREATE USER vendedor1 WITH PASSWORD 'vendedor_seguro_2026!';
CREATE USER gerente1 WITH PASSWORD 'gerente_seguro_2026!';

-- Crear roles (grupos de permisos)
CREATE ROLE rol_vendedor;
CREATE ROLE rol_gerente;
CREATE ROLE rol_admin;

-- GRANT: otorgar permisos
GRANT SELECT ON productos, clientes, ventas TO rol_vendedor;
GRANT INSERT ON ventas, detalle_venta TO rol_vendedor;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO rol_admin;

-- Asignar rol a usuario
GRANT rol_vendedor TO vendedor1;
GRANT rol_gerente TO gerente1;

-- REVOKE: quitar permisos
REVOKE DELETE ON productos FROM rol_vendedor;

-- Permisos a nivel de columna
GRANT SELECT (nombre, email, puesto) ON empleados TO rol_vendedor;
-- El vendedor puede ver nombre/email/puesto pero NO salario

-- ═══════ MySQL ═══════
CREATE USER 'vendedor1'@'%' IDENTIFIED BY 'vendedor_seguro_2026!';
GRANT SELECT, INSERT ON empresa_db.ventas TO 'vendedor1'@'%';
FLUSH PRIVILEGES;

-- ═══════ Oracle ═══════
CREATE USER vendedor1 IDENTIFIED BY vendedor_seguro;
GRANT CONNECT, RESOURCE TO vendedor1;
GRANT SELECT ON productos TO vendedor1;
```

## Row Level Security (PostgreSQL)

```sql
-- Cada vendedor solo ve SUS ventas
ALTER TABLE ventas ENABLE ROW LEVEL SECURITY;

CREATE POLICY ventas_por_empleado ON ventas
    FOR SELECT
    USING (empleado_id = current_setting('app.empleado_id')::integer);
-- Ahora, sin importar qué query haga el vendedor, solo ve sus propias ventas
```

## Ejercicios
1. Diseña un esquema de permisos para: admin, gerente, vendedor, auditor
2. Implementa Row Level Security para que cada sucursal solo vea sus datos
3. ¿Cómo protegerías contra SQL Injection desde la BD?
4. Crea un usuario de solo lectura para reportes
5. Implementa una política de passwords (expiración, complejidad)
