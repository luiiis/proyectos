# Preguntas de Entrevista - Full Stack Senior (Proyecto Final)

## Arquitectura y Diseño

### 1. ¿Cómo diseñarías un sistema ERP desde cero para una empresa mediana?
**Respuesta:**
1. **Requisitos**: entender módulos necesarios (ventas, compras, inventario, RRHH)
2. **Arquitectura**: monolito modular (no microservicios si el equipo es < 10 devs)
3. **Stack**: Spring Boot + Angular + PostgreSQL (maduros, ecosistema grande)
4. **Capas**: Controller → Service → Repository (Clean Architecture)
5. **Base de datos**: esquema normalizado 3FN, con desnormalización selectiva para reportes
6. **Seguridad**: OAuth2 + roles + permisos granulares
7. **Despliegue**: Docker + Kubernetes + CI/CD desde día 1
8. **Observabilidad**: logs + métricas + traces desde día 1

---

### 2. ¿Monolito o microservicios? ¿Cómo decides?
**Respuesta:**
Depende del contexto:
- **Equipo < 5 devs, dominio simple**: monolito (modular)
- **Equipo 5-15 devs, dominios separados**: monolito → extraer 2-3 servicios críticos
- **Equipo > 15 devs, alta escala**: microservicios con equipos por dominio

Regla: empieza con monolito modular. Solo separa cuando el DOLOR de no hacerlo sea mayor que la COMPLEJIDAD de microservicios.

---

### 3. ¿Qué es Domain-Driven Design (DDD) y cómo lo aplicas?
**Respuesta:**
DDD es diseñar software alrededor del DOMINIO del negocio (no de la tecnología):
- **Bounded Context**: cada módulo (ventas, inventario) tiene su propio modelo
- **Aggregates**: grupo de entidades que se modifican juntas (Venta + DetalleVenta)
- **Value Objects**: objetos sin identidad (Dinero, Dirección, Email)
- **Domain Events**: "VentaRegistrada", "StockBajo" → desacoplar módulos
- **Ubiquitous Language**: usar el mismo lenguaje que el negocio (no "Entity", sino "Producto")

---

## Performance y Escalabilidad

### 4. Tu API tarda 5 segundos en responder. ¿Cómo diagnosticas y resuelves?
**Respuesta (paso a paso):**
1. **Métricas**: ¿es un endpoint o todos? ¿siempre o solo bajo carga?
2. **Tracing**: seguir el request → ¿dónde se gasta el tiempo?
3. **Queries**: `EXPLAIN ANALYZE` → ¿sequential scans? → agregar índices
4. **N+1**: ¿Hibernate hace 100 queries en vez de 1? → JOIN FETCH
5. **Cache**: ¿datos que no cambian frecuentemente? → Redis
6. **Concurrencia**: ¿bloqueos? → connection pool, async processing
7. **Payload**: ¿devuelves datos innecesarios? → paginación, proyecciones

---

### 5. ¿Cómo manejas 1 millón de registros en una tabla de auditoría?
**Respuesta:**
- **Particionamiento**: particionar por fecha (partition by range on fecha)
- **Archivado**: mover registros > 1 año a tabla fría / S3
- **Índices**: solo en campos que se consultan frecuentemente
- **Queries**: SIEMPRE con filtro de fecha (nunca full scan)
- **Materialized views**: para reportes agregados

---

## Seguridad

### 6. ¿Cuáles son los top 5 riesgos de seguridad en tu ERP?
**Respuesta:**
1. **SQL Injection**: mitigado con JPA/PreparedStatement (nunca concatenar SQL)
2. **Broken Authentication**: JWT con expiración corta + refresh tokens + rate limiting en login
3. **Insecure Direct Object Reference**: verificar que el usuario tiene permiso sobre ESTE recurso específico
4. **Sensitive Data Exposure**: no devolver passwords en responses, HTTPS obligatorio
5. **Broken Access Control**: @PreAuthorize en cada endpoint, principio de menor privilegio

---

### 7. Un cliente reporta que alguien accedió a su cuenta. ¿Qué haces?
**Respuesta:**
1. **Inmediato**: invalidar todos los tokens del usuario (blacklist en Redis)
2. **Investigar**: revisar logs de auditoría (IP, hora, acciones realizadas)
3. **Contener**: forzar cambio de password, habilitar 2FA
4. **Prevenir**: analizar cómo obtuvieron acceso (credenciales filtradas? phishing? sesión robada?)
5. **Comunicar**: informar al cliente qué pasó y qué medidas se tomaron

---

## DevOps y Producción

### 8. ¿Cómo es tu pipeline de CI/CD ideal?
**Respuesta:**
```
Push a Git → GitHub Actions:
  1. Lint + format check
  2. Compilar (mvn package)
  3. Tests unitarios (mvn test)
  4. Tests de integración (Testcontainers)
  5. Análisis de código (SonarQube)
  6. Build Docker image
  7. Push a registry (ECR)
  8. Deploy a staging (automático)
  9. Tests E2E en staging
  10. Deploy a producción (manual approval)
```

---

### 9. Tu app en producción tiene un memory leak. ¿Cómo lo detectas y resuelves?
**Respuesta:**
1. **Detectar**: Grafana muestra memoria subiendo constantemente sin bajar (sawtooth → leak)
2. **Diagnosticar**: heap dump (`jmap -dump:live,format=b,file=heap.hprof PID`)
3. **Analizar**: Eclipse MAT o VisualVM → qué objetos están acumulándose
4. **Causas comunes**: listeners no removidos, caché sin TTL/límite, streams no cerrados, ThreadLocal sin cleanup
5. **Resolver**: fix + deploy + verificar que la curva se estabiliza

---

### 10. ¿Cómo garantizas zero-downtime en un deploy?
**Respuesta:**
1. **Rolling update** (K8s): reemplaza pods uno por uno (siempre hay pods atendiendo)
2. **Health checks**: no enviar tráfico hasta que el nuevo pod esté ready
3. **Graceful shutdown**: terminar requests en curso antes de apagar (30s grace period)
4. **Database migrations**: compatibles hacia atrás (nunca borrar columnas en el mismo deploy)
5. **Feature flags**: nueva funcionalidad oculta hasta activar manualmente
6. **Rollback plan**: si hay problemas → `kubectl rollout undo` en segundos

---

## Diseño de APIs

### 11. ¿Cómo versionas tu API sin romper clientes existentes?
**Respuesta:**
Estrategias:
- **URL path**: `/api/v1/productos`, `/api/v2/productos` (más explícito, recomendado)
- **Header**: `Accept: application/vnd.mi-api.v2+json`
- **Query param**: `?version=2`

Reglas de backward compatibility:
- NUNCA borrar campos de un response existente
- Campos nuevos son siempre opcionales
- Si necesitas breaking change → nueva versión
- Deprecar versiones con plazo (6 meses)

---

### 12. ¿Cuál es tu proceso para diseñar un feature nuevo end-to-end?
**Respuesta:**
1. **Requisitos**: entender el QUÉ (casos de uso, edge cases, criterios de aceptación)
2. **Diseño API**: definir endpoints, request/response, error cases
3. **Diseño BD**: schema, relaciones, índices necesarios
4. **Implementar backend**: entity → repository → service → controller → tests
5. **Implementar frontend**: service → componente → template → tests
6. **Integrar**: conectar frontend con backend real
7. **Testing**: unitario + integración + E2E
8. **Code review**: PR con descripción clara
9. **Deploy staging**: verificar en ambiente real
10. **Deploy producción**: feature flag → rollout gradual
