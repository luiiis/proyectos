import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * MÓDULO 10: Fechas - Demo ejecutable
 * Ejecutar: javac FechasDemo.java && java FechasDemo
 */
public class FechasDemo {
    public static void main(String[] args) {
        System.out.println("═══ MÓDULO 10: java.time ═══\n");

        LocalDate hoy = LocalDate.now();
        LocalDateTime ahora = LocalDateTime.now();
        System.out.println("Hoy: " + hoy);
        System.out.println("Ahora: " + ahora);

        // Operaciones
        LocalDate navidad = LocalDate.of(2026, 12, 25);
        long diasParaNavidad = ChronoUnit.DAYS.between(hoy, navidad);
        System.out.println("\nDías para Navidad: " + diasParaNavidad);

        // Antigüedad
        LocalDate ingreso = LocalDate.of(2020, 3, 15);
        Period antiguedad = Period.between(ingreso, hoy);
        System.out.printf("Antigüedad: %d años, %d meses, %d días%n",
            antiguedad.getYears(), antiguedad.getMonths(), antiguedad.getDays());

        // Formatear
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        System.out.println("\nFormateado: " + ahora.format(fmt));

        // Zonas horarias
        ZonedDateTime mexico = ZonedDateTime.now(ZoneId.of("America/Mexico_City"));
        ZonedDateTime tokio = ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
        System.out.println("\nMéxico: " + mexico.format(fmt));
        System.out.println("Tokio:  " + tokio.format(fmt));
    }
}
