# Mini-Proyecto 20: Importar CSV/Excel a Base de Datos

## Qué aprenderás
- Leer archivos CSV con Java
- Leer archivos Excel (.xlsx) con Apache POI
- Validar cada fila (formato, datos requeridos, duplicados)
- Insertar masivamente en la BD
- Reportar errores fila por fila (sin detener la importación)

## Dependencia (pom.xml)
```xml
<!-- Apache POI para leer Excel -->
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.2.5</version>
</dependency>
```

## Endpoint
```
POST /api/importar/productos
Content-Type: multipart/form-data
Field: file (archivo .csv o .xlsx)

Response:
{
  "success": true,
  "importados": 95,
  "errores": 5,
  "detalleErrores": [
    {"fila": 12, "error": "Precio inválido: 'abc'"},
    {"fila": 23, "error": "SKU duplicado: LAP-001"},
    {"fila": 45, "error": "Nombre vacío"}
  ]
}
```

## Formato esperado del CSV
```csv
nombre,precio,existencia,categoria,sku
Laptop HP,18999.00,25,Electrónica,LAP-HP-001
Mouse MX,1899.00,50,Periféricos,MOU-LOG-001
Monitor Dell,12499.00,15,Electrónica,MON-DELL-001
```

## Código clave (leer CSV)
```java
@PostMapping("/productos")
public ResponseEntity<?> importarCSV(@RequestParam("file") MultipartFile file) {
    List<Map<String, Object>> errores = new ArrayList<>();
    int importados = 0;

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
        String header = reader.readLine(); // Saltar encabezado
        String linea;
        int fila = 1;

        while ((linea = reader.readLine()) != null) {
            fila++;
            try {
                String[] campos = linea.split(",");
                // Validar y crear producto...
                productoService.crear(producto);
                importados++;
            } catch (Exception e) {
                errores.add(Map.of("fila", fila, "error", e.getMessage()));
            }
        }
    }

    return ResponseEntity.ok(Map.of(
        "importados", importados,
        "errores", errores.size(),
        "detalleErrores", errores
    ));
}
```

## Probar con curl
```bash
curl -X POST http://localhost:8080/api/importar/productos \
  -F "file=@productos.csv"
```
