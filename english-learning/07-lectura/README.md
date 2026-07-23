# Módulo 07: Lectura — Entender Documentación Técnica

## Estrategia para Leer en Inglés sin Frustrarte

### Regla de oro: NO traduzcas palabra por palabra.
Lee para entender la IDEA, no cada palabra. Si entiendes el 70%, es suficiente.

---

## 1. Vocabulario de Documentación

| Ves esto | Significa | Ejemplo |
|----------|-----------|---------|
| Prerequisites | Lo que necesitas ANTES | "Prerequisites: Java 21, Docker" |
| Setup / Installation | Cómo instalar | Sigue los pasos |
| Getting Started | Primeros pasos | Tutorial inicial |
| Quick Start | Versión rápida (sin explicaciones) | Para los impacientes |
| Configuration | Cómo configurar | Archivos .yml, .env |
| API Reference | Lista de todos los métodos/endpoints | Consulta cuando necesites |
| Migration Guide | Cómo actualizar de una versión a otra | Cambios breaking |
| Troubleshooting | Solución de problemas comunes | Si algo no funciona |
| FAQ | Preguntas frecuentes | Dudas comunes |
| Changelog | Qué cambió en cada versión | Ver si hay breaking changes |
| Deprecated | Obsoleto (se eliminará pronto) | Buscar alternativa |
| Note / Warning / Caution | Información importante | Leer con atención |

---

## 2. Patrones Comunes en Docs de Spring Boot

### application.yml comentado:
```yaml
server:
  port: 8080                    # The port the app listens on (Puerto)
spring:
  datasource:
    url: jdbc:postgresql://...  # Database connection URL (URL de conexión)
    username: postgres          # Database username (Usuario de BD)
    password: ${DB_PASS}        # From environment variable (Variable de entorno)
  jpa:
    hibernate:
      ddl-auto: validate        # Only validate, don't modify schema (Solo valida)
    show-sql: true              # Print SQL queries to console (Muestra queries)
```

### JavaDoc estándar:
```java
/**
 * Finds all active products in the database.     ← Qué hace
 * Results are cached for 5 minutes.              ← Detalle importante
 *
 * @param category optional filter by category    ← Parámetro (opcional)
 * @return list of active products                ← Qué devuelve
 * @throws DatabaseException if connection fails  ← Qué error puede dar
 * @since 2.0                                     ← Desde qué versión existe
 * @see ProductRepository                         ← Relacionado
 */
```

---

## 3. Práctica: Lee un README Real

Toma cualquier librería que uses y lee su README en GitHub:
1. **Spring Boot**: github.com/spring-projects/spring-boot
2. **Angular**: angular.dev
3. **Docker docs**: docs.docker.com

### Técnica de lectura:
```
1. Lee el TÍTULO y la primera oración → ¿De qué trata?
2. Lee los HEADERS (h2, h3) → ¿Qué secciones tiene?
3. Ve directo a "Getting Started" o "Quick Start"
4. Lee el código de ejemplo → el código es universal
5. Si hay una palabra que no entiendes y es CLAVE → búscala
6. Si no es clave → sigue leyendo, probablemente entiendas por contexto
```

---

## 4. Leer Stack Overflow

### Estructura de una pregunta:
```
TÍTULO: "How to fix NullPointerException in Spring Boot service"
(Cómo arreglar NPE en servicio Spring Boot)

PREGUNTA: Describe el problema + código + error message

RESPUESTAS: Ordenadas por votos (la mejor arriba)
- Primero lee la respuesta con más votos
- Busca el código en la respuesta (es lo más útil)
- Los comentarios a veces tienen la solución real
```

### Palabras clave en SO:
| Ves | Significa |
|-----|-----------|
| "Edit:" | Actualización del autor |
| "Update:" | Lo mismo |
| "Accepted answer" (verde) | El autor confirmó que funciona |
| "This worked for me" | Confirmación de otro usuario |
| "Deprecated" | La solución ya no funciona en versiones nuevas |
| "As of version X" | Aplica solo desde esa versión |
