# Preguntas de Entrevista - Módulo 13: Seguridad en BD

## Nivel Junior

### 1. ¿Qué es GRANT y REVOKE?
**Respuesta:**
- `GRANT`: dar permisos a un usuario/rol
- `REVOKE`: quitar permisos

```sql
GRANT SELECT, INSERT ON productos TO usuario_ventas;
REVOKE DELETE ON productos FROM usuario_ventas;
```

### 2. ¿Cuál es la diferencia entre un usuario y un rol?
**Respuesta:**
En PostgreSQL son lo mismo técnicamente (`CREATE ROLE`). La diferencia es conceptual:
- **Rol:** grupo de permisos (ej: `rol_vendedor` tiene SELECT + INSERT en ventas)
- **Usuario:** persona que HEREDA permisos de uno o más roles

```sql
CREATE ROLE rol_vendedor;
GRANT SELECT, INSERT ON ventas TO rol_vendedor;
GRANT rol_vendedor TO carlos;  -- Carlos hereda los permisos
```

### 3. ¿Qué es el principio de menor privilegio?
**Respuesta:**
Dar a cada usuario SOLO los permisos que necesita para su trabajo, nada más:
- Vendedor: SELECT + INSERT en ventas. NO puede DELETE ni acceder a tabla salarios.
- Reporte: solo SELECT. NO puede modificar nada.
- DBA: todos los permisos.

### 4. ¿Cómo previenes SQL Injection desde la BD?
**Respuesta:**
La BD sola no previene injection (eso es responsabilidad de la app). Pero puedes:
1. Usar stored procedures (el usuario no ejecuta SQL directo)
2. Limitar permisos (sin DROP, TRUNCATE)
3. Usar vistas (no acceso directo a tablas)
4. Row Level Security (PostgreSQL): filtrar filas según el usuario

### 5. ¿Qué es Row Level Security (RLS)?
**Respuesta:**
Política que filtra filas automáticamente según el usuario:
```sql
ALTER TABLE ventas ENABLE ROW LEVEL SECURITY;
CREATE POLICY vendedor_solo_sus_ventas ON ventas
    FOR SELECT USING (empleado_id = current_setting('app.user_id')::INT);
-- Cada vendedor solo ve SUS ventas, sin importar qué query ejecute
```

---

## Nivel Mid

### 6. ¿Cómo implementarías multitenancy con seguridad en la BD?
**Respuesta:**
Opción 1: Schema por tenant
```sql
CREATE SCHEMA tenant_empresa_a;
CREATE SCHEMA tenant_empresa_b;
-- Cada tenant solo ve su schema
SET search_path TO tenant_empresa_a;
```

Opción 2: Discriminador con RLS
```sql
ALTER TABLE datos ENABLE ROW LEVEL SECURITY;
CREATE POLICY tenant_isolation ON datos
    USING (tenant_id = current_setting('app.tenant_id')::INT);
```

### 7. ¿Cómo auditarías quién hizo qué en la BD?
**Respuesta:**
1. Triggers en tablas sensibles → registrar en tabla `auditoria`
2. `current_user` + `inet_client_addr()` para saber quién y desde dónde
3. Extensión `pgaudit` para logging completo a nivel statement
4. No dar acceso directo: todo via stored procedures que registran automáticamente
