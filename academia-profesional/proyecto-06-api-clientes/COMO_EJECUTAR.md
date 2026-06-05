# Cómo Ejecutar - Proyecto 06: API REST de Clientes

## Requisitos
- Java 21+ instalado (`java -version`)
- Maven 3.9+ instalado (`mvn -version`)

## 1. Compilar y Ejecutar

```bash
cd academia-profesional/proyecto-06-api-clientes

# Compilar
mvn clean compile

# Ejecutar
mvn spring-boot:run
```

La API estará disponible en: `http://localhost:8080`

## 2. Documentación Swagger

Abrir en el navegador: `http://localhost:8080/swagger-ui.html`

## 3. Probar los Endpoints

```bash
# Listar clientes
curl http://localhost:8080/api/clientes

# Crear cliente
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Juan Pérez","email":"juan@email.com","telefono":"555-1234"}'

# Obtener por ID
curl http://localhost:8080/api/clientes/1

# Actualizar
curl -X PUT http://localhost:8080/api/clientes/1 \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Juan P. García","email":"juan.garcia@email.com"}'

# Eliminar
curl -X DELETE http://localhost:8080/api/clientes/1
```

## 4. Ejecutar Tests

```bash
mvn test
```

## Solución de errores

| Error | Causa | Solución |
|-------|-------|----------|
| `Port 8080 already in use` | Puerto ocupado | Cambiar en application.yml o matar proceso |
| `mvn: command not found` | Maven no instalado | Instalar Maven o usar `./mvnw` |
| `Compilation failure` | Error de código | Revisar logs de compilación |
