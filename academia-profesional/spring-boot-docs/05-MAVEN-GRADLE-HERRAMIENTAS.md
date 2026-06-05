# Spring Boot Completo - Parte 5: Maven, Gradle y Herramientas Backend

---

# 1. MAVEN - Gestor de Dependencias y Build

## Que es Maven

Maven es la herramienta que:
- Descarga librerias externas (dependencias) automaticamente
- Compila tu codigo Java
- Ejecuta tests
- Empaqueta tu app en un .jar ejecutable
- Gestiona el ciclo de vida del proyecto

## Analogia

```
Maven es como un chef que:
- Tiene una RECETA (pom.xml) con los ingredientes necesarios
- Va al SUPERMERCADO (Maven Central) a comprar lo que falta
- COCINA (compila) siguiendo los pasos en orden
- EMPAQUETA el plato final (genera .jar)
```

## pom.xml explicado al 100%

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>

    <!-- PARENT: hereda configuracion de Spring Boot -->
    <!-- Sin esto, tendrias que especificar la version de CADA libreria -->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.0</version>
    </parent>

    <!-- COORDENADAS: identifican tu proyecto de forma unica en el mundo -->
    <groupId>com.empresa</groupId>       <!-- Tu organizacion (dominio invertido) -->
    <artifactId>mi-api</artifactId>      <!-- Nombre del proyecto -->
    <version>1.0.0</version>             <!-- Version actual -->
    <!-- Estas 3 juntas = "direccion" unica de tu proyecto -->

    <!-- PROPIEDADES: variables reutilizables -->
    <properties>
        <java.version>21</java.version>  <!-- Version de Java a compilar -->
    </properties>

    <!-- DEPENDENCIAS: librerias que tu proyecto NECESITA -->
    <dependencies>

        <!-- spring-boot-starter-web: incluye Tomcat + Jackson + Spring MVC -->
        <!-- Con esto ya puedes crear endpoints REST que devuelven JSON -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <!-- No necesitas <version> porque el parent ya la define -->
        </dependency>

        <!-- spring-boot-starter-data-jpa: incluye Hibernate + Spring Data -->
        <!-- Con esto mapeas clases Java a tablas de BD sin escribir SQL -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <!-- Driver PostgreSQL: el "traductor" entre Java y PostgreSQL -->
        <!-- runtime = solo se necesita al EJECUTAR (no al compilar) -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- Validacion: @NotBlank, @Email, @Size, etc. -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <!-- Security + JWT -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>0.12.5</version>
        </dependency>

        <!-- Swagger: documentacion automatica de la API -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.5.0</version>
        </dependency>

        <!-- Redis: cache -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>

        <!-- Actuator: health checks y metricas -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <!-- Prometheus: exportar metricas -->
        <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-registry-prometheus</artifactId>
        </dependency>

        <!-- TEST: solo se usa en tests (no se empaqueta en el .jar final) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <!-- BUILD: como compilar y empaquetar -->
    <build>
        <plugins>
            <!-- Plugin de Spring Boot: genera .jar ejecutable con Tomcat incluido -->
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

    <!-- PROFILES: diferentes configuraciones para diferentes ambientes -->
    <profiles>
        <profile>
            <id>prod</id>
            <properties>
                <spring.profiles.active>prod</spring.profiles.active>
            </properties>
        </profile>
    </profiles>
</project>
```

## Comandos Maven (los que usaras TODOS los dias)

```bash
# COMPILAR (solo compila, no ejecuta tests)
mvn compile

# EJECUTAR TESTS
mvn test

# EMPAQUETAR (compila + tests + genera .jar)
mvn package
# Resultado: target/mi-api-1.0.0.jar

# EMPAQUETAR sin tests (mas rapido para desarrollo)
mvn package -DskipTests

# EJECUTAR la app Spring Boot (desarrollo)
mvn spring-boot:run

# LIMPIAR (borrar carpeta target/)
mvn clean

# LIMPIAR + EMPAQUETAR (lo mas comun)
mvn clean package -DskipTests

# INSTALAR en repositorio local (~/.m2/repository)
mvn install

# VER arbol de dependencias (debug de conflictos)
mvn dependency:tree

# DESCARGAR dependencias sin compilar (para Docker cache)
mvn dependency:go-offline
```

## Ciclo de vida Maven (en orden)

```
validate → compile → test → package → verify → install → deploy

Cuando ejecutas "mvn package", Maven ejecuta TODOS los pasos anteriores:
  1. validate: verifica que el pom.xml es correcto
  2. compile: compila src/main/java → target/classes
  3. test: ejecuta src/test/java
  4. package: empaqueta en .jar

Cuando ejecutas "mvn test", ejecuta: validate → compile → test (se detiene ahi)
```

## Scopes de dependencias

```xml
<!-- compile (default): disponible en compilacion, test y runtime -->
<scope>compile</scope>

<!-- runtime: NO disponible en compilacion, SI en ejecucion -->
<!-- Ejemplo: driver de BD (tu codigo no lo importa directamente) -->
<scope>runtime</scope>

<!-- test: SOLO disponible en tests (no se empaqueta en el .jar) -->
<scope>test</scope>

<!-- provided: disponible en compilacion pero NO se empaqueta -->
<!-- Ejemplo: servlet-api (el servidor ya la tiene) -->
<scope>provided</scope>
```

---

# 2. GRADLE - Alternativa Moderna a Maven

## Que es Gradle

Gradle hace lo MISMO que Maven pero:
- Usa Kotlin/Groovy en lugar de XML (mas conciso)
- Es mas RAPIDO (compilacion incremental + cache)
- Es mas FLEXIBLE (puedes escribir logica custom)

## build.gradle.kts (Kotlin DSL)

```kotlin
plugins {
    java
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
}

group = "com.empresa"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()  // De donde descargar dependencias
}

dependencies {
    // Equivalente al pom.xml de arriba
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    
    runtimeOnly("org.postgresql:postgresql")
    
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
```

## Comandos Gradle

```bash
./gradlew build          # Compilar + tests + empaquetar
./gradlew bootRun        # Ejecutar Spring Boot
./gradlew test           # Solo tests
./gradlew clean build    # Limpiar + construir
./gradlew dependencies   # Ver arbol de dependencias
./gradlew bootJar        # Generar .jar ejecutable
```

## Maven vs Gradle: Cuando usar cada uno

| Aspecto | Maven | Gradle |
|---------|-------|--------|
| Configuracion | XML (verbose pero explicito) | Kotlin/Groovy (conciso) |
| Velocidad | Lento (recompila todo) | Rapido (incremental + cache) |
| Curva aprendizaje | Mas facil (estructura fija) | Mas complejo (muy flexible) |
| Ecosistema | Mas plugins disponibles | Creciendo rapido |
| Uso enterprise | Dominante (legacy + nuevo) | Creciendo (Android, nuevos) |
| Documentacion | Abundante | Buena pero menos |
| **Recomendacion** | **Proyecto enterprise existente** | **Proyecto nuevo, Android** |

---

# 3. HERRAMIENTAS ESENCIALES PARA BACKEND

## IDE (Entorno de Desarrollo)

### IntelliJ IDEA (RECOMENDADO para Java)
```
Por que IntelliJ:
- Autocompletado INTELIGENTE (entiende Spring, JPA, etc.)
- Refactoring poderoso (renombrar, extraer metodo, mover clase)
- Debugger visual (breakpoints, step-by-step, evaluar expresiones)
- Integracion con Git, Docker, BD, Maven/Gradle
- Detecta errores ANTES de compilar

Atajos esenciales:
  Ctrl+Shift+A    → Buscar cualquier accion
  Ctrl+N          → Buscar clase por nombre
  Ctrl+Shift+N    → Buscar archivo por nombre
  Alt+Enter       → Quick fix (sugerencias)
  Ctrl+Alt+L      → Formatear codigo
  Shift+F10       → Ejecutar
  Shift+F9        → Debug
  Ctrl+Shift+F10  → Ejecutar test actual
```

### VS Code (alternativa ligera)
```
Extensiones necesarias para Java:
- Extension Pack for Java (Microsoft)
- Spring Boot Extension Pack
- Lombok Annotations Support
- Docker
- GitLens
```

## Herramientas de Base de Datos

### DBeaver (RECOMENDADO - universal)
```
Conecta a: PostgreSQL, MySQL, Oracle, SQL Server, MongoDB, Redis, y 50+ mas
Gratis (Community Edition)
Features: editor SQL, diagrama ER, exportar datos, comparar schemas

Conexion PostgreSQL:
  Host: localhost
  Port: 5432
  Database: mi_bd
  Username: postgres
  Password: postgres123
```

### pgAdmin (solo PostgreSQL)
```
GUI web oficial de PostgreSQL
Se levanta con Docker: http://localhost:5050
Bueno para: administracion, backups, monitoreo
```

## Herramientas de API

### Postman
```
Para que: probar endpoints REST sin frontend
Features:
- Guardar colecciones de requests
- Variables de entorno (dev, prod)
- Tests automaticos en cada request
- Generar documentacion

Alternativas: Insomnia, HTTPie, curl
```

### Swagger UI (incluido en Spring Boot)
```
Se genera AUTOMATICAMENTE al agregar springdoc-openapi
URL: http://localhost:8080/swagger-ui.html
Features:
- Documentacion interactiva de TODOS los endpoints
- Probar endpoints directamente desde el navegador
- Ver schemas de request/response
- Autenticacion con JWT (boton "Authorize")
```

## Herramientas de Monitoreo

### Spring Boot Actuator
```yaml
# application.yml
management:
  endpoints:
    web:
      exposure:
        include: health,info,prometheus,metrics

# Endpoints disponibles:
# /actuator/health     → Estado de la app (UP/DOWN)
# /actuator/info       → Informacion de la app
# /actuator/prometheus → Metricas para Prometheus
# /actuator/metrics    → Metricas internas
```

### Prometheus + Grafana
```
Prometheus: recolecta metricas cada 15 segundos
Grafana: visualiza metricas en dashboards bonitos

Metricas que Spring expone automaticamente:
- http_server_requests_seconds (latencia por endpoint)
- jvm_memory_used_bytes (memoria)
- hikaricp_connections_active (conexiones BD)
- process_cpu_usage (CPU)
```

## Herramientas de Logging

### SLF4J + Logback (incluido en Spring Boot)
```java
// En tu codigo:
private static final Logger log = LoggerFactory.getLogger(MiService.class);

log.info("Producto creado: id={}, nombre={}", producto.getId(), producto.getNombre());
log.warn("Stock bajo: producto={}, stock={}", producto.getNombre(), producto.getStock());
log.error("Error procesando venta", exception);

// Niveles (de menos a mas grave):
// TRACE → DEBUG → INFO → WARN → ERROR

// application.yml:
logging:
  level:
    com.empresa: DEBUG        # Tu codigo: ver DEBUG y superiores
    org.springframework: INFO  # Spring: solo INFO y superiores
    org.hibernate.SQL: DEBUG   # Ver SQL generado por Hibernate
  pattern:
    console: "%d{HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

## Herramientas de Testing

### JUnit 5 + Mockito
```java
// Test unitario (prueba UNA clase aislada)
@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock ProductoRepository repo;  // Mock: objeto falso
    @InjectMocks ProductoService service;  // Clase real con mocks inyectados

    @Test
    @DisplayName("Crear producto con datos validos")
    void crear_datosValidos_retornaProducto() {
        // ARRANGE (preparar)
        var request = new ProductoRequest("Laptop", null, BigDecimal.valueOf(999), 10, null, null);
        var entity = new Producto(); entity.setNombre("Laptop");
        when(repo.save(any())).thenReturn(entity);

        // ACT (ejecutar)
        var resultado = service.crear(request);

        // ASSERT (verificar)
        assertThat(resultado.nombre()).isEqualTo("Laptop");
        verify(repo).save(any());  // Verificar que se llamo a save()
    }
}
```

### Testcontainers (BD real en tests)
```java
// Test de integracion con PostgreSQL REAL (en Docker)
@SpringBootTest
@Testcontainers
class ProductoRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void config(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
    }

    @Autowired ProductoRepository repo;

    @Test
    void findByActivoTrue_retornaSoloActivos() {
        var resultado = repo.findByActivoTrue();
        assertThat(resultado).allMatch(Producto::isActivo);
    }
}
```

## Git (Control de Versiones)

```bash
# Flujo diario de trabajo:
git checkout -b feature/crear-productos   # Crear rama
# ... escribir codigo ...
git add .                                  # Agregar cambios
git commit -m "feat: CRUD de productos"    # Guardar con mensaje
git push -u origin feature/crear-productos # Subir al servidor
# Crear Pull Request en GitHub/GitLab
# Code Review por otro dev
# Merge a main

# Conventional Commits (estandar de mensajes):
git commit -m "feat: agregar endpoint de ventas"      # Nueva feature
git commit -m "fix: corregir calculo de IVA"          # Bug fix
git commit -m "refactor: extraer logica a service"    # Refactoring
git commit -m "docs: documentar API de productos"     # Documentacion
git commit -m "test: agregar tests de VentaService"   # Tests
git commit -m "chore: actualizar dependencias"        # Mantenimiento
```

## Docker (para desarrollo)

```bash
# Levantar BD para desarrollo (sin instalar PostgreSQL)
docker run -d --name mi-postgres \
  -e POSTGRES_DB=mi_bd \
  -e POSTGRES_PASSWORD=postgres123 \
  -p 5432:5432 \
  postgres:16-alpine

# Levantar Redis para cache
docker run -d --name mi-redis -p 6379:6379 redis:7-alpine

# Ver contenedores corriendo
docker ps

# Ver logs
docker logs -f mi-postgres

# Detener
docker stop mi-postgres mi-redis

# Eliminar
docker rm mi-postgres mi-redis
```

---

# 4. RESUMEN: Stack Completo de Herramientas Backend

```
DESARROLLO:
  IDE: IntelliJ IDEA (o VS Code)
  Build: Maven (enterprise) o Gradle (nuevo)
  Java: 21+ (LTS)
  Framework: Spring Boot 3.3

BASE DE DATOS:
  BD: PostgreSQL 16
  GUI: DBeaver
  Migraciones: Flyway (produccion) o ddl-auto (desarrollo)

TESTING:
  Unit: JUnit 5 + Mockito
  Integration: Testcontainers
  API: Postman o Swagger UI

INFRAESTRUCTURA:
  Contenedores: Docker + Docker Compose
  Cache: Redis
  Mensajeria: Kafka (si necesitas eventos)

CALIDAD:
  Logging: SLF4J + Logback
  Metricas: Actuator + Prometheus + Grafana
  CI/CD: GitHub Actions

COLABORACION:
  Versionado: Git + GitHub/GitLab
  Documentacion: Swagger/OpenAPI
  Code Review: Pull Requests
```
