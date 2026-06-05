import java.sql.*;

/**
 * MÓDULO 13: JDBC - Demo ejecutable
 * Prerrequisito: PostgreSQL corriendo en localhost:5432 (docker compose up postgres -d)
 * Ejecutar: javac JdbcDemo.java && java -cp .:postgresql-42.7.3.jar JdbcDemo
 * (Necesitas descargar el driver: https://jdbc.postgresql.org/download/)
 * O más fácil: ejecutar desde el proyecto Maven del módulo 19/20
 */
public class JdbcDemo {
    static final String URL = "jdbc:postgresql://localhost:5432/empresa_db";
    static final String USER = "postgres";
    static final String PASS = "postgres123";

    public static void main(String[] args) {
        System.out.println("═══ MÓDULO 13: JDBC ═══\n");

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            System.out.println("✓ Conectado a PostgreSQL\n");

            // SELECT con PreparedStatement
            System.out.println("── Top 5 productos más caros ──");
            String sql = "SELECT nombre, precio FROM productos WHERE activo = true ORDER BY precio DESC LIMIT ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, 5);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        System.out.printf("  %s: $%,.2f%n", rs.getString("nombre"), rs.getDouble("precio"));
                    }
                }
            }

            // INSERT
            System.out.println("\n── INSERT ──");
            String insert = "INSERT INTO productos (nombre, precio, sku, categoria_id, proveedor_id, activo) VALUES (?, ?, ?, ?, ?, true) RETURNING id";
            try (PreparedStatement ps = conn.prepareStatement(insert)) {
                ps.setString(1, "JDBC Test Product");
                ps.setDouble(2, 777.77);
                ps.setString(3, "JDBC-TEST-001");
                ps.setInt(4, 1);
                ps.setInt(5, 1);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) System.out.println("  Insertado con ID: " + rs.getLong("id"));
            }

            // DELETE (limpiar)
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM productos WHERE sku = ?")) {
                ps.setString(1, "JDBC-TEST-001");
                int deleted = ps.executeUpdate();
                System.out.println("  Limpiado: " + deleted + " registro(s)");
            }

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("¿PostgreSQL está corriendo? Ejecuta: docker compose up postgres -d");
        }
    }
}
