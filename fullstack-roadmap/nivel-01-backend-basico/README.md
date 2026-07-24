# Nivel 1: Backend Básico — Proyecto 1: API de Saludos

## Objetivo
Crear tu PRIMERA aplicación Spring Boot. Entender cómo nace un proyecto, cómo se ejecuta, y cómo se crean endpoints.

---

## Qué vas a aprender
- Crear un proyecto Maven con Spring Boot
- Entender la estructura de carpetas
- Crear un Controller
- Crear endpoints GET
- Recibir parámetros
- Devolver texto y JSON
- Probar con navegador y Postman
- Entender códigos HTTP

---

## Paso 1: Crear el proyecto

### Opción A — Spring Initializr (web):
```
1. Ir a: https://start.spring.io
2. Configurar:
   - Project: Maven
   - Language: Java
   - Spring Boot: 3.3.x
   - Group: com.softwarelee
   - Artifact: api-saludos
   - Name: api-saludos
   - Package name: com.softwarelee.apisaludos
   - Packaging: Jar
   - Java: 21
3. Dependencies: Spring Web
4. Click "Generate" → descarga un .zip
5. Descomprimir en tu carpeta de trabajo
6. Abrir con IntelliJ IDEA o VS Code
```

### Opción B — Desde terminal:
```cmd
mvn archetype:generate -DgroupId=com.softwarelee -DartifactId=api-saludos
```
(Luego agregar dependencias manualmente en pom.xml)

---

## Paso 2: Entender la estructura

```
api-saludos/
├── pom.xml                              ← Dependencias y configuración
├── src/
│   ├── main/
│   │   ├── java/com/softwarelee/apisaludos/
│   │   │   └── ApiSaludosApplication.java  ← Clase principal
│   │   └── resources/
│   │       └── application.properties      ← Configuración
│   └── test/
│       └── java/com/softwarelee/apisaludos/
│           └── ApiSaludosApplicationTests.java
└── mvnw / mvnw.cmd                      ← Maven wrapper
```

### ¿Qué es cada archivo?

| Archivo | Para qué |
|---------|----------|
| `pom.xml` | Lista de dependencias (como un package.json de Java) |
| `ApiSaludosApplication.java` | El punto de entrada (equivalente a main()) |
| `application.properties` | Configuración (puerto, BD, etc.) |
| `mvnw` | Permite ejecutar Maven sin instalarlo globalmente |

---

## Paso 3: El archivo pom.xml (explicado)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>

    <!-- Spring Boot como "padre" (hereda configuraciones) -->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.0</version>
    </parent>

    <!-- Identificación de TU proyecto -->
    <groupId>com.softwarelee</groupId>
    <artifactId>api-saludos</artifactId>
    <version>1.0.0</version>
    <name>api-saludos</name>
    <description>Mi primera API con Spring Boot</description>

    <properties>
        <java.version>21</java.version>
    </properties>

    <!-- Dependencias (librerías que usas) -->
    <dependencies>
        <!-- Spring Web: incluye Tomcat + JSON + Controllers -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- Para tests -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## Paso 4: La clase principal (ya viene creada)

```java
package com.softwarelee.apisaludos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Esta anotación le dice a Spring: "aquí empieza todo"
@SpringBootApplication
public class ApiSaludosApplication {

    public static void main(String[] args) {
        // Esto arranca el servidor (Tomcat embebido en puerto 8080)
        SpringApplication.run(ApiSaludosApplication.class, args);
    }
}
```

---

## Paso 5: Crear tu primer Controller

Crea el archivo: `src/main/java/com/softwarelee/apisaludos/controller/SaludoController.java`

```java
package com.softwarelee.apisaludos.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * Controller: recibe peticiones HTTP y devuelve respuestas.
 * 
 * @RestController = esta clase maneja requests HTTP y devuelve JSON
 * @RequestMapping = prefijo de la URL para todos los endpoints de esta clase
 */
@RestController
@RequestMapping("/api/saludos")
public class SaludoController {

    /**
     * GET /api/saludos
     * Devuelve un saludo genérico como texto.
     */
    @GetMapping
    public String saludoGeneral() {
        return "¡Hola! Bienvenido a mi primera API";
    }

    /**
     * GET /api/saludos/Luis
     * Devuelve un saludo personalizado.
     * @PathVariable = toma el valor de la URL
     */
    @GetMapping("/{nombre}")
    public String saludoPersonalizado(@PathVariable String nombre) {
        return "¡Hola " + nombre + "! Bienvenido a la API";
    }

    /**
     * GET /api/saludos/json/Maria
     * Devuelve un saludo en formato JSON.
     */
    @GetMapping("/json/{nombre}")
    public Map<String, Object> saludoJson(@PathVariable String nombre) {
        return Map.of(
            "mensaje", "¡Hola " + nombre + "!",
            "status", "success",
            "codigo", 200
        );
    }

    /**
     * GET /api/calculadora/sumar?a=10&b=20
     * Recibe parámetros de la URL (query params).
     * @RequestParam = toma valores de ?clave=valor
     */
    @GetMapping("/calculadora/sumar")
    public Map<String, Object> sumar(@RequestParam int a, @RequestParam int b) {
        int resultado = a + b;
        return Map.of(
            "operacion", a + " + " + b,
            "resultado", resultado
        );
    }

    /**
     * GET /api/saludos/hora
     * Devuelve la fecha y hora actual del servidor.
     */
    @GetMapping("/hora")
    public Map<String, String> horaActual() {
        return Map.of(
            "fecha", java.time.LocalDate.now().toString(),
            "hora", java.time.LocalTime.now().toString(),
            "zona", java.time.ZoneId.systemDefault().toString()
        );
    }
}
```

---

## Paso 6: Ejecutar el proyecto

```cmd
cd api-saludos
mvn spring-boot:run
```

**Debes ver al final:**
```
Started ApiSaludosApplication in X.X seconds
Tomcat started on port 8080
```

---

## Paso 7: Probar los endpoints

### En el navegador:
```
http://localhost:8080/api/saludos
http://localhost:8080/api/saludos/Carlos
http://localhost:8080/api/saludos/json/Maria
http://localhost:8080/api/saludos/calculadora/sumar?a=10&b=20
http://localhost:8080/api/saludos/hora
```

### En Postman:
```
1. Abrir Postman
2. New Request → GET
3. URL: http://localhost:8080/api/saludos/json/Carlos
4. Click "Send"
5. Ver la respuesta JSON abajo
```

---

## Paso 8: Conceptos aprendidos

| Concepto | Qué es | Ejemplo |
|----------|--------|---------|
| Controller | Clase que recibe requests HTTP | `@RestController` |
| Endpoint | Una URL que hace algo | `GET /api/saludos` |
| Request | Lo que el cliente ENVÍA | GET con parámetros |
| Response | Lo que el servidor DEVUELVE | JSON o texto |
| @GetMapping | Endpoint que responde a GET | `@GetMapping("/ruta")` |
| @PathVariable | Dato dentro de la URL | `/saludos/{nombre}` |
| @RequestParam | Dato después del ? | `?a=10&b=20` |
| JSON | Formato de datos universal | `{"clave": "valor"}` |
| Puerto 8080 | Donde "escucha" el servidor | localhost:8080 |
| application.properties | Configuración de la app | `server.port=8080` |

---

## Paso 9: Configurar el puerto (opcional)

En `src/main/resources/application.properties`:
```properties
# Cambiar el puerto (por defecto es 8080)
server.port=8080

# Nombre de la aplicación
spring.application.name=api-saludos
```

---

## Paso 10: Commit en Git

```cmd
cd api-saludos
git init
git add .
git commit -m "feat: proyecto inicial - API de saludos con 5 endpoints"
```

---

## Errores Frecuentes

| Error | Causa | Solución |
|-------|-------|----------|
| Port 8080 already in use | Otro programa usa ese puerto | Cambiar a 8081 en properties |
| Cannot find main class | Estructura de carpetas incorrecta | Verificar package name |
| 404 Not Found | URL incorrecta | Verificar @RequestMapping |
| Whitelabel Error Page | No hay endpoint en esa ruta | Revisar la URL |

---

## ¿Qué sigue?
→ Nivel 2: Proyecto 2 — Administrador de Tareas en memoria (CRUD completo)
