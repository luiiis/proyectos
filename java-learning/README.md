# 🎓 Academia Completa: Java + Spring Boot + Arquitectura Backend

## De Principiante Absoluto a Senior Java Backend Engineer

Academia estructurada en 30 módulos que te lleva desde "¿qué es Java?" hasta construir sistemas empresariales con microservicios, Kubernetes y arquitectura hexagonal.

---

## Estructura

```
java-learning/
├── 01-introduccion/        → ¿Qué es Java? JVM, JDK, JRE
├── 02-instalacion/         → Setup completo del entorno
├── 03-java-basico/         → Variables, operadores, control de flujo
├── 04-poo/                 → Clases, herencia, polimorfismo, interfaces
├── 05-colecciones/         → List, Map, Set, Queue + Big O
├── 06-excepciones/         → Try/catch, custom exceptions
├── 07-generics/            → Tipos genéricos, wildcards
├── 08-streams/             → Stream API, filter, map, reduce
├── 09-lambdas/             → Expresiones lambda, functional interfaces
├── 10-fechas/              → LocalDate, Duration, Period
├── 11-archivos/            → I/O, Files, Path, NIO
├── 12-concurrencia/        → Threads, ExecutorService, CompletableFuture
├── 13-jdbc/                → Conexión directa a BD
├── 14-patrones/            → Design Patterns (GoF)
├── 15-testing/             → JUnit 5, Mockito, TDD
├── 16-maven/               → Gestión de dependencias y build
├── 17-gradle/              → Alternativa moderna a Maven
├── 18-spring-core/         → IoC, DI, Beans, Context
├── 19-spring-boot/         → Auto-configuration, starters
├── 20-jpa-hibernate/       → ORM, entities, relationships
├── 21-rest-api/            → CRUD, paginación, Swagger
├── 22-security/            → Spring Security, roles, permisos
├── 23-jwt/                 → Tokens, refresh, OAuth2
├── 24-docker/              → Contenedores, Compose
├── 25-microservicios/      → Gateway, Discovery, Config
├── 26-kafka/               → Event-driven, producers, consumers
├── 27-redis/               → Cache, sesiones, rate limiting
├── 28-kubernetes/          → Pods, Services, Deployments
├── 29-arquitectura/        → Clean, Hexagonal, DDD, CQRS
├── 30-proyecto-final/      → Sistema enterprise completo
├── ejercicios/             → 900 ejercicios clasificados
├── soluciones/             → Respuestas separadas
├── entrevistas/            → Preguntas Junior → Senior → Tech Lead
└── evaluaciones/           → Exámenes por módulo
```

## Roadmap de Aprendizaje

```
FASE 1: Java Core (Módulos 1-13) ─── 8-12 semanas
  │  Fundamentos del lenguaje, POO, colecciones, streams
  │
FASE 2: Herramientas (Módulos 14-17) ─── 3-4 semanas
  │  Patrones, testing, Maven/Gradle
  │
FASE 3: Spring Ecosystem (Módulos 18-23) ─── 6-8 semanas
  │  Spring Boot, JPA, REST, Security, JWT
  │
FASE 4: DevOps & Arquitectura (Módulos 24-29) ─── 6-8 semanas
  │  Docker, Microservicios, Kafka, K8s, Clean Architecture
  │
FASE 5: Proyecto Final (Módulo 30) ─── 4-6 semanas
     Sistema enterprise completo
```

## Requisitos Previos
- Computadora con 8GB+ RAM
- Ganas de aprender (no se necesita experiencia previa)
- Docker Desktop instalado (para módulos 13+)

## Cómo Usar
1. Sigue los módulos EN ORDEN (cada uno construye sobre el anterior)
2. NO avances sin completar los ejercicios del módulo actual
3. Intenta resolver ANTES de ver las soluciones
4. Cada módulo tiene un mini-proyecto práctico
5. El proyecto final integra TODO lo aprendido




//levantar proyecto 
# DATABASE-LEARNING (SQL + Backend)
cd database-learning
docker compose up -d
# BD lista en localhost:5432, PgAdmin en localhost:5050

# JAVA-LEARNING (Backend Spring Boot)
cd java-learning/19-spring-boot/proyecto/app
mvn spring-boot:run
# API en localhost:8080

# FRONTEND-LEARNING (Angular)
cd frontend-learning/30-proyecto-final/proyecto
ng new sistema-empresarial --standalone
cd sistema-empresarial && ng serve
# Frontend en localhost:4200