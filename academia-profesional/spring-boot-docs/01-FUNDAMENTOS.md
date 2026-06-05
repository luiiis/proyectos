# Spring Boot Completo - Parte 1: Fundamentos

## Que es Spring Boot

Spring Boot es un framework que SIMPLIFICA la creacion de aplicaciones Java.
Sin Spring Boot necesitarias configurar manualmente: servidor web, conexion a BD,
seguridad, serializacion JSON, inyeccion de dependencias, etc.
Spring Boot lo hace AUTOMATICAMENTE.

## Que problema resuelve

```
Sin Spring Boot (Java puro):
  - Descargar Tomcat, configurarlo, desplegarlo
  - Configurar JDBC manualmente (URL, driver, pool)
  - Escribir SQL a mano para cada operacion
  - Manejar sesiones HTTP manualmente
  - Configurar CORS, headers, content-type
  - 500+ lineas de configuracion XML
  RESULTADO: 2 semanas para tener un "Hola Mundo" funcional

Con Spring Boot:
  - Tomcat INCLUIDO (embedded)
  - Conexion a BD con 3 lineas en application.yml
  - JPA genera SQL automaticamente
  - Security se configura con 1 clase
  - Todo funciona con anotaciones (@RestController, @Service, etc.)
  RESULTADO: 5 minutos para tener un API REST funcionando
```

## Como funciona internamente

```
1. Tu escribes: @SpringBootApplication en la clase principal
2. Spring escanea TODOS los paquetes buscando anotaciones:
   @Component, @Service, @Repository, @Controller, @Configuration
3. Para cada clase anotada, Spring CREA una instancia (Bean)
4. Spring INYECTA las dependencias automaticamente (DI)
5. Spring configura TODO segun las dependencias del pom.xml:
   - Tienes spring-boot-starter-web? → configura Tomcat + Jackson
   - Tienes spring-boot-starter-data-jpa? → configura Hibernate + DataSource
   - Tienes spring-boot-starter-security? → configura filtros de seguridad
6. Tomcat arranca en puerto 8080 → listo para recibir peticiones
```

## Crear un proyecto desde cero

```bash
# Opcion 1: Spring Initializr (web)
# Ir a https://start.spring.io
# Seleccionar: Maven, Java 21, Spring Boot 3.3
# Agregar dependencias: Web, JPA, PostgreSQL, Validation, Security
# Descargar ZIP → descomprimir → abrir en IDE

# Opcion 2: Desde terminal
curl https://start.spring.io/starter.zip \
  -d dependencies=web,data-jpa,postgresql,validation,security \
  -d type=maven-project -d language=java -d javaVersion=21 \
  -d name=mi-api -o mi-api.zip
unzip mi-api.zip
```

## Estructura del proyecto

```
mi-api/
├── pom.xml                          ← Dependencias (Maven)
├── src/main/
│   ├── java/com/empresa/miapi/
│   │   ├── MiApiApplication.java   ← Punto de entrada (@SpringBootApplication)
│   │   ├── config/                  ← Configuracion (Security, CORS, etc.)
│   │   ├── controller/             ← Endpoints REST (@RestController)
│   │   ├── service/                ← Logica de negocio (@Service)
│   │   ├── repository/             ← Acceso a datos (@Repository)
│   │   ├── entity/                 ← Mapeo BD (@Entity)
│   │   ├── dto/                    ← Objetos de transferencia (Records)
│   │   └── exception/              ← Manejo de errores
│   └── resources/
│       └── application.yml          ← Configuracion (BD, puerto, JWT, etc.)
└── src/test/                        ← Tests
```

## application.yml explicado

```yaml
server:
  port: 8080                    # Puerto donde escucha (default: 8080)

spring:
  application:
    name: mi-api                # Nombre de la app (para logs y discovery)

  datasource:
    url: jdbc:postgresql://localhost:5432/mi_bd    # URL de conexion a BD
    username: postgres                              # Usuario BD
    password: ${DB_PASSWORD:postgres123}            # Password (variable de entorno o default)
    # ${VARIABLE:default} = lee variable de entorno, si no existe usa el default
    
  jpa:
    hibernate:
      ddl-auto: validate        # validate=no modifica BD, update=crea/modifica tablas
    show-sql: true              # Muestra SQL generado en consola (solo desarrollo)
    properties:
      hibernate:
        format_sql: true        # SQL formateado (legible)
        dialect: org.hibernate.dialect.PostgreSQLDialect

  # Profiles: diferentes configs para diferentes ambientes
  profiles:
    active: ${SPRING_PROFILES_ACTIVE:dev}   # dev, prod, test
```

## Anotaciones fundamentales

```java
// CLASE PRINCIPAL
@SpringBootApplication  // = @Configuration + @EnableAutoConfiguration + @ComponentScan
public class Application { public static void main(String[] args) { SpringApplication.run(Application.class, args); } }

// COMPONENTES (Spring los gestiona como Singleton)
@Component      // Generico: "Spring, gestiona esta clase"
@Service        // Semantico: logica de negocio
@Repository     // Semantico: acceso a datos (traduce excepciones SQL)
@Controller     // Semantico: endpoints web (devuelve vistas HTML)
@RestController // = @Controller + @ResponseBody (devuelve JSON)
@Configuration  // Clase de configuracion (define @Beans)

// INYECCION DE DEPENDENCIAS
@Autowired      // "Spring, inyecta aqui" (preferir constructor injection)
@Qualifier("nombre")  // Cuando hay multiples implementaciones

// CONFIGURACION
@Value("${jwt.secret}")  // Inyectar valor de application.yml
@ConfigurationProperties(prefix = "app")  // Mapear grupo de propiedades a clase

// WEB
@RequestMapping("/api/productos")  // Prefijo de URL
@GetMapping("/{id}")               // GET /api/productos/5
@PostMapping                       // POST /api/productos
@PutMapping("/{id}")               // PUT /api/productos/5
@DeleteMapping("/{id}")            // DELETE /api/productos/5
@PathVariable                      // Extrae {id} de la URL
@RequestParam                      // Extrae ?nombre=valor de la URL
@RequestBody                       // Deserializa JSON del body a objeto Java
@ResponseStatus(HttpStatus.CREATED) // Cambia el status code de respuesta

// VALIDACION
@Valid          // Activa validacion en el parametro
@NotBlank       // No puede ser null ni vacio
@NotNull        // No puede ser null
@Size(min=3, max=50)  // Longitud
@Email          // Formato email
@Min(0)         // Valor minimo
@DecimalMin("0.01")   // Decimal minimo

// JPA
@Entity         // "Esta clase mapea a una tabla"
@Table(name = "productos")  // Nombre de la tabla
@Id             // Primary Key
@GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment
@Column(nullable = false, unique = true, length = 100)  // Restricciones
@ManyToOne      // Relacion N:1
@OneToMany      // Relacion 1:N
@ManyToMany     // Relacion M:N
@Transactional  // Todo el metodo es una transaccion (rollback si falla)

// CACHE
@Cacheable("productos")           // Guarda resultado en cache
@CacheEvict(value="productos", allEntries=true)  // Borra cache

// SEGURIDAD
@PreAuthorize("hasRole('ADMIN')")  // Solo ADMIN puede ejecutar este metodo
@PreAuthorize("hasAnyRole('ADMIN','GERENTE')")  // ADMIN o GERENTE
```

---

## Ejecutar

```bash
# Desarrollo
mvn spring-boot:run

# Compilar JAR
mvn package -DskipTests

# Ejecutar JAR
java -jar target/mi-api-1.0.0.jar

# Con profile de produccion
java -jar target/mi-api-1.0.0.jar --spring.profiles.active=prod
```
