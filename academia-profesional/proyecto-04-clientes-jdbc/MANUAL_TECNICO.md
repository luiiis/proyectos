# Manual Técnico - Proyecto 04: Sistema Clientes JDBC

## Objetivo de Aprendizaje
Entender cómo Java se conecta DIRECTAMENTE a una base de datos. Este es el nivel más bajo: sin frameworks, sin magia. Entender esto te permite saber qué hace Spring/JPA "por debajo".

---

## Arquitectura

```
┌────────────────────────────┐         ┌──────────────────────────┐
│     ClientesJDBC.java      │         │    PostgreSQL (Docker)    │
│                            │         │                          │
│  DriverManager.getConnection()       │    clientes_db           │
│         │                  │  JDBC   │      │                   │
│         ▼                  │────────►│      ▼                   │
│  Connection               │  :5432  │   tabla: clientes        │
│         │                  │         │   id, nombre, apellido,  │
│         ▼                  │         │   email, telefono,       │
│  PreparedStatement         │         │   ciudad, activo,        │
│         │                  │         │   created_at             │
│         ▼                  │         │                          │
│  ResultSet (resultados)    │         │                          │
└────────────────────────────┘         └──────────────────────────┘
```

---

## Conceptos Clave

### ¿Qué es JDBC?
```
JDBC = Java Database Connectivity
Es la API estándar de Java para conectarse a CUALQUIER base de datos relacional.
Es como un "enchufe universal": el mismo código Java funciona con PostgreSQL, MySQL, Oracle, etc.
Solo cambias el driver (JAR) y la URL de conexión.
```

### ¿Qué es un PreparedStatement?
```
NUNCA hagas esto (SQL Injection):
  String sql = "SELECT * FROM clientes WHERE nombre = '" + input + "'";
  // Si input = "'; DROP TABLE clientes; --" → destruye tu BD

SIEMPRE haz esto (seguro):
  PreparedStatement ps = conn.prepareStatement("SELECT * FROM clientes WHERE nombre = ?");
  ps.setString(1, input);  // El driver escapa caracteres peligrosos
```

### ¿Qué es try-with-resources?
```java
// Connection, PreparedStatement y ResultSet son RECURSOS que se deben cerrar
// try-with-resources los cierra AUTOMÁTICAMENTE al salir del bloque
try (Connection conn = DriverManager.getConnection(url, user, pass);
     PreparedStatement ps = conn.prepareStatement("SELECT * FROM clientes");
     ResultSet rs = ps.executeQuery()) {
    
    while (rs.next()) {
        System.out.println(rs.getString("nombre"));
    }
}  // ← conn, ps, rs se cierran aquí automáticamente
```

---

## Docker Compose

El archivo `docker-compose.yml` levanta PostgreSQL con:
- Base de datos `clientes_db` creada automáticamente
- Script `sql/schema.sql` ejecutado al inicio (crea tabla + datos de prueba)
- Puerto 5432 expuesto para que Java se conecte

---

## Flujo Completo

```
1. docker compose up -d
   → PostgreSQL arranca en :5432
   → Ejecuta schema.sql automáticamente
   → Tabla "clientes" con datos de prueba

2. Java se conecta:
   DriverManager.getConnection("jdbc:postgresql://localhost:5432/clientes_db", "postgres", "postgres123")

3. Operaciones CRUD:
   INSERT → PreparedStatement con ? + setString/setInt
   SELECT → ResultSet.next() para iterar resultados
   UPDATE → PreparedStatement.executeUpdate()
   DELETE → PreparedStatement.executeUpdate()

4. Cerrar conexión (try-with-resources lo hace automático)
```

---

## Comparativa: JDBC vs JPA (Proyecto 07)

| Aspecto | JDBC (este proyecto) | JPA (proyecto 07) |
|---------|---------------------|-------------------|
| SQL | Lo escribes tú manualmente | Se genera automáticamente |
| Mapeo | ResultSet → manualmente extraer campos | Automático con @Entity |
| Código | 20+ líneas por operación | 1 línea (repository.save()) |
| Control | Total (100% tú decides) | Menos (Hibernate decide mucho) |
| Performance | Óptima (tú optimizas) | Buena (pero puede generar queries malos) |
| Cuándo usarlo | Queries complejos, performance crítica | CRUD estándar, productividad |
