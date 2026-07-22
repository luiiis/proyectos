# Examen Fase 3: Spring Boot + JPA + Security (Módulos 18-23)

## Instrucciones
- Tiempo: 120 minutos
- Puedes usar IDE con autocompletado
- Debes tener PostgreSQL corriendo (Docker)
- El resultado debe compilar y ejecutar

---

## Sección A: Teoría (20 puntos)

1. ¿Qué es IoC (Inversion of Control) y cómo Spring lo implementa?
2. ¿Cuál es la diferencia entre `@Component`, `@Service`, `@Repository`?
3. Explica el ciclo de vida de un Bean en Spring (creación → destrucción)
4. ¿Qué es `@Transactional(propagation = REQUIRES_NEW)`?
5. ¿Cómo funciona Spring Security Filter Chain?

---

## Sección B: Proyecto Práctico (80 puntos)

### Construye una API REST completa para "Sistema de Tareas" con:

**Entidades:**
- `Usuario` (id, nombre, email, password, rol)
- `Proyecto` (id, nombre, descripcion, fechaInicio, fechaFin, owner)
- `Tarea` (id, titulo, descripcion, estado, prioridad, proyecto, asignado)

**Requerimientos:**

### B1. Entidades JPA (15 pts)
- Mapear las 3 entidades con relaciones:
  - Usuario 1:N Proyectos (owner)
  - Proyecto 1:N Tareas
  - Usuario 1:N Tareas (asignado)
- Constraints: NOT NULL donde corresponda, UNIQUE en email
- Enums para estado (TODO, IN_PROGRESS, DONE) y prioridad (LOW, MEDIUM, HIGH)

### B2. Endpoints REST (20 pts)
```
POST   /api/auth/register
POST   /api/auth/login

GET    /api/proyectos              → listar (paginado)
POST   /api/proyectos              → crear
GET    /api/proyectos/{id}         → detalle + tareas
PUT    /api/proyectos/{id}         → actualizar
DELETE /api/proyectos/{id}         → eliminar (solo owner)

GET    /api/proyectos/{id}/tareas  → listar tareas del proyecto
POST   /api/proyectos/{id}/tareas  → crear tarea
PATCH  /api/tareas/{id}/estado     → cambiar estado
PATCH  /api/tareas/{id}/asignar    → asignar a usuario
```

### B3. Seguridad JWT (15 pts)
- Login devuelve token JWT
- Endpoints protegidos (excepto register/login)
- Solo el owner del proyecto puede eliminarlo
- Solo el asignado o el owner puede cambiar estado de tarea

### B4. Validaciones (10 pts)
- `@NotBlank` en nombre/título
- `@Email` en email
- `@Size(min=6)` en password
- Custom: fechaFin debe ser después de fechaInicio
- Respuestas de error con formato consistente (400 + lista de errores)

### B5. Tests (10 pts)
- Mínimo 5 tests unitarios del servicio de Tareas
- Mínimo 3 tests de integración de endpoints
- Mock del repository en unit tests

### B6. Docker (10 pts)
- Dockerfile multi-stage
- docker-compose con app + PostgreSQL
- `docker compose up` debe funcionar sin configuración manual

---

## Entrega
Estructura esperada:
```
examen-spring/
├── docker-compose.yml
├── Dockerfile
├── pom.xml
├── src/main/java/com/examen/
│   ├── entity/
│   ├── repository/
│   ├── service/
│   ├── controller/
│   ├── security/
│   └── dto/
├── src/main/resources/application.yml
└── src/test/java/...
```

## Aprobación
- 50+ = Aprobado
- 70+ = Listo para trabajar con Spring Boot
- 85+ = Nivel mid backend Java
