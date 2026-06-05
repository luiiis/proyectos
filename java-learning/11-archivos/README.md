# Módulo 11: Manejo de Archivos (I/O y NIO)

```java
// Leer archivo completo
String contenido = Files.readString(Path.of("datos.txt"));
List<String> lineas = Files.readAllLines(Path.of("datos.csv"));

// Escribir archivo
Files.writeString(Path.of("salida.txt"), "Hola mundo");
Files.write(Path.of("lista.txt"), lineas);

// Leer CSV línea por línea (eficiente para archivos grandes)
try (BufferedReader reader = Files.newBufferedReader(Path.of("ventas.csv"))) {
    reader.lines()
        .skip(1)  // Saltar header
        .map(line -> line.split(","))
        .forEach(cols -> System.out.println(cols[0] + ": $" + cols[3]));
}

// Crear directorios
Files.createDirectories(Path.of("reportes/2026/mayo"));

// Listar archivos
Files.list(Path.of(".")).filter(Files::isRegularFile).forEach(System.out::println);
```

## Ejercicios
1. Lee un CSV de productos y carga en una List<Producto>
2. Genera un reporte de ventas en formato CSV
3. Implementa un sistema de logs que escriba en archivo con rotación diaria
