# Módulo 20: Spring Boot + JPA - Manual Técnico

## ¿Qué construimos?
Una API REST completa en Java que se conecta a PostgreSQL usando Spring Data JPA.
Demuestra cómo una aplicación real interactúa con la base de datos.

## Arquitectura del proyecto

```
src/main/java/com/empresa/
├── Application.java                 ← Punto de entrada
├── domain/
│   ├── entity/
│   │   ├── Producto.java           ← Mapea tabla "productos"
│   │   ├── Cliente.java            ← Mapea tabla "clientes"
│   │   └── Venta.java             ← Mapea tabla "ventas"
│   └── repository/
│       ├── ProductoRepository.java ← Queries automáticas
│       └── VentaRepository.java
├── service/
│   ├── ProductoService.java        ← Lógica de negocio
│   └── VentaService.java
├── controller/
│   ├── ProductoController.java     ← Endpoints REST
│   └── VentaController.java
└── dto/
    ├── ProductoDto.java            ← Lo que recibe/devuelve la API
    └── VentaDto.java
```

## ¿Cómo se conecta Java con la BD?

```
1. Controller recibe: POST /api/productos {nombre: "Laptop", precio: 999}
2. Controller llama a: productoService.crear(dto)
3. Service convierte DTO → Entity
4. Service llama a: productoRepository.save(entity)
5. JPA/Hibernate genera: INSERT INTO productos (nombre, precio) VALUES ('Laptop', 999)
6. PostgreSQL ejecuta el INSERT y devuelve el ID generado
7. JPA devuelve la Entity con el ID
8. Service convierte Entity → DTO
9. Controller responde: HTTP 201 {id: 501, nombre: "Laptop", precio: 999}
```

## Cómo levantar el proyecto

### Prerrequisitos
- Java 21+ instalado
- Maven 3.9+ instalado
- PostgreSQL corriendo en Docker (docker compose up postgres -d)

### Ejecutar
```bash
cd database-learning/20-springboot/proyecto/app
mvn spring-boot:run
# Servidor en: http://localhost:8080
# Swagger: http://localhost:8080/swagger-ui.html
```

### Probar
```bash
# Listar productos
curl http://localhost:8080/api/productos

# Crear producto
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre": "Test desde curl", "precio": 999.99, "stock": 10}'

# Buscar por ID
curl http://localhost:8080/api/productos/1
```
