import java.sql.*;
import java.util.*;

/**
 * Proyecto 04: Sistema Clientes con JDBC
 * Demuestra: Connection, PreparedStatement, ResultSet, transacciones
 * Prerrequisito: docker compose up -d (levanta PostgreSQL)
 * Ejecutar: javac ClientesJDBC.java && java -cp .:postgresql-42.7.3.jar ClientesJDBC
 */
public class ClientesJDBC {
    private static final String URL = "jdbc:postgresql://localhost:5432/clientes_db";
    private static final String USER = "postgres";
    private static final String PASS = "postgres123";

    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════");
        System.out.println("  SISTEMA CLIENTES JDBC - Proyecto 04");
        System.out.println("═══════════════════════════════════════\n");

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            System.out.println("✓ Conectado a PostgreSQL\n");

            listarClientes(conn);
            crearCliente(conn, "Roberto", "Méndez", "roberto@mail.com", "Querétaro");
            buscarPorCiudad(conn, "CDMX");
            actualizarEmail(conn, 1, "carlos.garcia@empresa.com");
            eliminar(conn, "roberto@mail.com");
            contarPorCiudad(conn);

        } catch (SQLException e) {
            System.err.println("✗ Error: " + e.getMessage());
            System.err.println("  ¿PostgreSQL está corriendo? Ejecuta: docker compose up -d");
        }
    }

    static void listarClientes(Connection conn) throws SQLException {
        System.out.println("── Todos los clientes ──");
        try (var ps = conn.prepareStatement("SELECT id, nombre, apellido, email, ciudad FROM clientes WHERE activo = true ORDER BY id");
             var rs = ps.executeQuery()) {
            while (rs.next()) {
                System.out.printf("  [%d] %s %s - %s (%s)%n",
                    rs.getInt("id"), rs.getString("nombre"), rs.getString("apellido"),
                    rs.getString("email"), rs.getString("ciudad"));
            }
        }
    }

    static void crearCliente(Connection conn, String nombre, String apellido, String email, String ciudad) throws SQLException {
        System.out.println("\n── Crear cliente ──");
        String sql = "INSERT INTO clientes (nombre, apellido, email, ciudad) VALUES (?, ?, ?, ?) RETURNING id";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, email);
            ps.setString(4, ciudad);
            var rs = ps.executeQuery();
            if (rs.next()) System.out.printf("  ✓ Creado: %s %s (ID: %d)%n", nombre, apellido, rs.getInt("id"));
        }
    }

    static void buscarPorCiudad(Connection conn, String ciudad) throws SQLException {
        System.out.println("\n── Clientes en " + ciudad + " ──");
        try (var ps = conn.prepareStatement("SELECT nombre, apellido FROM clientes WHERE ciudad = ? AND activo = true")) {
            ps.setString(1, ciudad);
            var rs = ps.executeQuery();
            while (rs.next()) System.out.printf("  • %s %s%n", rs.getString(1), rs.getString(2));
        }
    }

    static void actualizarEmail(Connection conn, int id, String nuevoEmail) throws SQLException {
        System.out.println("\n── Actualizar email ──");
        try (var ps = conn.prepareStatement("UPDATE clientes SET email = ? WHERE id = ?")) {
            ps.setString(1, nuevoEmail);
            ps.setInt(2, id);
            int rows = ps.executeUpdate();
            System.out.printf("  ✓ %d registro(s) actualizado(s)%n", rows);
        }
    }

    static void eliminar(Connection conn, String email) throws SQLException {
        System.out.println("\n── Eliminar (soft delete) ──");
        try (var ps = conn.prepareStatement("UPDATE clientes SET activo = false WHERE email = ?")) {
            ps.setString(1, email);
            int rows = ps.executeUpdate();
            System.out.printf("  ✓ %d registro(s) desactivado(s)%n", rows);
        }
    }

    static void contarPorCiudad(Connection conn) throws SQLException {
        System.out.println("\n── Clientes por ciudad ──");
        try (var ps = conn.prepareStatement("SELECT ciudad, COUNT(*) as total FROM clientes WHERE activo = true GROUP BY ciudad ORDER BY total DESC");
             var rs = ps.executeQuery()) {
            while (rs.next()) System.out.printf("  %s: %d%n", rs.getString("ciudad"), rs.getInt("total"));
        }
    }
}
