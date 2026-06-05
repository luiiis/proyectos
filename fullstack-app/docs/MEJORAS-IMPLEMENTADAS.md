# Mejoras Implementadas - Guía Detallada

## Índice
1. [Swagger/OpenAPI](#1-swaggeropenapi)
2. [Rate Limiting](#2-rate-limiting)
3. [Tests Unitarios e Integración](#3-tests-unitarios-e-integración)
4. [CI/CD con GitHub Actions](#4-cicd-con-github-actions)
5. [Monitoreo con Prometheus + Grafana](#5-monitoreo-con-prometheus--grafana)
6. [Certificados SSL con Let's Encrypt](#6-certificados-ssl-con-lets-encrypt)
7. [Kubernetes](#7-kubernetes)
8. [Redis para Caché](#8-redis-para-caché)

---

## 1. Swagger/OpenAPI

### ¿Qué es?
Documentación interactiva y automática de tu API REST. Genera una página web donde puedes ver todos los endpoints, sus parámetros, y probarlos directamente.

### ¿Dónde está?
- `config/SwaggerConfig.java` → Configuración
- Acceso: `http://localhost:8080/swagger-ui.html`

### ¿Cómo funciona?
```
Tu código Java (Controllers) → SpringDoc escanea anotaciones → Genera JSON OpenAPI → Swagger UI lo renderiza
```

1. SpringDoc lee tus `@RestController`, `@GetMapping`, `@PostMapping`, etc.
2. Genera automáticamente la especificación OpenAPI (JSON).
3. Swagger UI renderiza esa especificación como página web interactiva.
4. Puedes probar endpoints directamente desde el navegador.

### ¿Cómo probar endpoints protegidos?
1. Primero haz login con `POST /api/auth/login`
2. Copia el `accessToken` de la respuesta
3. Clic en el botón "Authorize" (candado arriba a la derecha)
4. Pega: `Bearer tu-token-aqui`
5. Ahora todos los endpoints incluirán el token automáticamente

### Dependencia agregada
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.5.0</version>
</dependency>
```

---

## 2. Rate Limiting

### ¿Qué es?
Mecanismo que limita cuántas peticiones puede hacer un cliente en un período de tiempo. Protege contra:
- Ataques de fuerza bruta (intentar miles de contraseñas)
- DDoS (saturar el servidor con peticiones)
- Abuso de la API (scraping excesivo)

### ¿Dónde está?
- `config/RateLimitConfig.java` → Define los límites
- `config/RateLimitFilter.java` → Aplica los límites a cada petición

### ¿Cómo funciona? (Algoritmo Token Bucket)
```
Imagina una cubeta con 100 fichas:
- Cada petición consume 1 ficha
- Cada minuto se rellenan 100 fichas
- Si no hay fichas → petición rechazada (HTTP 429)

Para login es más estricto:
- Cubeta con 5 fichas
- Se rellenan 5 cada minuto
- Máximo 5 intentos de login por minuto por IP
```

### Flujo:
```
Request → RateLimitFilter → ¿Hay tokens? 
                              ├── SÍ → Continúa al Controller
                              └── NO → HTTP 429 "Too Many Requests"
```

### Headers de respuesta:
- `X-Rate-Limit-Remaining: 95` → Te quedan 95 peticiones
- `X-Rate-Limit-Retry-After-Seconds: 45` → Espera 45 segundos

---

## 3. Tests Unitarios e Integración

### ¿Qué son?

| Tipo | Qué prueba | Velocidad | Dependencias |
|------|-----------|-----------|--------------|
| Unitario | 1 clase aislada | Milisegundos | Ninguna (mocks) |
| Integración | Todo junto | Segundos | BD en memoria (H2) |

### ¿Dónde están?
```
src/test/java/
├── service/
│   ├── AuthServiceTest.java        → Tests unitarios de autenticación
│   └── ProductServiceTest.java     → Tests unitarios de productos
└── controller/
    └── AuthControllerIntegrationTest.java → Test de integración
```

### Estructura de un test (AAA):
```java
@Test
void login_Success() {
    // ARRANGE (Preparar)
    when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
    
    // ACT (Ejecutar)
    AuthResponse response = authService.login(loginRequest);
    
    // ASSERT (Verificar)
    assertThat(response.getAccessToken()).isNotNull();
}
```

### Conceptos clave:
- **@Mock**: Crea un objeto falso que simula una dependencia
- **@InjectMocks**: Inyecta los mocks en la clase que pruebas
- **when().thenReturn()**: "Cuando llamen a este método, devuelve esto"
- **verify()**: "Verifica que se llamó a este método"
- **assertThat()**: "Verifica que el resultado es X"

### Ejecutar tests:
```bash
# Todos los tests
mvn test

# Solo tests unitarios
mvn test -Dtest="*Test"

# Solo tests de integración
mvn test -Dtest="*IntegrationTest"

# Con reporte detallado
mvn test -Dsurefire.reportFormat=plain
```

---

## 4. CI/CD con GitHub Actions

### ¿Qué es?
Automatización que se ejecuta cada vez que haces push o creas un Pull Request.

### ¿Dónde está?
- `.github/workflows/ci-cd.yml`

### Flujo del Pipeline:
```
Push a GitHub
    │
    ├── Job 1: Tests Backend Auth (Maven + JUnit)
    ├── Job 2: Tests Backend Mail (Maven)
    ├── Job 3: Build Frontend (Node + Angular)
    │
    └── Job 4: Docker Build & Push (solo si pasan los 3 anteriores Y es push a main)
              ├── Build imagen backend-auth
              ├── Build imagen backend-mail
              └── Build imagen frontend
              └── Push a Docker Hub
```

### Configuración necesaria en GitHub:
1. Ir a tu repo → Settings → Secrets and variables → Actions
2. Agregar estos secrets:
   - `DOCKER_HUB_USERNAME`: tu usuario de Docker Hub
   - `DOCKER_HUB_TOKEN`: token de acceso de Docker Hub

### ¿Cuándo se ejecuta?
- `push` a `main` o `develop` → Ejecuta todo
- `pull_request` a `main` → Solo tests (no push de imágenes)

---

## 5. Monitoreo con Prometheus + Grafana

### ¿Qué es cada uno?

| Herramienta | Función | Analogía |
|-------------|---------|----------|
| Prometheus | Recolecta métricas | El termómetro que mide |
| Grafana | Visualiza métricas | El dashboard que muestra |
| Actuator | Expone métricas de Spring Boot | La ventana que permite medir |

### ¿Dónde está?
- `docker/monitoring/prometheus.yml` → Config de Prometheus
- `docker/monitoring/grafana/` → Dashboards y datasources
- `application.yml` → Configuración de Actuator

### Flujo:
```
Spring Boot (Actuator) → expone /actuator/prometheus
Prometheus → cada 15s consulta ese endpoint → almacena métricas
Grafana → consulta Prometheus → muestra gráficas bonitas
```

### Métricas disponibles:
- **HTTP**: peticiones/segundo, tiempo de respuesta, errores
- **JVM**: memoria heap, garbage collection, threads
- **BD**: conexiones activas, tiempo de queries
- **Custom**: lo que tú quieras medir

### Acceso:
- Prometheus: `http://localhost:9090`
- Grafana: `http://localhost:3000` (admin/admin123)

### Levantar monitoreo:
```bash
docker compose -f docker-compose.yml -f docker-compose.monitoring.yml up -d
```

---

## 6. Certificados SSL con Let's Encrypt

### ¿Qué es?
Certificados HTTPS gratuitos y automáticos. Encriptan la comunicación entre el navegador y tu servidor.

### ¿Dónde está?
- `docker/ssl/nginx-ssl.conf` → Configuración Nginx con HTTPS
- `docker/ssl/init-letsencrypt.sh` → Script para obtener certificado

### Flujo:
```
1. Ejecutas init-letsencrypt.sh
2. Certbot contacta a Let's Encrypt
3. Let's Encrypt verifica tu dominio (HTTP challenge)
4. Si pasa → emite certificado (válido 90 días)
5. Nginx usa el certificado para HTTPS
6. Cron job renueva automáticamente cada 60 días
```

### Requisitos:
- Un dominio real (ej: miapp.com)
- DNS apuntando a tu servidor
- Puerto 80 abierto

### Pasos:
```bash
# 1. Editar el script con tu dominio y email
nano docker/ssl/init-letsencrypt.sh

# 2. Ejecutar
chmod +x docker/ssl/init-letsencrypt.sh
./docker/ssl/init-letsencrypt.sh

# 3. Actualizar nginx-ssl.conf con tu dominio
# 4. Reiniciar
docker compose up -d
```

---

## 7. Kubernetes

### ¿Qué es?
Orquestador de contenedores para producción. Docker Compose es para desarrollo; Kubernetes es para producción a escala.

### ¿Dónde está?
```
k8s/
├── namespace.yml              → Espacio aislado para la app
├── secrets.yml                → Contraseñas encriptadas
├── backend-auth-deployment.yml → Cómo ejecutar el backend
└── ingress.yml                → Punto de entrada desde Internet
```

### Conceptos clave:

| Concepto | Docker Compose equivalente | Descripción |
|----------|---------------------------|-------------|
| Pod | Container | Unidad mínima de ejecución |
| Deployment | service + image | Define cómo ejecutar pods |
| Service | ports | Nombre DNS interno |
| Ingress | nginx reverse proxy | Entrada desde Internet |
| Secret | .env | Variables sensibles |
| Namespace | - | Aislamiento de recursos |

### Ventajas sobre Docker Compose:
- **Auto-healing**: Si un pod muere, se recrea solo
- **Escalado**: `kubectl scale deployment backend-auth --replicas=5`
- **Rolling updates**: Actualiza sin downtime
- **Load balancing**: Distribuye tráfico entre réplicas

### Comandos básicos:
```bash
# Aplicar configuración
kubectl apply -f k8s/ -n fullstack-app

# Ver pods corriendo
kubectl get pods -n fullstack-app

# Ver logs de un pod
kubectl logs -f deployment/backend-auth -n fullstack-app

# Escalar
kubectl scale deployment backend-auth --replicas=3 -n fullstack-app
```

---

## 8. Redis para Caché

### ¿Qué es?
Base de datos en memoria que almacena datos temporales para acceso ultra-rápido.

### ¿Dónde está?
- `config/RedisConfig.java` → Configuración de conexión y caché
- `service/ProductService.java` → Uso de `@Cacheable` y `@CacheEvict`

### ¿Cómo funciona?
```
Sin caché:
  GET /api/products → Controller → Service → Oracle (200ms) → Response

Con caché:
  GET /api/products (1ra vez) → Service → Oracle (200ms) → Guarda en Redis → Response
  GET /api/products (2da vez) → Service → Redis (2ms) → Response ← 100x más rápido!
  ... (después de 5 min el caché expira y vuelve a consultar Oracle)
```

### Anotaciones:
```java
@Cacheable("products")     // Guarda resultado en caché
@CacheEvict(allEntries=true) // Borra el caché (cuando hay cambios)
@CachePut                  // Actualiza el caché sin borrarlo
```

### Configuración de TTL (Time To Live):
- Productos: 5 minutos (cambian poco)
- Usuarios: 2 minutos (pueden cambiar más)
- Roles: 30 minutos (casi nunca cambian)

### Levantar Redis:
```bash
docker compose -f docker-compose.yml -f docker-compose.monitoring.yml up redis -d

# Verificar
docker exec -it fullstack-redis redis-cli ping
# Respuesta: PONG

# Ver claves almacenadas
docker exec -it fullstack-redis redis-cli keys "*"
```

---

## Resumen de Puertos

| Servicio | Puerto | URL |
|----------|--------|-----|
| Frontend | 80/443 | http://localhost |
| Backend Auth | 8080 | http://localhost:8080 |
| Backend Mail | 8081 | http://localhost:8081 |
| Swagger UI | 8080 | http://localhost:8080/swagger-ui.html |
| MySQL | 3306 | - |
| Oracle | 1521 | - |
| SQL Server | 1433 | - |
| Redis | 6379 | - |
| Prometheus | 9090 | http://localhost:9090 |
| Grafana | 3000 | http://localhost:3000 |

---

## Orden de Aprendizaje Recomendado

Si estás aprendiendo, te sugiero este orden:

1. **Swagger** → Lo más fácil, resultado inmediato visible
2. **Redis/Caché** → Concepto simple, gran impacto en rendimiento
3. **Tests** → Fundamental para cualquier desarrollador profesional
4. **Rate Limiting** → Seguridad básica, fácil de entender
5. **CI/CD** → Automatización, requiere cuenta en GitHub
6. **Monitoreo** → Útil en producción, requiere entender métricas
7. **SSL** → Necesitas un dominio real
8. **Kubernetes** → Lo más complejo, para cuando domines Docker
