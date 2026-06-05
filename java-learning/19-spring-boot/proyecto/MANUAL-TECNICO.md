# Módulo 19: Spring Boot - Manual Técnico

## ¿Qué construimos?
Una aplicación Spring Boot funcional desde cero que demuestra:
- Auto-configuración (Spring configura todo automáticamente)
- Profiles (dev vs prod)
- Endpoints REST básicos
- Conexión a PostgreSQL
- Swagger/OpenAPI

## Cómo se construyó paso a paso

### Paso 1: Crear proyecto con Spring Initializr
```bash
# Opción A: Web (https://start.spring.io)
# Opción B: CLI
curl https://start.spring.io/starter.zip \
  -d dependencies=web,data-jpa,postgresql,validation \
  -d type=maven-project \
  -d language=java \
  -d javaVersion=21 \
  -d name=mi-app \
  -o mi-app.zip
```

### Paso 2: Estructura generada
```
src/main/java/com/ejemplo/
├── MiAppApplication.java    ← @SpringBootApplication (punto de entrada)
└── (vacío, tú creas el resto)

src/main/resources/
├── application.yml          ← Configuración
└── (vacío)
```

### Paso 3: Agregar capas (Controller → Service → Repository)
Cada capa tiene una responsabilidad:
- Controller: recibe HTTP, valida, delega
- Service: lógica de negocio
- Repository: acceso a datos

### Paso 4: Configurar BD y ejecutar
```bash
# Asegúrate de tener PostgreSQL corriendo (Docker)
docker run -d --name pg-springboot -p 5432:5432 \
  -e POSTGRES_DB=springboot_db -e POSTGRES_PASSWORD=postgres123 \
  postgres:16-alpine

# Ejecutar la app
cd proyecto/app
mvn spring-boot:run
# → http://localhost:8080
# → http://localhost:8080/swagger-ui.html
```

## Flujo de una petición HTTP

```
GET http://localhost:8080/api/saludo?nombre=Carlos

1. Tomcat (embebido) recibe la petición en puerto 8080
2. DispatcherServlet busca qué Controller maneja /api/saludo
3. SaludoController.saludar("Carlos") se ejecuta
4. Devuelve: {"mensaje": "¡Hola, Carlos!", "fecha": "2026-05-30"}
5. Jackson serializa el objeto Java → JSON
6. Tomcat envía la respuesta HTTP 200 con el JSON
```
