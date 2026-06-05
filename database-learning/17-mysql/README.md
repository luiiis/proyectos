# Módulo 17: MySQL - Características Específicas

## ¿Cuándo elegir MySQL?
MySQL es la BD más popular del mundo por su simplicidad y velocidad en lecturas. Ideal para aplicaciones web, CMS (WordPress), y cuando necesitas replicación simple.

---

## 1. Storage Engines

```sql
-- InnoDB (DEFAULT desde MySQL 5.5): transacciones, FK, row-level locking
CREATE TABLE ventas (id INT PRIMARY KEY) ENGINE=InnoDB;

-- MyISAM (legacy): más rápido en lecturas puras, sin transacciones
CREATE TABLE logs (id INT PRIMARY KEY) ENGINE=MyISAM;

-- MEMORY: datos en RAM (ultra-rápido, se pierde al reiniciar)
CREATE TABLE cache_sesiones (token VARCHAR(255) PRIMARY KEY, data JSON) ENGINE=MEMORY;

-- Ver engine de una tabla
SHOW TABLE STATUS WHERE Name = 'ventas';
```

## 2. Diferencias Clave con PostgreSQL

| Feature | PostgreSQL | MySQL |
|---------|-----------|-------|
| BOOLEAN | Nativo (TRUE/FALSE) | TINYINT(1) (0/1) |
| SERIAL | `SERIAL` | `AUTO_INCREMENT` |
| String concat | `\|\|` | `CONCAT()` |
| ILIKE | Nativo | No existe (usar LOWER()) |
| LIMIT con OFFSET | `LIMIT 10 OFFSET 20` | Igual |
| UPSERT | `ON CONFLICT DO UPDATE` | `ON DUPLICATE KEY UPDATE` |
| CTE Recursiva | Sí (desde v8.4) | Sí (desde v8.0) |
| Window Functions | Sí (desde v8.4) | Sí (desde v8.0) |
| JSON | JSONB (indexable) | JSON (no indexable directamente) |
| Full-text | tsvector + GIN | FULLTEXT index |
| Partitioning | Nativo avanzado | Nativo básico |

## 3. Full-Text Search en MySQL

```sql
-- Crear índice FULLTEXT
ALTER TABLE productos ADD FULLTEXT idx_ft_productos (nombre, descripcion);

-- Buscar (modo natural language)
SELECT nombre, precio,
    MATCH(nombre, descripcion) AGAINST('laptop profesional') AS relevancia
FROM productos
WHERE MATCH(nombre, descripcion) AGAINST('laptop profesional')
ORDER BY relevancia DESC;

-- Modo booleano (operadores + - *)
SELECT nombre FROM productos
WHERE MATCH(nombre, descripcion) AGAINST('+laptop -gaming' IN BOOLEAN MODE);
```

## 4. JSON en MySQL

```sql
-- Crear tabla con JSON
CREATE TABLE pedidos_config (
    id INT AUTO_INCREMENT PRIMARY KEY,
    config JSON NOT NULL
);

INSERT INTO pedidos_config (config) VALUES
('{"envio": "express", "regalo": true, "notas": "Fragil"}');

-- Consultar campos JSON
SELECT config->>'$.envio' AS tipo_envio FROM pedidos_config;
SELECT JSON_EXTRACT(config, '$.notas') FROM pedidos_config;

-- Filtrar por contenido JSON
SELECT * FROM pedidos_config WHERE JSON_EXTRACT(config, '$.regalo') = true;

-- Modificar JSON
UPDATE pedidos_config SET config = JSON_SET(config, '$.envio', 'standard') WHERE id = 1;
```

## 5. Replicación Básica

```sql
-- MySQL soporta replicación Master-Slave nativa
-- Master (escribe) → Slave(s) (solo lectura)

-- En el Master (my.cnf):
-- server-id = 1
-- log_bin = mysql-bin
-- binlog_do_db = empresa_db

-- En el Slave:
CHANGE MASTER TO
    MASTER_HOST='master_ip',
    MASTER_USER='repl_user',
    MASTER_PASSWORD='repl_pass',
    MASTER_LOG_FILE='mysql-bin.000001',
    MASTER_LOG_POS=0;
START SLAVE;
SHOW SLAVE STATUS\G
```

## 6. Variables de Sistema Importantes

```sql
-- Ver configuración actual
SHOW VARIABLES LIKE 'innodb_buffer_pool_size';  -- RAM para caché de datos
SHOW VARIABLES LIKE 'max_connections';           -- Conexiones máximas
SHOW VARIABLES LIKE 'query_cache%';              -- Caché de queries

-- Ajustar en runtime
SET GLOBAL max_connections = 500;
SET GLOBAL innodb_buffer_pool_size = 2147483648;  -- 2GB
```

## 7. Ejercicios

1. Crea una tabla con FULLTEXT y busca productos por descripción
2. Almacena configuración de usuario en JSON y consulta campos específicos
3. Compara rendimiento de InnoDB vs MyISAM para 100K inserts
4. Implementa un esquema de replicación master-slave con Docker
5. ¿Cuándo elegirías MySQL sobre PostgreSQL? Da 3 escenarios reales

---

## Siguiente Módulo
→ [18-Oracle](../18-oracle/README.md)
