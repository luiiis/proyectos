# Módulo 10: Manejo de Fechas (java.time)

```java
// Fecha sin hora
LocalDate hoy = LocalDate.now();                    // 2026-05-30
LocalDate navidad = LocalDate.of(2026, 12, 25);
LocalDate parsed = LocalDate.parse("2026-01-15");

// Fecha con hora
LocalDateTime ahora = LocalDateTime.now();          // 2026-05-30T14:30:00
ZonedDateTime conZona = ZonedDateTime.now(ZoneId.of("America/Mexico_City"));

// Operaciones
LocalDate manana = hoy.plusDays(1);
LocalDate mesAnterior = hoy.minusMonths(1);
long diasEntre = ChronoUnit.DAYS.between(hoy, navidad);

// Duration (horas, minutos, segundos)
Duration duracion = Duration.between(inicio, fin);
duracion.toHours();

// Period (años, meses, días)
Period antiguedad = Period.between(fechaIngreso, LocalDate.now());
System.out.println(antiguedad.getYears() + " años");

// Formatear
DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
String formateado = ahora.format(fmt);  // "30/05/2026 14:30"
```

## Ejercicios
1. Calcula la edad exacta de una persona (años, meses, días)
2. Determina cuántos días laborales hay entre dos fechas
3. Genera un reporte de ventas agrupado por semana
