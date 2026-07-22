# Preguntas de Entrevista - Tema: JDBC y Bases de Datos

## Nivel Junior

### 1. ¿Qué es JDBC y para qué sirve?
**Respuesta:**
JDBC (Java Database Connectivity) es la API estándar de Java para conectarse a bases de datos relacionales. Permite ejecutar SQL (SELECT, INSERT, UPDATE, DELETE) desde código Java. Es la capa más baja de acceso a datos; frameworks como JPA/Hibernate usan JDBC internamente.

---

### 2. ¿Qué es SQL Injection y cómo se previene?
**Respuesta:**
SQL Injection es cuando un atacante introduce SQL malicioso a través de la entrada del usuario:
```java
// VULNERABLE:
"SELECT * FROM users WHERE name = '" + input + "'"
// Si input = "'; DROP TABLE users; --" → destruye la tabla

// SEGURO: usar PreparedStatement con parámetros
PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE name = ?");
ps.setString(1, input);  // El driver escapa automáticamente
```

---

### 3. ¿Cuál es la diferencia entre Statement y PreparedStatement?
**Respuesta:**
- **Statement**: ejecuta SQL como texto plano. Vulnerable a SQL Injection. No se reutiliza.
- **PreparedStatement**: SQL precompilado con parámetros (?). Seguro. Se puede reutilizar. Mejor performance para queries repetidos.

**Regla:** NUNCA uses Statement. Siempre PreparedStatement.

---

### 4. ¿Qué es un ResultSet?
**Respuesta:**
Es un cursor que apunta a los resultados de un SELECT. Se recorre con `next()`:
```java
ResultSet rs = ps.executeQuery();
while (rs.next()) {  // Avanza fila por fila
    String nombre = rs.getString("nombre");
    int edad = rs.getInt("edad");
}
```
Es como leer un archivo línea por línea.

---

### 5. ¿Por qué hay que cerrar Connection, PreparedStatement y ResultSet?
**Respuesta:**
Son recursos nativos del sistema operativo (sockets de red, memoria). Si no los cierras:
- Se acumulan conexiones abiertas → la BD rechaza nuevas conexiones
- Memory leaks → la app se queda sin memoria

Solución: try-with-resources (cierra automáticamente).

---

## Nivel Mid

### 6. ¿Qué es un Connection Pool y por qué es necesario?
**Respuesta:**
Crear una conexión a BD es LENTO (~50-200ms). Un pool mantiene conexiones pre-creadas y las reutiliza:
- Sin pool: 100 requests = 100 conexiones nuevas = 10-20 segundos desperdiciados
- Con pool: 100 requests = reutilizan 10-20 conexiones existentes = milisegundos

HikariCP es el estándar en Spring Boot (el más rápido disponible).

---

### 7. ¿Qué son las transacciones ACID?
**Respuesta:**
- **Atomicity**: Todo o nada. Si falla una parte, se deshace todo.
- **Consistency**: La BD pasa de un estado válido a otro estado válido.
- **Isolation**: Transacciones concurrentes no se interfieren.
- **Durability**: Una vez commitado, no se pierde (ni con crash).

```java
conn.setAutoCommit(false);
// operación 1...
// operación 2...
conn.commit();    // Todo OK → se guarda
// conn.rollback(); // Si falla → se deshace todo
```

---

### 8. ¿Cuál es la diferencia entre executeQuery() y executeUpdate()?
**Respuesta:**
- `executeQuery()`: para SELECT. Devuelve `ResultSet` (filas de resultado).
- `executeUpdate()`: para INSERT, UPDATE, DELETE. Devuelve `int` (filas afectadas).
- `execute()`: para cualquiera. Devuelve `boolean` (true si hay ResultSet).

---

### 9. ¿Qué problemas tiene JDBC puro que JPA resuelve?
**Respuesta:**
- Mucho código repetitivo (abrir conexión, crear PS, mapear ResultSet → objeto)
- SQL hardcodeado en Java (difícil de mantener)
- Mapeo manual campo por campo (error-prone)
- Sin caché de entidades
- Sin lazy loading

JPA/Hibernate resuelve todo esto, pero ABSTRAER no es ELIMINAR: JDBC sigue ejecutándose por debajo.

---

### 10. ¿Cómo harías un batch insert de 10,000 registros eficientemente?
**Respuesta:**
```java
conn.setAutoCommit(false);
PreparedStatement ps = conn.prepareStatement("INSERT INTO clientes(nombre, email) VALUES (?, ?)");

for (int i = 0; i < 10000; i++) {
    ps.setString(1, "Cliente " + i);
    ps.setString(2, "cliente" + i + "@mail.com");
    ps.addBatch();          // Acumula
    if (i % 1000 == 0) {
        ps.executeBatch();  // Envía lote de 1000 al servidor
    }
}
ps.executeBatch();  // Enviar los que queden
conn.commit();
```
- Sin batch: 10,000 roundtrips a la BD
- Con batch: ~10 roundtrips (1000x más rápido)
