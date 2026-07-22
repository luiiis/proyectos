# Ejercicios y Retos - Proyecto 04: JDBC

## Reto 1: Agregar campo "fecha_nacimiento"
1. Modifica `schema.sql` para agregar una columna `fecha_nacimiento DATE`
2. Modifica el INSERT para incluir la fecha
3. Modifica el SELECT para leer con `rs.getDate("fecha_nacimiento")`

**Lo que practicas:** ALTER TABLE, tipos de dato Date en Java/SQL

---

## Reto 2: Buscar por ciudad
Agrega un método `buscarPorCiudad(String ciudad)` que use:
```java
PreparedStatement ps = conn.prepareStatement(
    "SELECT * FROM clientes WHERE ciudad ILIKE ?"
);
ps.setString(1, "%" + ciudad + "%");
```

**Lo que practicas:** LIKE/ILIKE, búsqueda parcial, PreparedStatement

---

## Reto 3: Paginación manual
Implementa un método `listarPaginado(int pagina, int tamano)`:
```sql
SELECT * FROM clientes ORDER BY id LIMIT ? OFFSET ?
-- OFFSET = (pagina - 1) * tamano
```

**Lo que practicas:** LIMIT/OFFSET, paginación SQL

---

## Reto 4: Transacciones
Implementa una transferencia que debe ser atómica (todo o nada):
```java
conn.setAutoCommit(false);
try {
    // INSERT cliente 1
    // INSERT cliente 2
    conn.commit();
} catch (Exception e) {
    conn.rollback();  // Si algo falla, deshacer todo
}
```

**Lo que practicas:** Transacciones ACID, commit/rollback

---

## Reto 5: Connection Pool
Investiga e implementa HikariCP (el pool de conexiones más rápido):
```java
HikariConfig config = new HikariConfig();
config.setJdbcUrl("jdbc:postgresql://localhost:5432/clientes_db");
config.setMaximumPoolSize(10);
HikariDataSource ds = new HikariDataSource(config);
Connection conn = ds.getConnection();  // Reutiliza conexiones
```

**Lo que practicas:** Pool de conexiones, rendimiento, recurso compartido

---

## Reto 6: Patrón DAO
Refactoriza el código en capas:
```
ClienteDAO (interface)  → define operaciones: findAll, findById, save, update, delete
ClienteDAOImpl          → implementa con JDBC
Main                    → usa solo la interface (puede cambiar implementación)
```

**Lo que practicas:** Patrón DAO, interfaces, separación de responsabilidades
