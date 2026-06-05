# Proyecto 06: API REST de Clientes con Spring Boot

## ¿Qué problema resuelve?
Construir una API REST profesional con Spring Boot. CRUD completo de clientes con validaciones, manejo centralizado de errores, documentación Swagger y buenas prácticas de diseño de APIs.

## Tecnologías
- Java 21
- Spring Boot 3.2
- Spring Web (REST controllers)
- Spring Validation (Bean Validation)
- Swagger/OpenAPI 3.0 (documentación)
- Maven (gestión de dependencias)
- H2 Database (desarrollo)
- Lombok (reducir boilerplate)

## Funcionalidades
- CRUD completo: GET, POST, PUT, DELETE
- Validaciones con @Valid y anotaciones
- Manejo global de excepciones (@ControllerAdvice)
- Respuestas estandarizadas (ApiResponse)
- Documentación interactiva con Swagger UI
- DTOs para separar capas
- Códigos HTTP correctos (200, 201, 400, 404, 500)
- Búsqueda por nombre y email

## Endpoints
- `GET /api/clientes` - Listar todos
- `GET /api/clientes/{id}` - Obtener por ID
- `POST /api/clientes` - Crear cliente
- `PUT /api/clientes/{id}` - Actualizar
- `DELETE /api/clientes/{id}` - Eliminar
- `GET /api/clientes/buscar?nombre=X` - Buscar

## Conceptos Clave
- Arquitectura en capas (Controller → Service → Repository)
- Validaciones declarativas con Bean Validation
- Manejo de errores centralizado y consistente
- Documentación automática de API con OpenAPI
