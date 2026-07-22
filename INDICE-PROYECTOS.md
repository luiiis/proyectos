# 📚 Índice de Proyectos — Workspace Completo

## Vista General

```
c:\cursos\github\proyectos\
├── academia-profesional/    ← 22 proyectos progresivos (Java → Angular → Docker → Cloud)
├── database-learning/       ← 21 módulos SQL (PostgreSQL + MySQL + Oracle)
├── java-learning/           ← 30 módulos Java (básico → Spring Boot → Microservicios)
├── frontend-learning/       ← 30 módulos Frontend (HTML/CSS/JS → Angular → Arquitectura)
├── fullstack-2026/          ← Proyecto moderno (Java 25 + Angular 21 + Keycloak + Redis)
└── fullstack-app/           ← Proyecto enterprise (3 BDs + 2 backends + Angular)
```

---

## Cómo Usar Esta Academia

### Si eres PRINCIPIANTE (no sabes programar):
```
1. java-learning (módulos 01-09)     → Aprender Java desde cero
2. database-learning (módulos 01-07) → Aprender SQL
3. academia-profesional (proyectos 01-03) → Practicar con proyectos reales
```

### Si ya sabes JAVA BÁSICO:
```
1. java-learning (módulos 10-17)     → Colecciones, Streams, Testing, Maven
2. database-learning (módulos 08-15) → SQL avanzado
3. academia-profesional (proyectos 04-08) → APIs con Spring Boot
```

### Si quieres aprender FRONTEND:
```
1. frontend-learning (módulos 01-09) → HTML, CSS, JavaScript, TypeScript
2. frontend-learning (módulos 10-18) → Angular Core
3. academia-profesional (proyectos 09-11) → Angular + Spring Boot integrado
```

### Si quieres ser FULL STACK:
```
1. Completar Java + SQL + Frontend (arriba)
2. academia-profesional (proyectos 12-22) → Docker, Kafka, Redis, K8s
3. fullstack-2026 → Stack moderno completo (Java 25 + Angular 21)
4. fullstack-app → Enterprise con 3 bases de datos
```

---

## Tabla de Ejecución Rápida

| Proyecto | Comando | URL |
|----------|---------|-----|
| academia/proyecto-01 | `javac Calculadora.java && java Calculadora` | Terminal |
| academia/proyecto-06 | `mvn spring-boot:run` | http://localhost:8080 |
| academia/proyecto-11 | `docker compose up --build -d` | http://localhost |
| database-learning | `docker compose up -d` | PgAdmin :5050 |
| java-learning/03 | `javac *.java && java Ejercicio01_Variables` | Terminal |
| java-learning/19 | `mvn spring-boot:run` | http://localhost:8080 |
| frontend-learning/04 | `node ejercicios.js` | Terminal |
| **fullstack-2026** | `docker compose up --build -d` | http://localhost |
| **fullstack-app** | `docker compose up --build -d` | http://localhost |

---

## Documentación por Proyecto

| Proyecto | Manual Técnico | Cómo Ejecutar | Entrevistas | Evaluaciones |
|----------|:-:|:-:|:-:|:-:|
| academia-profesional | ✅ | ✅ (por proyecto) | ✅ 12 archivos | — |
| database-learning | ✅ | ✅ | ✅ 15 archivos | ✅ 3 exámenes |
| java-learning | ✅ | ✅ | ✅ 4 archivos | ✅ 4 exámenes |
| frontend-learning | — | ✅ | ✅ 3 archivos | ✅ 3 exámenes |
| fullstack-2026 | ✅ | ✅ | — | — |
| fullstack-app | ✅ | ✅ | — | — |

---

## Tecnologías Cubiertas

### Backend
- Java 8 → 25 (todas las versiones)
- Spring Boot 3.x (Web, JPA, Security, Cache, AI)
- PostgreSQL, MySQL, Oracle, SQL Server
- Docker, Kubernetes
- Kafka, Redis
- JWT, OAuth2, Keycloak
- Microservicios (Gateway, Discovery, Config)

### Frontend
- HTML5, CSS3 (Flexbox, Grid, responsive)
- JavaScript ES6+ (Promises, async/await, modules)
- TypeScript 5.x (interfaces, generics, utility types)
- Angular 17-21 (Signals, Standalone, @defer, control flow)
- Angular Material, PrimeNG
- RxJS
- Testing (Jest, Playwright)

### DevOps
- Docker + Docker Compose
- Kubernetes (Deployments, Services, HPA, Ingress)
- CI/CD (GitHub Actions)
- Prometheus + Grafana (observabilidad)
- Terraform (IaC)

---

## Prerrequisitos Globales

```bash
# Verificar TODO instalado:
java -version          # Java 21+ (idealmente 25)
mvn -version           # Maven 3.9+
node -v                # Node.js 20+
ng version             # Angular CLI 17+
docker --version       # Docker 24+
docker compose version # Compose v2+
git --version          # Git 2.40+
```
