import java.nio.file.*;
import java.io.IOException;
import java.util.List;

/**
 * MÓDULO 11: Archivos - Demo ejecutable
 * Ejecutar: javac ArchivosDemo.java && java ArchivosDemo
 */
public class ArchivosDemo {
    public static void main(String[] args) throws IOException {
        System.out.println("═══ MÓDULO 11: Archivos (NIO) ═══\n");

        Path archivo = Path.of("demo-productos.csv");

        // Escribir CSV
        List<String> lineas = List.of(
            "id,nombre,precio,stock",
            "1,Laptop HP,18999.99,25",
            "2,Mouse Logitech,899.00,50",
            "3,Monitor Dell,12499.00,15",
            "4,Teclado MX,2899.00,40"
        );
        Files.write(archivo, lineas);
        System.out.println("✓ Archivo escrito: " + archivo.toAbsolutePath());

        // Leer y procesar
        System.out.println("\n── Contenido del CSV ──");
        Files.readAllLines(archivo).stream()
            .skip(1) // Saltar header
            .map(line -> line.split(","))
            .forEach(cols -> System.out.printf("  %s: $%s (stock: %s)%n", cols[1], cols[2], cols[3]));

        // Info del archivo
        System.out.println("\nTamaño: " + Files.size(archivo) + " bytes");
        System.out.println("Existe: " + Files.exists(archivo));

        // Limpiar
        Files.delete(archivo);
        System.out.println("\n✓ Archivo eliminado (limpieza)");
    }
}
