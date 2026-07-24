# Mini-Proyecto 18: Generar PDF (Facturas/Reportes)

## Qué aprenderás
- Generar PDF desde Java (OpenPDF / iText)
- Crear tablas, imágenes y texto formateado
- Devolver PDF como descarga HTTP
- Generar ticket de venta
- Generar reporte de inventario

## Dependencia (pom.xml)
```xml
<dependency>
    <groupId>com.github.librepdf</groupId>
    <artifactId>openpdf</artifactId>
    <version>2.0.2</version>
</dependency>
```

## Endpoint
```
GET /api/reportes/productos/pdf
→ Descarga: productos_2026-07-23.pdf

GET /api/ventas/{id}/ticket
→ Descarga: ticket_V-001.pdf
```

## Código para generar PDF
```java
@GetMapping("/productos/pdf")
public void generarReportePDF(HttpServletResponse response) throws Exception {
    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=productos.pdf");

    Document document = new Document(PageSize.LETTER);
    PdfWriter.getInstance(document, response.getOutputStream());
    document.open();

    // Título
    Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
    document.add(new Paragraph("Reporte de Productos", titleFont));
    document.add(new Paragraph("Fecha: " + LocalDate.now()));
    document.add(new Paragraph(" "));

    // Tabla
    PdfPTable table = new PdfPTable(4); // 4 columnas
    table.setWidthPercentage(100);
    table.addCell("Nombre"); table.addCell("Precio");
    table.addCell("Stock"); table.addCell("Categoría");

    for (Producto p : productoService.listarTodos()) {
        table.addCell(p.getNombre());
        table.addCell("$" + p.getPrecio());
        table.addCell(String.valueOf(p.getExistencia()));
        table.addCell(p.getCategoriaNombre());
    }

    document.add(table);
    document.close();
}
```

## Probar
```bash
# Descargar PDF
curl http://localhost:8080/api/reportes/productos/pdf -o reporte.pdf

# O abrir en navegador:
http://localhost:8080/api/reportes/productos/pdf
→ Se descarga automáticamente
```
