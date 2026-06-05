# 🎓 Guía de Mentor Senior - Fullstack desde Cero hasta Producción

## Tabla de Contenidos

1. [Visión General de la Arquitectura](#1-visión-general-de-la-arquitectura)
2. [Tecnologías Elegidas y Por Qué](#2-tecnologías-elegidas-y-por-qué)
3. [Módulo 1: Base de Datos](#3-módulo-1-base-de-datos)
4. [Módulo 2: Backend Auth](#4-módulo-2-backend-auth)
5. [Módulo 3: Backend Mail](#5-módulo-3-backend-mail)
6. [Módulo 4: Frontend Angular](#6-módulo-4-frontend-angular)
7. [Módulo 5: Docker y Despliegue](#7-módulo-5-docker-y-despliegue)
8. [Flujo Completo: Del URL a la Pantalla](#8-flujo-completo-del-url-a-la-pantalla)
9. [Guía Práctica: Levantar Todo desde Cero](#9-guía-práctica-levantar-todo-desde-cero)

---

# 1. Visión General de la Arquitectura

## 1.1 Concepto Teórico: Arquitectura de Microservicios

Una arquitectura de microservicios divide una aplicación grande en servicios pequeños e independientes. Cada servicio:
- Tiene su propia responsabilidad
- Puede desplegarse independientemente
- Se comunica con otros servicios via HTTP/JSON

**Alternativa**: Monolito (todo en un solo proyecto). Es más simple pero más difícil de escalar y mantener.

## 1.2 Diagrama de Arquitectura

```
┌─────────────────────────────────────────────────────────────────────────┐
│                           INTERNET / USUARIO                             │
│                     (Navegador web en tu computadora)                     │
└───────────────────────────────────┬─────────────────────────────────────┘
                                    │
                                    │ HTTP Request (puerto 80/443)
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                         NGINX (Reverse Proxy)                            │
│                                                                          │
│  Función: Recibe TODAS las peticiones y las enruta al servicio correcto │
│                                                                          │
│  Reglas de enrutamiento:                                                 │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────────┐  │
│  │ /  → Frontend    │  │ /api/ → Backend  │  │ /api/mail/ → Mail   │  │
│  │ (archivos HTML)  │  │ Auth (Java)      │  │ Service (Java)      │  │
│  └──────────────────┘  └──────────────────┘  └──────────────────────┘  │
└───────────┬─────────────────────┬─────────────────────┬─────────────────┘
            │                     │                     │
            ▼                     ▼                     ▼
┌───────────────────┐  ┌───────────────────┐  ┌───────────────────┐
│   FRONTEND        │  │  BACKEND AUTH     │  │  BACKEND MAIL     │
│   Angular 17      │  │  Spring Boot      │  │  Spring Boot      │
│   Puerto: 80      │  │  Puerto: 8080     │  │  Puerto: 8081     │
│                   │  │                   │  │                   │
│ - Login           │  │ - Autenticación   │  │ - Envío emails    │
│ - Dashboard       │  │ - CRUD Usuarios   │  │ - Reset password  │
│ - Productos       │  │ - CRUD Productos  │  │ - Logs de correos │
│ - Usuarios        │  │ - Inventario      │  │                   │
└───────────────────┘  │ - Auditoría       │  └─────────┬─────────┘
                       └─────────┬─────────┘            │
                                 │                      │
                    ┌────────────┼────────────┐         │
                    │            │            │         │
                    ▼            ▼            ▼         ▼
            ┌───────────┐ ┌───────────┐ ┌───────────┐ ┌───────────┐
            │  MySQL    │ │  Oracle   │ │SQL Server │ │   SMTP    │
            │  :3306    │ │  :1521    │ │  :1433    │ │  (Gmail)  │
            │           │ │           │ │           │ │           │
            │ Usuarios  │ │ Productos │ │ Auditoría │ │  Correos  │
            │ Roles     │ │ Inventario│ │ Logs      │ │           │
            │ Tokens    │ │ Ventas    │ │ Notific.  │ │           │
            └───────────┘ └───────────┘ └───────────┘ └───────────┘
```

## 1.3 ¿Por qué esta arquitectura?

| Decisión | Razón | Alternativa |
|----------|-------|-------------|
| 2 backends separados | Mail es independiente, puede escalar solo | 1 monolito (más simple pero acoplado) |
| 3 bases de datos | Aprender multi-datasource, separar responsabilidades | 1 sola BD (más simple) |
| Nginx como proxy | Un solo punto de entrada, maneja SSL, cachea estáticos | Exponer cada servicio directamente |
| JWT stateless | No necesita sesión en servidor, escala horizontalmente | Sesiones en servidor (más simple pero no escala) |

## 1.4 Flujo de una Petición Completa

```
1. Usuario escribe: http://localhost/login
2. Nginx recibe la petición en puerto 80
3. Nginx ve que es "/" → sirve index.html de Angular
4. Angular carga en el navegador (SPA)
5. Usuario llena formulario de login y da clic
6. Angular envía POST http://localhost/api/auth/login con JSON {username, password}
7. Nginx ve "/api/" → reenvía a backend-auth:8080
8. Spring Security recibe la petición
9. AuthController.login() se ejecuta
10. AuthService valida credenciales contra MySQL
11. Si son correctas → JwtService genera token JWT
12. Responde JSON con {accessToken, refreshToken, roles}
13. Angular almacena el token en localStorage
14. Angular redirige a /dashboard
15. Para cada petición siguiente, Angular agrega header: Authorization: Bearer <token>
```

---

# 2. Tecnologías Elegidas y Por Qué

## 2.1 Backend

| Tecnología | Qué es | Por qué se eligió | Alternativas |
|-----------|--------|-------------------|--------------|
| Java 17 | Lenguaje de programación | Tipado fuerte, ecosistema maduro, demanda laboral alta | Python, Node.js, Go, C# |
| Spring Boot 3.2 | Framework web para Java | Convención sobre configuración, enorme ecosistema | Quarkus, Micronaut, Jakarta EE |
| Spring Security | Framework de seguridad | Estándar de la industria para auth en Java | Apache Shiro, custom filters |
| Spring Data JPA | ORM (mapeo objeto-relacional) | Elimina SQL manual, repositorios automáticos | MyBatis, JDBC Template, jOOQ |
| JWT | Tokens de autenticación | Stateless, no necesita sesión en servidor | OAuth2, sesiones, API keys |
| Maven | Gestor de dependencias | Estándar en Java enterprise | Gradle (más moderno pero más complejo) |
| Lombok | Generador de código | Elimina boilerplate (getters, setters, constructors) | Records de Java, escribir manual |

## 2.2 Frontend

| Tecnología | Qué es | Por qué se eligió | Alternativas |
|-----------|--------|-------------------|--------------|
| Angular 17 | Framework SPA | Estructura opinada, TypeScript nativo, enterprise-ready | React, Vue, Svelte |
| TypeScript | Superset de JavaScript | Tipado estático, menos bugs, mejor IDE support | JavaScript puro |
| Angular Material | Librería de UI | Componentes accesibles, diseño Material Design | PrimeNG, Tailwind, Bootstrap |
| RxJS | Programación reactiva | Manejo de streams asíncronos (HTTP, eventos) | Promises, Signals |

## 2.3 Bases de Datos

| BD | Tipo | Por qué se eligió | Cuándo usarla |
|----|------|-------------------|---------------|
| MySQL 8 | Relacional (open source) | Gratis, rápida, la más popular del mundo | Apps web, startups, CRUD general |
| Oracle XE | Relacional (enterprise) | Estándar en banca/gobierno, PL/SQL poderoso | Sistemas críticos, alta concurrencia |
| SQL Server | Relacional (Microsoft) | Integración con .NET, reporting, BI | Empresas Microsoft, analytics |

## 2.4 DevOps

| Tecnología | Qué es | Por qué se eligió |
|-----------|--------|-------------------|
| Docker | Contenedores | Empaqueta app + dependencias, funciona igual en cualquier máquina |
| Docker Compose | Orquestación local | Levanta múltiples contenedores con un comando |
| Nginx | Servidor web/proxy | Rápido, estable, maneja SSL y proxy reverso |
| GitHub Actions | CI/CD | Gratis para repos públicos, integrado con GitHub |
| Kubernetes | Orquestación producción | Auto-healing, escalado, rolling updates |

---

# 3. Módulo 1: Base de Datos

## 3.1 Concepto Teórico: Bases de Datos Relacionales

Una base de datos relacional organiza datos en **tablas** (filas y columnas) con **relaciones** entre ellas.

Conceptos fundamentales:
- **Tabla**: Colección de registros del mismo tipo (como una hoja de Excel)
- **Columna**: Un atributo/campo (nombre, email, precio)
- **Fila**: Un registro individual (un usuario, un producto)
- **Primary Key (PK)**: Identificador único de cada fila (generalmente `id`)
- **Foreign Key (FK)**: Referencia a otra tabla (crea la relación)
- **Índice**: Estructura que acelera búsquedas (como el índice de un libro)

## 3.2 MySQL - Tablas de Autenticación

### Tabla: `users`
```
¿Por qué existe?
- Almacena la información de cada persona que usa el sistema.
- Es la tabla CENTRAL de autenticación.
- Spring Security la consulta en cada login.

¿Cómo se relaciona?
- users ←→ roles (muchos a muchos via user_roles)
- users → password_reset_tokens (un usuario puede tener tokens de reset)
```

### Tabla: `roles`
```
¿Por qué existe?
- Define los PERMISOS del sistema (qué puede hacer cada usuario).
- Permite control de acceso granular sin modificar código.
- Un admin puede crear nuevos roles sin tocar el backend.

¿Cómo se relaciona?
- roles ←→ users (muchos a muchos via user_roles)
- Un rol puede tener muchos usuarios, un usuario puede tener muchos roles.
```

### Tabla: `user_roles` (tabla pivote)
```
¿Por qué existe?
- Implementa la relación MUCHOS A MUCHOS entre users y roles.
- Sin esta tabla, un usuario solo podría tener 1 rol.
- Permite: admin tiene roles [ADMIN, USER], manager tiene [MANAGER, USER].

Estructura:
  user_id | role_id
  --------|--------
  1       | 1        ← admin tiene rol ADMIN
  1       | 3        ← admin también tiene rol USER
  2       | 2        ← manager1 tiene rol MANAGER
```

### Tabla: `password_reset_tokens`
```
¿Por qué existe?
- Cuando un usuario olvida su contraseña, se genera un token único.
- Ese token se envía por email como enlace.
- El token tiene EXPIRACIÓN (1 hora) para seguridad.
- Se marca como "usado" después de restablecer la contraseña.

Flujo:
  1. Usuario pide reset → se genera token UUID → se guarda aquí
  2. Se envía email con link: /reset-password?token=abc123
  3. Usuario hace clic → se valida token (no expirado, no usado)
  4. Usuario pone nueva contraseña → token se marca como usado
```

### Tabla: `email_logs`
```
¿Por qué existe?
- Registra TODOS los correos enviados por el sistema.
- Permite auditar: ¿se envió el correo? ¿falló? ¿cuándo?
- Útil para debugging: "no me llegó el correo" → revisar logs.

Estados posibles:
  PENDING → Se creó pero no se ha enviado
  SENT    → Se envió exitosamente
  FAILED  → Falló el envío (se guarda el error)
```

## 3.3 Oracle - Tablas de Productos

### Tabla: `products`
```
¿Por qué existe?
- Catálogo de todos los productos que vende el negocio.
- Contiene precio, stock actual, categoría, SKU.
- Usa SOFT DELETE (is_active=0) en lugar de borrar físicamente.

¿Por qué soft delete?
- Nunca pierdes datos históricos.
- Las ventas pasadas siguen referenciando el producto.
- Se puede "reactivar" un producto.

¿Por qué en Oracle?
- Simula un escenario real donde productos/inventario están en un ERP Oracle.
- Practica el uso de SEQUENCES (Oracle no tiene AUTO_INCREMENT).
```

### Tabla: `inventory_movements`
```
¿Por qué existe?
- Registra CADA movimiento de stock (entrada o salida).
- Permite trazabilidad: ¿quién sacó 5 unidades? ¿cuándo? ¿por qué?
- El stock actual en `products` es el RESULTADO de sumar entradas - salidas.

Tipos:
  ENTRY → Compra a proveedor, devolución de cliente
  EXIT  → Venta, merma, transferencia

Ejemplo:
  Producto "Laptop HP" stock=25
  ENTRY +25 "Stock inicial"
  EXIT  -3  "Venta V-2024-001"
  Stock actual: 25 - 3 = 22
```

### Tabla: `sales` y `sale_details`
```
¿Por qué existen?
- `sales`: Encabezado de la venta (quién compró, total, fecha)
- `sale_details`: Detalle línea por línea (qué productos, cuántos, a qué precio)

¿Por qué separadas?
- Una venta puede tener MUCHOS productos.
- Relación 1:N (una venta → muchos detalles).
- Permite calcular totales, reportes por producto, etc.

Ejemplo:
  sales: V-2024-001, TechCorp, $56,999.97
  sale_details:
    - Laptop HP x3 @ $18,999.99 = $56,999.97
```

## 3.4 SQL Server - Tablas de Auditoría

### Tabla: `audit_logs`
```
¿Por qué existe?
- Registra TODAS las acciones importantes del sistema.
- Cumplimiento regulatorio (quién hizo qué y cuándo).
- Investigación de incidentes de seguridad.

¿Qué registra?
- LOGIN, REGISTER, CREATE, UPDATE, DELETE, ASSIGN_ROLE, STOCK_ENTRY, STOCK_EXIT

¿Por qué en SQL Server?
- Simula un escenario donde auditoría está en un servidor separado.
- Si hackean la BD principal, los logs están en otro servidor.
```

### Tabla: `system_logs`
```
¿Por qué existe?
- Logs TÉCNICOS del sistema (errores, tiempos de respuesta, etc.).
- Diferente de audit_logs: esto es para DESARROLLADORES, no para negocio.
- Permite detectar: endpoints lentos, errores frecuentes, servicios caídos.
```

### Tabla: `login_attempts`
```
¿Por qué existe?
- Registra CADA intento de login (exitoso o fallido).
- Detecta ataques de fuerza bruta (muchos intentos fallidos desde una IP).
- Permite bloquear IPs sospechosas.

Ejemplo de detección:
  IP 203.0.113.50 → 50 intentos fallidos en 1 minuto → ALERTA
```

### Tabla: `notifications`
```
¿Por qué existe?
- Sistema de notificaciones internas para usuarios.
- "Tienes stock bajo", "Tu venta se completó", "Intento de acceso sospechoso".
- Permite marcar como leídas (is_read).
```

---

# 4. Módulo 2: Backend Auth (Explicación Línea por Línea)

## 4.1 Concepto: Spring Boot

**¿Qué es?**
Framework que simplifica la creación de aplicaciones Java. Sin Spring Boot necesitarías configurar manualmente: servidor web, conexión a BD, seguridad, serialización JSON, etc. Spring Boot lo hace automáticamente.

**Analogía**: Spring Boot es como un auto automático. Java puro es como un auto manual donde tú controlas todo.

## 4.2 Archivo: `pom.xml` (Línea por Línea)

```xml
<!-- pom.xml = "receta" del proyecto. Lista todas las dependencias (librerías externas). -->
<!-- Maven lee este archivo y descarga automáticamente todo lo necesario. -->

<parent>
    <!-- Hereda configuración de Spring Boot (versiones compatibles de todo) -->
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.5</version>
    <!-- Sin esto, tendrías que especificar la versión de CADA librería manualmente -->
</parent>

<dependencies>
    <!-- spring-boot-starter-web: Incluye Tomcat (servidor web) + Jackson (JSON) + Spring MVC -->
    <!-- Con esto ya puedes crear endpoints REST que reciben/devuelven JSON -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- spring-boot-starter-data-jpa: Incluye Hibernate (ORM) + Spring Data -->
    <!-- Permite mapear clases Java ↔ tablas de BD sin escribir SQL -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- spring-boot-starter-security: Autenticación + Autorización -->
    <!-- Protege TODOS los endpoints por defecto (debes configurar cuáles son públicos) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <!-- jjwt: Librería para crear y validar tokens JWT -->
    <!-- JWT = JSON Web Token, un string firmado que contiene info del usuario -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
    </dependency>

    <!-- mysql-connector-j: Driver JDBC para conectarse a MySQL -->
    <!-- Sin esto, Java no sabe "hablar" el protocolo de MySQL -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
    </dependency>

    <!-- ojdbc11: Driver JDBC para Oracle -->
    <dependency>
        <groupId>com.oracle.database.jdbc</groupId>
        <artifactId>ojdbc11</artifactId>
    </dependency>

    <!-- mssql-jdbc: Driver JDBC para SQL Server -->
    <dependency>
        <groupId>com.microsoft.sqlserver</groupId>
        <artifactId>mssql-jdbc</artifactId>
    </dependency>
</dependencies>
```

## 4.3 Archivo: `application.yml` (Configuración)

```yaml
# application.yml = configuración de la aplicación
# Spring Boot lee este archivo al arrancar y configura todo automáticamente

server:
  port: 8080  # Puerto donde escucha el servidor (http://localhost:8080)

spring:
  datasource:
    mysql:
      # ${VARIABLE:valor_default} = lee variable de entorno, si no existe usa el default
      # Esto permite cambiar la config sin tocar código (12-factor app)
      url: jdbc:mysql://${MYSQL_HOST:localhost}:${MYSQL_PORT:3306}/${MYSQL_DB:auth_db}
      # jdbc:mysql:// = protocolo JDBC para MySQL
      # localhost:3306 = host y puerto del servidor MySQL
      # auth_db = nombre de la base de datos
      
      username: ${MYSQL_USER:root}
      password: ${MYSQL_PASSWORD:ENC(encryptedPasswordHere)}
      # ENC(...) = password encriptado con Jasypt (no se guarda en texto plano)
      
      driver-class-name: com.mysql.cj.jdbc.Driver
      # Clase Java que sabe comunicarse con MySQL

jwt:
  secret: ${JWT_SECRET:...}
  # Clave secreta para firmar tokens JWT
  # NUNCA debe estar en código fuente en producción
  
  expiration: 86400000  # 24 horas en milisegundos (24 * 60 * 60 * 1000)
  # Después de este tiempo, el token expira y el usuario debe hacer login de nuevo
```

## 4.4 Archivo: `SecurityConfig.java` (Seguridad)

```java
@Configuration          // Le dice a Spring: "esta clase contiene configuración"
@EnableWebSecurity      // Activa Spring Security
@EnableMethodSecurity   // Permite usar @PreAuthorize en métodos
public class SecurityConfig {

    @Bean  // @Bean = "Spring, crea este objeto y guárdalo para inyectarlo donde se necesite"
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CORS: permite que el frontend (localhost:4200) llame al backend (localhost:8080)
            // Sin esto, el navegador BLOQUEA las peticiones cross-origin por seguridad
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // CSRF: desactivado porque usamos JWT (no cookies de sesión)
            // CSRF protege contra ataques donde un sitio malicioso envía requests con tu cookie
            // Con JWT no hay cookie → no hay riesgo de CSRF
            .csrf(csrf -> csrf.disable())
            
            // Stateless: NO crear sesión HTTP en el servidor
            // Cada request debe traer su propio token JWT
            // Esto permite escalar horizontalmente (múltiples servidores)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Reglas de autorización:
            .authorizeHttpRequests(auth -> auth
                // Estos endpoints son PÚBLICOS (no necesitan token)
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                
                // Solo usuarios con rol ADMIN pueden acceder a /api/admin/**
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                
                // Todo lo demás requiere estar autenticado (tener token válido)
                .anyRequest().authenticated()
            )
            
            // Agregar nuestro filtro JWT ANTES del filtro de username/password
            // Así Spring Security valida el token antes de intentar autenticar con user/pass
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt: algoritmo de hash para contraseñas
        // Strength 12 = 2^12 = 4096 iteraciones (más lento = más seguro contra fuerza bruta)
        // "password123" → "$2a$12$LQv3c1yqBo9SkvXS7QTJPOoFcEoaKCixHjV9Xz0YDQwtM0GZI5Hy"
        // El hash es DIFERENTE cada vez (usa salt aleatorio)
        return new BCryptPasswordEncoder(12);
    }
}
```

## 4.5 Archivo: `JwtService.java` (Tokens)

```java
/**
 * ¿Qué es JWT?
 * - JSON Web Token: un string codificado en Base64 con 3 partes separadas por puntos.
 * - Estructura: HEADER.PAYLOAD.SIGNATURE
 * 
 * Ejemplo real:
 * eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcwNTMxMjAwMH0.firma_aqui
 * 
 * Decodificado:
 * HEADER:  {"alg": "HS256"}           ← Algoritmo de firma
 * PAYLOAD: {"sub": "admin", "iat": 1705312000, "exp": 1705398400}  ← Datos del usuario
 * SIGNATURE: HMAC-SHA256(header + payload, secretKey)  ← Firma para verificar autenticidad
 * 
 * ¿Por qué es seguro?
 * - La FIRMA se genera con una clave secreta que solo el servidor conoce.
 * - Si alguien modifica el payload, la firma ya no coincide → token inválido.
 * - El servidor NO necesita almacenar el token (stateless).
 */
@Service
public class JwtService {

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
            .subject(userDetails.getUsername())     // ¿Quién es? → "admin"
            .issuedAt(new Date())                   // ¿Cuándo se creó? → ahora
            .expiration(new Date(System.currentTimeMillis() + jwtExpiration))  // ¿Cuándo expira?
            .signWith(getSignInKey())               // Firmar con la clave secreta
            .compact();                             // Generar el string final
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        // 1. Extraer el username del token
        final String username = extractUsername(token);
        // 2. Verificar que coincide con el usuario Y que no ha expirado
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
}
```

## 4.6 Archivo: `JwtAuthenticationFilter.java` (Filtro)

```java
/**
 * Este filtro se ejecuta en CADA petición HTTP que llega al servidor.
 * 
 * Flujo:
 * Request HTTP → JwtAuthenticationFilter → ¿Tiene token válido? 
 *                                           ├── SÍ → Autenticar y continuar
 *                                           └── NO → Continuar sin autenticar
 *                                                    (si el endpoint es público, OK)
 *                                                    (si requiere auth, Spring devuelve 401)
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // OncePerRequestFilter = se ejecuta UNA vez por request (no se repite en forwards)

    @Override
    protected void doFilterInternal(HttpServletRequest request, ...) {
        
        // 1. Buscar el header "Authorization"
        final String authHeader = request.getHeader("Authorization");
        
        // 2. Si no hay header o no empieza con "Bearer " → no hay token, seguir
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);  // Pasar al siguiente filtro
            return;
        }
        
        // 3. Extraer el token (quitar "Bearer " del inicio)
        final String jwt = authHeader.substring(7);  // "Bearer eyJhbG..." → "eyJhbG..."
        
        // 4. Extraer el username del token
        final String username = jwtService.extractUsername(jwt);
        
        // 5. Si hay username Y no está ya autenticado
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 6. Cargar el usuario de la BD
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            // 7. Validar el token
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // 8. Crear objeto de autenticación y ponerlo en el contexto de seguridad
                // Esto le dice a Spring: "este request viene de un usuario autenticado"
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // 9. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
```

## 4.7 Archivo: `DataSourceConfig.java` (Multi-BD)

```java
/**
 * ¿Por qué es complejo?
 * - Spring Boot por defecto soporta UNA sola base de datos.
 * - Para usar 3 BDs diferentes, necesitamos configurar manualmente:
 *   1. Un DataSource (conexión) por cada BD
 *   2. Un EntityManagerFactory (JPA) por cada BD
 *   3. Un TransactionManager por cada BD
 *   4. Separar repositorios y entidades por paquete
 * 
 * ¿Cómo sabe Spring qué repositorio usa qué BD?
 * - Por el PAQUETE donde está:
 *   repository.mysql.*    → usa mysqlEntityManagerFactory
 *   repository.oracle.*   → usa oracleEntityManagerFactory
 *   repository.sqlserver.* → usa sqlserverEntityManagerFactory
 */

@Configuration
@EnableJpaRepositories(
    basePackages = "com.fullstack.auth.repository.mysql",  // Solo escanea este paquete
    entityManagerFactoryRef = "mysqlEntityManagerFactory",  // Usa este EntityManager
    transactionManagerRef = "mysqlTransactionManager"       // Usa este TransactionManager
)
public static class MysqlConfig {

    @Primary  // @Primary = "si hay ambigüedad, usa ESTE por defecto"
    @Bean
    public DataSource mysqlDataSource() {
        // Crea un pool de conexiones HikariCP
        // Pool = mantiene N conexiones abiertas listas para usar (más rápido que abrir/cerrar)
        return mysqlDataSourceProperties()
            .initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean mysqlEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        // "update" = Hibernate crea/modifica tablas automáticamente según las entidades
        // Opciones: none, validate, update, create, create-drop
        // En producción SIEMPRE usar "none" o "validate" (nunca modificar BD automáticamente)
        
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        // Dialect = le dice a Hibernate cómo generar SQL específico para MySQL
        // MySQL usa LIMIT, Oracle usa ROWNUM, SQL Server usa TOP

        return builder
            .dataSource(mysqlDataSource())
            .packages("com.fullstack.auth.entity.mysql")  // Solo mapea entidades de este paquete
            .persistenceUnit("mysql")  // Nombre lógico de esta unidad de persistencia
            .properties(properties)
            .build();
    }
}
```

## 4.8 Archivo: `User.java` (Entidad)

```java
@Entity  // "Esta clase se mapea a una tabla en la BD"
@Table(name = "users")  // Nombre de la tabla (si no se pone, usa el nombre de la clase)
@Data  // Lombok: genera getters, setters, toString, equals, hashCode automáticamente
@Builder  // Lombok: permite crear objetos con User.builder().username("admin").build()
public class User implements UserDetails {
    // UserDetails = interfaz de Spring Security que define qué es un "usuario autenticable"

    @Id  // "Este campo es la Primary Key"
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment en MySQL
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    // nullable=false → NOT NULL en la BD
    // unique=true → UNIQUE constraint (no puede haber 2 usuarios con mismo username)
    // length=50 → VARCHAR(50)
    private String username;

    @Column(nullable = false)
    private String password;  // Almacena el HASH BCrypt, NUNCA el password en texto plano

    @ManyToMany(fetch = FetchType.EAGER)
    // ManyToMany = relación muchos a muchos (un user tiene muchos roles, un rol tiene muchos users)
    // EAGER = cargar los roles INMEDIATAMENTE cuando se carga el usuario
    // (LAZY = cargar solo cuando se accede a user.getRoles(), más eficiente pero puede dar errores)
    @JoinTable(
        name = "user_roles",  // Tabla pivote que conecta users con roles
        joinColumns = @JoinColumn(name = "user_id"),  // FK hacia users
        inverseJoinColumns = @JoinColumn(name = "role_id")  // FK hacia roles
    )
    private Set<Role> roles = new HashSet<>();

    // Métodos de UserDetails (Spring Security los usa para autenticación)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convierte roles a GrantedAuthority (formato que entiende Spring Security)
        // "ADMIN" → "ROLE_ADMIN" (Spring Security espera el prefijo ROLE_)
        return roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
            .collect(Collectors.toList());
    }

    @Override
    public boolean isEnabled() {
        return isActive;  // Si is_active=false, el usuario no puede hacer login
    }
}
```

## 4.9 Archivo: `AuthService.java` (Lógica de Negocio)

```java
@Service  // "Esta clase contiene lógica de negocio, Spring la gestiona"
@RequiredArgsConstructor  // Lombok: genera constructor con todos los campos final (inyección)
public class AuthService {

    // Spring inyecta automáticamente estas dependencias (Dependency Injection)
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional  // Si algo falla, se hace ROLLBACK (deshacer todos los cambios)
    public AuthResponse register(RegisterRequest request) {
        // 1. Validar que no exista el username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El username ya existe");
        }

        // 2. Buscar o crear el rol USER
        Role userRole = roleRepository.findByName("USER")
            .orElseGet(() -> roleRepository.save(
                Role.builder().name("USER").build()
            ));

        // 3. Crear el usuario con password encriptado
        User user = User.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            // encode() convierte "password123" → "$2a$12$hash_aleatorio_largo"
            .roles(Set.of(userRole))
            .build();

        userRepository.save(user);  // INSERT INTO users ...

        // 4. Generar tokens
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // 5. Devolver respuesta
        return AuthResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .username(user.getUsername())
            .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
            .build();
    }

    public AuthResponse login(AuthRequest request) {
        // 1. Autenticar (Spring Security valida username + password contra la BD)
        // Si las credenciales son incorrectas, lanza BadCredentialsException
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(), 
                request.getPassword()
            )
        );

        // 2. Si llegamos aquí, las credenciales son correctas
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 3. Generar tokens y devolver
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .username(user.getUsername())
            .email(user.getEmail())
            .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
            .build();
    }
}
```

## 4.10 Archivo: `AuthController.java` (Endpoints)

```java
@RestController  // "Esta clase maneja peticiones HTTP y devuelve JSON"
// @Controller devuelve vistas HTML, @RestController devuelve JSON
@RequestMapping("/api/auth")  // Prefijo de URL para todos los endpoints de esta clase
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")  // POST http://localhost:8080/api/auth/register
    public ResponseEntity<ApiResponse<AuthResponse>> register(
        @Valid @RequestBody RegisterRequest request,
        // @RequestBody = "deserializa el JSON del body a este objeto Java"
        // @Valid = "valida las anotaciones @NotBlank, @Email, @Size del DTO"
        HttpServletRequest httpRequest  // Para obtener la IP del cliente
    ) {
        AuthResponse response = authService.register(request);
        
        // ResponseEntity permite controlar el código HTTP de respuesta
        // .ok() = HTTP 200
        return ResponseEntity.ok(ApiResponse.ok("Usuario registrado", response));
    }

    @PostMapping("/login")  // POST http://localhost:8080/api/auth/login
    public ResponseEntity<ApiResponse<AuthResponse>> login(
        @Valid @RequestBody AuthRequest request,
        HttpServletRequest httpRequest
    ) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Login exitoso", response));
    }
}
```

---
