# Mini-Proyecto 16: Subir Imágenes (File Upload)

## Qué aprenderás
- Recibir archivos con @RequestParam MultipartFile
- Validar tipo (solo jpg/png) y tamaño (máx 5MB)
- Guardar en disco local (desarrollo)
- Guardar en S3/MinIO (producción)
- Devolver URL para acceder a la imagen
- Servir imágenes estáticas

## Endpoint
```
POST /api/archivos/subir
Content-Type: multipart/form-data
Form fields: file (el archivo), descripcion (texto)

Response:
{
  "success": true,
  "url": "/uploads/productos/abc123.jpg",
  "nombre": "abc123.jpg",
  "tamano": 245000,
  "tipo": "image/jpeg"
}
```

## Código clave
```java
@PostMapping("/subir")
public ResponseEntity<?> subir(@RequestParam("file") MultipartFile file) {
    // Validar tipo
    String tipo = file.getContentType();
    if (!List.of("image/jpeg", "image/png", "image/webp").contains(tipo)) {
        return ResponseEntity.badRequest().body(Map.of("error", "Solo se permiten JPG, PNG o WebP"));
    }

    // Validar tamaño (5MB máximo)
    if (file.getSize() > 5 * 1024 * 1024) {
        return ResponseEntity.badRequest().body(Map.of("error", "Archivo muy grande (máx 5MB)"));
    }

    // Generar nombre único
    String extension = tipo.split("/")[1];
    String nombre = UUID.randomUUID() + "." + extension;

    // Guardar en disco
    Path destino = Paths.get("uploads", nombre);
    Files.createDirectories(destino.getParent());
    file.transferTo(destino.toFile());

    return ResponseEntity.ok(Map.of(
        "url", "/uploads/" + nombre,
        "nombre", nombre,
        "tamano", file.getSize()
    ));
}
```

## Configuración para servir archivos
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
```

## Probar con Postman
```
1. Método: POST
2. URL: http://localhost:8080/api/archivos/subir
3. Body → form-data
4. Key: "file" (tipo: File) → seleccionar imagen
5. Send
6. Respuesta: URL de la imagen subida
7. Abrir URL en navegador: http://localhost:8080/uploads/abc123.jpg
```

## Probar con curl
```bash
curl -X POST http://localhost:8080/api/archivos/subir \
  -F "file=@mi-imagen.jpg"
```
