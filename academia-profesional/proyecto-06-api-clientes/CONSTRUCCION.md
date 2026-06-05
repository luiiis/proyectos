# Bitácora de Construcción - Proyecto 06: API Clientes

## Paso 1: ¿Qué problema resuelve?
Exponer datos de clientes como API REST para que cualquier frontend (Angular, React, móvil) pueda consumirlos.

## Paso 2: Decisiones técnicas

| Decisión | Razón | Alternativa |
|----------|-------|-------------|
| Spring Boot 3.3 | Estándar enterprise Java | Quarkus (más rápido arranque) |
| Records para DTOs | Inmutables, concisos | Clases + Lombok (más código) |
| Almacenamiento en memoria | Simplicidad para aprender REST | BD (se agrega en proyecto 07) |
| Validación con @Valid | Estándar Jakarta | Validación manual (más código) |
| GlobalExceptionHandler | Respuestas de error consistentes | Try/catch en cada controller |

## Paso 3: Estructura de capas
```
Controller (recibe HTTP) → Service (lógica) → Model (datos)
     ↓                         ↓
  DTOs (Request/Response)   Almacenamiento
```

## Paso 4: ¿Qué aprendí?
- Spring Boot auto-configura Tomcat, Jackson, validación
- Records son perfectos para DTOs (inmutables, sin boilerplate)
- @RestControllerAdvice centraliza el manejo de errores
- @Valid + anotaciones de Jakarta validan automáticamente
- ConcurrentHashMap es thread-safe para almacenamiento en memoria

## Paso 5: Siguiente paso
Proyecto 07 agrega PostgreSQL con JPA (reemplaza el Map en memoria por BD real).
