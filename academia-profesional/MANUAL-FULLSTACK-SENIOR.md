# 📘 Manual Técnico Completo: Todo lo que Debe Saber un Full Stack Senior

## Índice
1. [Base de Datos](#1-base-de-datos)
2. [Backend](#2-backend)
3. [Frontend](#3-frontend)
4. [APIs y Comunicación](#4-apis-y-comunicación)
5. [Seguridad](#5-seguridad)
6. [Docker](#6-docker)
7. [Docker Compose](#7-docker-compose)
8. [Kubernetes](#8-kubernetes)
9. [Servidores y Redes](#9-servidores-y-redes)
10. [Almacenamiento y Caché](#10-almacenamiento-y-caché)
11. [Mensajería y Eventos](#11-mensajería-y-eventos)
12. [CI/CD](#12-cicd)
13. [Observabilidad](#13-observabilidad)
14. [Arquitectura de Software](#14-arquitectura-de-software)
15. [Herramientas de Desarrollo](#15-herramientas-de-desarrollo)
16. [IA para Desarrolladores](#16-ia-para-desarrolladores)
17. [Roadmap Senior](#17-roadmap-senior)

---

# 1. Base de Datos

## ¿Qué es?
Sistema que almacena, organiza y permite consultar datos de forma eficiente.

## Tipos

| Tipo | Ejemplo | Cuándo usar | Cómo funciona |
|------|---------|-------------|---------------|
| **Relacional (SQL)** | PostgreSQL, MySQL, Oracle | Datos estructurados, transacciones, relaciones | Tablas con filas y columnas, SQL para consultar |
| **Documental (NoSQL)** | MongoDB, CouchDB | Datos flexibles, JSON, prototipos | Documentos JSON sin schema fijo |
| **Clave-Valor** | Redis, DynamoDB | Caché, sesiones, datos simples | key → value, ultra rápido (en RAM) |
| **Grafos** | Neo4j | Redes sociales, recomendaciones | Nodos + relaciones, traversal |
| **Columnar** | Cassandra, ClickHouse | Big Data, analytics | Optimizado para leer columnas completas |
| **Series temporales** | InfluxDB, TimescaleDB | IoT, métricas, logs | Optimizado para datos con timestamp |

## Conceptos Fundamentales

### Tabla
```
Tabla = colección de registros del mismo tipo
Fila = un registro (un cliente, un producto)
Columna = un atributo (nombre, precio, email)
```

### Llaves
```sql
PRIMARY KEY (PK) → Identificador ÚNICO de cada fila (nunca se repite, nunca es NULL)
FOREIGN KEY (FK) → Referencia a otra tabla (crea la RELACIÓN)
```

### Relaciones
```
1:N (uno a muchos)  → Un cliente tiene MUCHAS ventas
M:N (muchos a muchos) → Un usuario tiene MUCHOS roles, un rol tiene MUCHOS usuarios
1:1 (uno a uno)     → Un empleado tiene UN expediente
```

### Ejemplo completo
```sql
-- Crear tabla
CREATE TABLE productos (
    id SERIAL PRIMARY KEY,           -- PK auto-incremental
    nombre VARCHAR(150) NOT NULL,    -- Texto obligatorio
    precio NUMERIC(10,2) CHECK(precio > 0),  -- Validación en BD
    stock INTEGER DEFAULT 0,         -- Valor por defecto
    categoria_id INTEGER REFERENCES categorias(id),  -- FK
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW()
);

-- Insertar
INSERT INTO productos (nombre, precio, stock) VALUES ('Laptop', 18999.99, 25);

-- Consultar
SELECT p.nombre, p.precio, c.nombre AS categoria
FROM productos p
JOIN categorias c ON p.categoria_id = c.id
WHERE p.precio > 5000 AND p.activo = TRUE
ORDER BY p.precio DESC
LIMIT 10;

-- Actualizar
UPDATE productos SET precio = precio * 1.10 WHERE categoria_id = 1;

-- Eliminar (soft delete)
UPDATE productos SET activo = FALSE WHERE id = 5;
```

### Índices (Performance)
```sql
-- Sin índice: PostgreSQL lee TODA la tabla (Seq Scan) → lento
-- Con índice: va directo al dato (Index Scan) → rápido

CREATE INDEX idx_productos_precio ON productos(precio);
-- Ahora: WHERE precio > 5000 es 100x más rápido en tablas grandes
```

### Transacciones (ACID)
```sql
BEGIN;
  UPDATE cuentas SET saldo = saldo - 1000 WHERE id = 1;  -- Restar
  UPDATE cuentas SET saldo = saldo + 1000 WHERE id = 2;  -- Sumar
COMMIT;  -- Todo o nada (si falla el segundo, se deshace el primero)
```

---

# 2. Backend

## ¿Qué es?
El código que corre en el SERVIDOR. Procesa peticiones, aplica lógica de negocio, accede a la BD.

## Lenguajes principales

| Lenguaje | Framework | Uso típico |
|----------|-----------|-----------|
| Java | Spring Boot | Enterprise, banca, gobierno |
| JavaScript | Node.js/Express | Startups, APIs rápidas |
| Python | Django/FastAPI | Data, ML, APIs |
| C# | .NET | Enterprise Microsoft |
| Go | Gin/Fiber | Microservicios, alta concurrencia |

## Arquitectura en Capas (Spring Boot)
```
┌─────────────────────────────────────────────┐
│ Controller (recibe HTTP, valida, delega)     │ ← @RestController
├─────────────────────────────────────────────┤
│ Service (lógica de negocio, transacciones)  │ ← @Service
├─────────────────────────────────────────────┤
│ Repository (acceso a datos, queries)        │ ← @Repository
├─────────────────────────────────────────────┤
│ Entity (mapea tabla ↔ clase Java)           │ ← @Entity
└─────────────────────────────────────────────┘
```

## Ejemplo: Endpoint REST completo
```java
// Controller: recibe petición HTTP
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @GetMapping("/{id}")  // GET /api/productos/5
    public Producto buscar(@PathVariable Long id) {
        return service.buscarPorId(id);  // Delega al service
    }

    @PostMapping  // POST /api/productos (body: JSON)
    public Producto crear(@Valid @RequestBody ProductoDto dto) {
        return service.crear(dto);
    }
}

// Service: lógica de negocio
@Service
public class ProductoService {
    public Producto buscarPorId(Long id) {
        return repo.findById(id)
            .orElseThrow(() -> new NotFoundException("No encontrado: " + id));
    }
}

// Repository: acceso a BD (Spring genera el SQL automáticamente)
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByActivoTrue();  // → SELECT * FROM productos WHERE activo = true
}
```

---

# 3. Frontend

## ¿Qué es?
El código que corre en el NAVEGADOR del usuario. Muestra la interfaz, captura eventos, llama al backend.

## Tecnologías

| Tecnología | Qué es | Para qué |
|-----------|--------|----------|
| HTML | Estructura | Qué se muestra (texto, botones, tablas) |
| CSS | Estilo | Cómo se ve (colores, tamaños, layout) |
| JavaScript | Lógica | Qué hace (eventos, llamadas HTTP, animaciones) |
| TypeScript | JS + tipos | Detecta errores en compilación (no en runtime) |
| Angular | Framework SPA | Estructura completa para apps enterprise |
| React | Librería UI | Componentes reutilizables, ecosistema flexible |
| Vue | Framework progresivo | Balance entre simplicidad y poder |

## Conceptos Angular

### Componente
```typescript
@Component({
  selector: 'app-producto',
  template: `
    <h2>{{ producto.nombre }}</h2>
    <p>Precio: {{ producto.precio | currency }}</p>
    <button (click)="comprar()">Comprar</button>
  `
})
export class ProductoComponent {
  producto = signal<Producto>({ nombre: 'Laptop', precio: 18999 });

  comprar() {
    this.http.post('/api/ventas', { productoId: 1 }).subscribe();
  }
}
```

### Servicio (comunicación con backend)
```typescript
@Injectable({ providedIn: 'root' })
export class ProductoService {
  private http = inject(HttpClient);

  listar(): Observable<Producto[]> {
    return this.http.get<Producto[]>('/api/productos');
  }

  crear(producto: Producto): Observable<Producto> {
    return this.http.post<Producto>('/api/productos', producto);
  }
}
```

### Interceptor (agrega token JWT automáticamente)
```typescript
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = inject(AuthService).getToken();
  if (token) {
    req = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
  }
  return next(req);
};
```

---

# 4. APIs y Comunicación

## REST API
```
GET    /api/productos       → Listar todos
GET    /api/productos/5     → Buscar por ID
POST   /api/productos       → Crear (body: JSON)
PUT    /api/productos/5     → Actualizar completo
PATCH  /api/productos/5     → Actualizar parcial
DELETE /api/productos/5     → Eliminar
```

## Formato JSON
```json
{
  "id": 1,
  "nombre": "Laptop HP",
  "precio": 18999.99,
  "stock": 25,
  "activo": true
}
```

## Status Codes
```
200 OK          → Éxito
201 Created     → Recurso creado
400 Bad Request → Datos inválidos
401 Unauthorized → No autenticado
403 Forbidden   → Sin permisos
404 Not Found   → No existe
429 Too Many    → Rate limit excedido
500 Server Error → Bug en el servidor
```

---

# 5. Seguridad

## JWT (JSON Web Token)
```
Flujo:
1. POST /login {username, password}
2. Backend valida → genera token firmado
3. Responde: {token: "eyJhbGciOiJIUzI1NiJ9.eyJzdWI..."}
4. Frontend guarda token en localStorage
5. Cada petición incluye: Authorization: Bearer eyJ...
6. Backend valida firma del token en cada request

Token = HEADER.PAYLOAD.SIGNATURE
  Header: {"alg":"HS256"}
  Payload: {"sub":"admin","exp":1735689600}
  Signature: HMAC-SHA256(header+payload, secretKey)
```

## BCrypt (hash de passwords)
```
NUNCA guardar passwords en texto plano.
BCrypt genera un hash diferente cada vez (salt aleatorio):
  "admin123" → "$2a$12$LQv3c1yqBo9SkvXS7QTJPOoFcEoaKCixHjV9Xz0YDQwtM0GZI5Hy"
  "admin123" → "$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi"
  (Ambos son "admin123" pero el hash es diferente → imposible de revertir)
```

## CORS
```
El navegador BLOQUEA peticiones de localhost:4200 a localhost:8080 por seguridad.
CORS permite que el backend diga: "acepto peticiones de localhost:4200".
```

---

# 6. Docker

## ¿Qué es?
Empaqueta tu app + TODAS sus dependencias en un contenedor que funciona igual en cualquier máquina.

## Analogía
```
Sin Docker: "En mi máquina funciona" (pero en el servidor no)
Con Docker: "Si funciona en Docker, funciona en CUALQUIER lugar"
```

## Dockerfile (receta para crear imagen)
```dockerfile
# Stage 1: Compilar
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline    # Cachear dependencias
COPY src ./src
RUN mvn package -DskipTests      # Compilar

# Stage 2: Ejecutar (imagen final pequeña)
FROM eclipse-temurin:21-jre-alpine
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## Comandos esenciales
```bash
docker build -t mi-app .           # Construir imagen
docker run -p 8080:8080 mi-app     # Ejecutar contenedor
docker ps                          # Ver contenedores corriendo
docker logs -f mi-contenedor       # Ver logs
docker exec -it mi-contenedor sh   # Entrar al contenedor
docker stop mi-contenedor          # Detener
```

---

# 7. Docker Compose

## ¿Qué es?
Orquesta MÚLTIPLES contenedores con un solo archivo y un solo comando.

## Ejemplo: App + BD + Redis
```yaml
version: '3.9'
services:
  postgres:
    image: postgres:16-alpine
    environment:
      POSTGRES_DB: mi_app
      POSTGRES_PASSWORD: secret123
    ports: ["5432:5432"]
    volumes: [pg_data:/var/lib/postgresql/data]
    healthcheck:
      test: ["CMD", "pg_isready"]
      interval: 5s

  redis:
    image: redis:7-alpine
    ports: ["6379:6379"]

  backend:
    build: ./backend
    ports: ["8080:8080"]
    environment:
      DB_HOST: postgres          # Nombre del servicio = DNS interno
      REDIS_HOST: redis
    depends_on:
      postgres: { condition: service_healthy }

  frontend:
    build: ./frontend
    ports: ["80:80"]
    depends_on: [backend]

volumes:
  pg_data:
```

## Comandos
```bash
docker compose up -d          # Levantar todo en background
docker compose ps             # Ver estado
docker compose logs -f backend # Ver logs de un servicio
docker compose down           # Detener todo
docker compose down -v        # Detener + borrar datos
```

---

# 8. Kubernetes

## ¿Qué es?
Orquestador de contenedores para PRODUCCIÓN. Docker Compose es para desarrollo; Kubernetes es para escalar.

## Conceptos
```
Pod         = 1+ contenedores (unidad mínima, efímero)
Deployment  = "Mantén N réplicas de este pod corriendo"
Service     = DNS interno estable (los pods cambian de IP, el service no)
Ingress     = Punto de entrada desde Internet
ConfigMap   = Configuración no-sensible
Secret      = Passwords encriptados
HPA         = Auto-scaling (si CPU > 70% → crear más pods)
```

## ¿Qué ganas sobre Docker Compose?
```
Docker Compose:                    Kubernetes:
- Si un contenedor muere → muerto  - Se recrea automáticamente
- Escalar: manual                  - Automático (HPA)
- 1 máquina                        - Cluster de máquinas
- Sin load balancing               - Incluido
- Sin rolling updates              - Zero downtime deployments
```

---

# 9. Servidores y Redes

## Tipos de servidor
```
Servidor web (Nginx/Apache)  → Sirve archivos estáticos + reverse proxy
Servidor de aplicación (Tomcat) → Ejecuta código Java
Servidor de BD (PostgreSQL)  → Almacena y consulta datos
Load Balancer (HAProxy/ALB)  → Distribuye tráfico entre servidores
CDN (CloudFront/Cloudflare)  → Cachea contenido cerca del usuario
```

## Nginx como Reverse Proxy
```
Internet → Nginx (:80) → decide según la URL:
  /           → sirve archivos Angular (estáticos)
  /api/       → reenvía a Spring Boot (:8080)
  /api/mail/  → reenvía a Mail Service (:8081)
```

## DNS
```
"www.miapp.com" → DNS → "192.168.1.100" (IP del servidor)
Es como la agenda de contactos de Internet.
```

## HTTPS/SSL
```
HTTP  = datos en texto plano (cualquiera puede leerlos)
HTTPS = datos encriptados (solo el servidor puede descifrarlos)
Let's Encrypt = certificados SSL GRATIS y automáticos
```

---

# 10. Almacenamiento y Caché

## Redis (caché en memoria)
```
Sin caché: cada petición → consulta BD (200ms)
Con caché: primera vez → BD (200ms) → guarda en Redis
           siguientes → Redis (2ms) ← 100x más rápido

Usos:
- Caché de datos frecuentes
- Sesiones de usuario
- Rate limiting (contar peticiones por IP)
- Colas de trabajo
- Pub/Sub (notificaciones)
```

## Tipos de almacenamiento
```
RAM (Redis)        → Ultra rápido, volátil, caro
SSD (PostgreSQL)   → Rápido, persistente, económico
HDD (backups)      → Lento, persistente, muy barato
Object Storage (S3) → Archivos/imágenes, escalable, pago por uso
```

---

# 11. Mensajería y Eventos

## Apache Kafka
```
¿Qué es? Sistema de mensajería distribuido.
¿Para qué? Comunicación ASÍNCRONA entre servicios.

Sin Kafka:
  Servicio A llama directamente a Servicio B
  Si B está caído → A falla también (acoplamiento)

Con Kafka:
  A publica evento → Kafka lo almacena → B lo consume cuando pueda
  Si B está caído → los eventos se acumulan (no se pierden)
  Cuando B vuelve → procesa los pendientes

Conceptos:
  Producer → publica mensajes en un Topic
  Topic    → cola de mensajes (como un buzón)
  Consumer → lee mensajes del Topic
  Offset   → posición del consumer (sabe dónde se quedó)
```

---

# 12. CI/CD

## ¿Qué es?
```
CI (Continuous Integration):
  Cada push → se ejecutan tests automáticamente
  Si algo falla → te enteras en minutos (no en 3 días)

CD (Continuous Deployment):
  Si los tests pasan → se despliega automáticamente
  Sin intervención manual → menos errores humanos
```

## GitHub Actions (ejemplo)
```yaml
name: CI/CD
on: [push]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with: { java-version: '21' }
      - run: mvn test          # Ejecutar tests
      - run: mvn package       # Compilar
      - run: docker build -t mi-app .  # Crear imagen
      - run: docker push mi-app        # Subir a registry
```

---

# 13. Observabilidad

## Los 3 pilares
```
1. MÉTRICAS (Prometheus)
   Números en el tiempo: requests/s, latencia, memoria, CPU
   "¿Cuántas peticiones por segundo recibo?"

2. LOGS (ELK Stack / Loki)
   Texto con contexto: qué pasó, cuándo, quién
   "ERROR: Stock insuficiente para producto 5"

3. TRACES (OpenTelemetry / Jaeger)
   Seguir una petición a través de múltiples servicios
   "Request tardó 500ms: 200ms en BD + 150ms en Redis + 150ms en lógica"
```

## Grafana
```
Dashboards visuales que muestran:
- Peticiones por segundo (gráfica de línea)
- Tiempo de respuesta p95 (gauge)
- Errores por minuto (contador)
- Uso de memoria (área)
- Alertas cuando algo supera un umbral
```

---

# 14. Arquitectura de Software

## Patrones principales

### Monolito vs Microservicios
```
Monolito: todo en 1 proyecto, 1 BD, 1 deploy
  ✅ Simple, rápido de desarrollar
  ❌ Difícil de escalar partes independientes

Microservicios: cada módulo es un servicio independiente
  ✅ Escalar solo lo que necesita, equipos independientes
  ❌ Complejidad operativa (redes, logs, tracing)
```

### Hexagonal Architecture
```
El DOMINIO (lógica de negocio) no depende de nada externo.
Si cambias de PostgreSQL a MongoDB → solo cambias el adaptador.
Si cambias de REST a GraphQL → solo cambias el adaptador.

  HTTP → [Port] → APPLICATION → [Port] → PostgreSQL
  CLI  → [Port] → (DOMAIN)    → [Port] → Redis
  Kafka→ [Port] →             → [Port] → Email
```

### SOLID
```
S - Single Responsibility: cada clase tiene UNA razón para cambiar
O - Open/Closed: abierto a extensión, cerrado a modificación
L - Liskov Substitution: subclases reemplazan a la padre sin romper
I - Interface Segregation: interfaces pequeñas y específicas
D - Dependency Inversion: depender de abstracciones, no de implementaciones
```

---

# 15. Herramientas de Desarrollo

## IDE
| Herramienta | Para qué |
|-------------|----------|
| IntelliJ IDEA | Java/Spring Boot (el mejor para Java) |
| VS Code | Frontend, scripts, multi-lenguaje |
| DBeaver | Conectar a cualquier BD (PostgreSQL, MySQL, Oracle) |
| Postman | Probar APIs REST |
| Docker Desktop | Gestionar contenedores |

## Terminal
```bash
git          → Control de versiones
mvn          → Compilar Java
npm/ng       → Frontend Angular
docker       → Contenedores
kubectl      → Kubernetes
curl         → Probar APIs desde terminal
```

## Git (control de versiones)
```bash
git clone <repo>           # Descargar proyecto
git checkout -b feature/x  # Crear rama
git add . && git commit    # Guardar cambios
git push                   # Subir al servidor
git pull                   # Descargar cambios de otros
# Pull Request → Code Review → Merge a main
```

---

# 16. IA para Desarrolladores

## Herramientas 2026

| Herramienta | Qué hace | Precio |
|-------------|----------|--------|
| **GitHub Copilot** | Autocompletado inteligente en IDE | $10-19/mes |
| **Cursor** | IDE completo con IA (fork VS Code) | $20/mes |
| **Claude Code** | Agente terminal para refactors grandes | $20/mes |
| **Kiro** | IDE con specs, hooks, steering | Incluido |
| **v0 (Vercel)** | Genera UI desde texto/imagen | Freemium |
| **ChatGPT/Claude** | Explicar código, diseñar arquitectura | $20/mes |

## Cómo usar IA efectivamente
```
❌ MAL: "Hazme una app de ventas"
✅ BIEN: "Crea un @Service en Spring Boot que registre una venta:
         recibe clienteId + lista de {productoId, cantidad},
         valida stock, descuenta inventario, calcula IVA 16%,
         guarda en tabla ventas + detalle_venta, retorna la venta creada.
         Usa @Transactional para atomicidad."
```

## Lo que IA NO reemplaza (tu valor)
```
- Diseño de arquitectura (qué patrón usar y por qué)
- Debugging de problemas complejos en producción
- Decisiones de negocio (qué construir)
- Code review (detectar problemas sutiles)
- Performance optimization (entender el sistema completo)
- Comunicación con el equipo y stakeholders
```

---

# 17. Roadmap Senior

## ¿Qué diferencia a un Senior de un Junior?

| Aspecto | Junior | Senior |
|---------|--------|--------|
| Código | Funciona | Funciona + es mantenible + es testeable |
| Problemas | Resuelve lo que le asignan | Identifica problemas antes de que ocurran |
| Arquitectura | Sigue la existente | Diseña nuevas, evalúa trade-offs |
| Errores | Los corrige | Los previene (tests, validaciones, monitoring) |
| Equipo | Trabaja solo | Mentora, hace code review, documenta |
| Producción | No tiene acceso | Despliega, monitorea, resuelve incidentes |

## Orden de aprendizaje recomendado
```
Mes 1-3:   Java + SQL + Git (fundamentos sólidos)
Mes 4-6:   Spring Boot + JPA + REST APIs
Mes 7-9:   Angular/React + TypeScript + HTTP
Mes 10-12: Docker + Testing + Security (JWT)
Mes 13-15: Microservicios + Kafka + Redis
Mes 16-18: Kubernetes + CI/CD + Observabilidad
Mes 19-24: Arquitectura + System Design + Liderazgo técnico
```

## Certificaciones útiles (no obligatorias)
```
- AWS Solutions Architect
- Kubernetes (CKA/CKAD)
- Spring Professional
- Oracle Certified Java Developer
```

## Libros recomendados
```
- Clean Code (Robert Martin) → Código limpio
- Design Patterns (GoF) → Patrones de diseño
- System Design Interview (Alex Xu) → Diseño de sistemas
- Building Microservices (Sam Newman) → Microservicios
- The Pragmatic Programmer → Mentalidad de ingeniero
```
