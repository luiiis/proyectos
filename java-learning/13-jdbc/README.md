# Módulo 13: JDBC - Conexión Directa a Base de Datos

```java
// Conexión a PostgreSQL
String url = "jdbc:postgresql://localhost:5432/empresa_db";
try (Connection conn = DriverManager.getConnection(url, "postgres", "postgres123")) {

    // PreparedStatement (SIEMPRE usar para prevenir SQL Injection)
    String sql = "SELECT * FROM productos WHERE precio > ? AND categoria_id = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setDouble(1, 1000.0);
        ps.setInt(2, 1);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                System.out.println(rs.getString("nombre") + ": $" + rs.getDouble("precio"));
            }
        }
    }

    // INSERT
    String insert = "INSERT INTO productos (nombre, precio, stock) VALUES (?, ?, ?)";
    try (PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
        ps.setString(1, "Nuevo Producto");
        ps.setDouble(2, 999.99);
        ps.setInt(3, 50);
        ps.executeUpdate();
        ResultSet keys = ps.getGeneratedKeys();
        if (keys.next()) System.out.println("ID generado: " + keys.getLong(1));
    }
}
```

## ¿Por qué aprender JDBC si existe JPA?
- Entender QUÉ hace JPA por debajo
- Queries complejas que JPA no maneja bien
- Performance crítica (JDBC es más rápido que JPA)
- Entrevistas técnicas lo preguntan

## Ejercicios
1. Conecta a PostgreSQL y lista todos los productos
2. Implementa CRUD completo con PreparedStatement
3. Implementa una transacción que registre una venta (encabezado + detalles)
4. Crea un pool de conexiones con HikariCP
