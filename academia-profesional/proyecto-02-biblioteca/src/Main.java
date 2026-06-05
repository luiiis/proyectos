import java.util.*;

/**
 * Proyecto 02: Sistema Biblioteca - POO completa
 * Demuestra: herencia, polimorfismo, interfaces, abstracción, colecciones
 * Ejecutar: javac *.java && java Main
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════");
        System.out.println("  SISTEMA BIBLIOTECA - Proyecto 02");
        System.out.println("═══════════════════════════════════════\n");

        Biblioteca biblioteca = new Biblioteca("Biblioteca Central");

        // Agregar materiales (polimorfismo: todos son MaterialBiblioteca)
        biblioteca.agregar(new Libro("Clean Code", "Robert Martin", "978-0132350884", 464));
        biblioteca.agregar(new Libro("Design Patterns", "GoF", "978-0201633610", 395));
        biblioteca.agregar(new Libro("Java Efectivo", "Joshua Bloch", "978-0134685991", 412));
        biblioteca.agregar(new Revista("IEEE Software", "IEEE", "Ene 2026", 45));
        biblioteca.agregar(new Revista("ACM Computing", "ACM", "Feb 2026", 38));

        // Registrar usuarios
        biblioteca.registrarUsuario(new Usuario(1, "Carlos García", "carlos@mail.com"));
        biblioteca.registrarUsuario(new Usuario(2, "María López", "maria@mail.com"));

        // Mostrar catálogo
        biblioteca.mostrarCatalogo();

        // Realizar préstamos
        System.out.println("\n── Préstamos ──");
        biblioteca.prestar("978-0132350884", 1);
        biblioteca.prestar("978-0201633610", 1);
        biblioteca.prestar("978-0134685991", 2);

        // Intentar prestar algo ya prestado
        biblioteca.prestar("978-0132350884", 2);

        // Mostrar préstamos activos
        biblioteca.mostrarPrestamos();

        // Devolver
        System.out.println("\n── Devoluciones ──");
        biblioteca.devolver("978-0132350884", 1);

        // Buscar
        System.out.println("\n── Búsqueda ──");
        biblioteca.buscar("Java");

        // Estadísticas
        biblioteca.estadisticas();
    }
}
