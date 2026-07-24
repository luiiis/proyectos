# 📚 Índice de Proyectos — Workspace Completo

## Vista General

```
c:\cursos\github\proyectos\
├── fullstack-roadmap/       ← 🎯 RUTA PRINCIPAL: 14 niveles de cero a profesional
├── mini-proyectos/          ← 🧪 50 funcionalidades aisladas (login, email, PDF, caché...)
├── academia-profesional/    ← 22 proyectos progresivos (Java → Angular → Docker → Cloud)
├── database-learning/       ← 21 módulos SQL (PostgreSQL + MySQL + Oracle)
├── java-learning/           ← 30 módulos Java (básico → Spring Boot → Microservicios)
├── frontend-learning/       ← 30 módulos Frontend (HTML/CSS/JS → Angular → Arquitectura)
├── english-learning/        ← Inglés técnico (pronunciación, vocabulario, lecturas)
├── fullstack-2026/          ← Proyecto moderno (Java 25 + Angular 21 + Keycloak + Redis)
└── fullstack-app/           ← Proyecto enterprise (3 BDs + 2 backends + Angular)
```

---

## 🎯 fullstack-roadmap — Ruta Principal (De Cero a Profesional)

La ruta que te lleva paso a paso desde "Hola Mundo" hasta un ERP con Docker.

| Nivel | Proyecto | Tecnología | Docker |
|-------|----------|-----------|:------:|
| 00 | Preparación + Fundamentos | Instalación + 12 guías | — |
| 01 | API de Saludos | Spring Boot básico | — |
| 02 | CRUD Tareas en memoria | Capas (Controller→Service→Repository) | — |
| 03 | Productos + MyBatis | MySQL + Flyway + MyBatis | ✅ |
| 04 | Pruebas Automatizadas | JUnit 5 + Mockito + MockMvc | ✅ |
| 05 | Seguridad JWT | Spring Security + JWT + Roles | ✅ |
| 06 | Funciones Profesionales | Email + Reset password + Auditoría | ✅ |
| 07 | Arquitectura Hexagonal | Puertos + Adaptadores + Dominio | ✅ |
| 08 | Angular Básico | Signals + Forms + Routing (4 mini-apps) | — |
| 09 | Angular + Backend | HttpClient + Servicios + Componentes | ✅ |
| 10 | Login Full Stack | Angular Guard + Interceptor + JWT | ✅ |
| 11 | Sistema Inventario | Multi-módulo (compras, stock, reportes) | ✅ |
| 12 | ERP Profesional | Ventas + Caja + Multi-sucursal (10 versiones) | ✅ |
| 13 | Microsoft Entra ID | OAuth2 + OIDC + Resource Server | ✅ |
| 14 | DevOps | Docker + Nginx + CI/CD + GitHub Actions | ✅ |

**Documentación incluida:** README, COMO_EJECUTAR, MANUAL-TECNICO, MANUAL-USUARIO, DOCKER-GUIA

```cmd
:: Levantar cualquier nivel (03-14):
cd fullstack-roadmap/nivel-XX/proyecto
docker compose up -d
```

---

## 🧪 mini-proyectos — Funcionalidades Aisladas

Cada uno resuelve UNA funcionalidad específica. Independientes entre sí.

### Completos (código + Docker + docs):

| # | Proyecto | Stack | Docker |
|---|----------|-------|:------:|
| 01 | Login con Sesión | Spring Security + Thymeleaf | ✅ |
| 03 | Login LDAP | Spring Security + OpenLDAP | ✅ |
| 06 | Recuperar Password | Token UUID + Email + Expiración | ✅ |
| 11 | Enviar Email | Spring Mail + MailHog | ✅ |
| 16 | Subir Imágenes | MultipartFile + Validación | ✅ |
| 18 | Generar PDF | OpenPDF (tablas, reportes) | ✅ |
| 20 | Importar Excel/CSV | Apache POI + Validación fila a fila | ✅ |
| 29 | Auditoría Automática | AOP + JSON antes/después | ✅ |
| 42 | Caché Redis | @Cacheable + Redis 7 + TTL | ✅ |

### Documentados (README con código, pendientes de estructura):
02, 04, 05, 07-10, 12-15, 17, 19, 21-28, 30-41, 43-50

```cmd
:: Levantar cualquier mini-proyecto:
cd mini-proyectos/XX-nombre
docker compose up -d
```

---

## 🏢 academia-profesional — 22 Proyectos Enterprise

| # | Proyecto | Tecnología |
|---|----------|-----------|
| 01 | Calculadora | Java Core (consola) |
| 02 | Biblioteca | Java POO |
| 03 | Inventario | Java Colecciones |
| 04 | Clientes JDBC | Java + PostgreSQL directo |
| 05 | BD Ventas | SQL puro (16 tablas, triggers) |
| 06 | API Clientes | Spring Boot REST |
| 07 | API Productos | Spring Boot + JPA + Docker |
| 08 | Auth JWT | Spring Security completo |
| 09 | Dashboard Angular | Angular + Material |
| 10 | Clientes PrimeNG | Angular + DataTable |
| 11 | Ventas Full Stack | Angular + Spring integrado |
| 12 | Docker | Contenedores para todo |
| 13 | Kafka | Event-driven messaging |
| 14 | Redis | Caché + performance |
| 15 | Microservicios | Spring Cloud (Gateway, Discovery) |
| 16 | Kubernetes | Orquestación producción |
| 17 | Observabilidad | Prometheus + Grafana |
| 18 | Spring AI | IA con OpenAI |
| 19 | AWS Cloud | Despliegue en la nube |
| 20 | Terraform | Infraestructura como código |
| 21 | GraphQL | Alternativa a REST |
| 22 | WebSockets | Tiempo real |
| Final | ERP Empresarial | Todo integrado |

---

## 📖 database-learning — 21 Módulos SQL

| Fase | Módulos | Contenido |
|------|---------|-----------|
| Básico | 01-07 | SELECT, INSERT, UPDATE, DELETE, JOIN, funciones |
| Intermedio | 08-13 | Subconsultas, vistas, triggers, procedimientos |
| Avanzado | 14-19 | Optimización, particiones, replicación |
| Proyecto | 20-21 | BD completa + proyecto final |

---

## ☕ java-learning — 30 Módulos Java

| Fase | Módulos | Contenido |
|------|---------|-----------|
| Fundamentos | 01-09 | Variables, condicionales, ciclos, arreglos, POO |
| Intermedio | 10-17 | Colecciones, Streams, Excepciones, Generics, Testing |
| Spring Boot | 18-24 | REST, JPA, Security, Docker, Redis |
| Avanzado | 25-30 | Microservicios, Kafka, Kubernetes, proyecto final |

---

## 🎨 frontend-learning — 30 Módulos Frontend

| Fase | Módulos | Contenido |
|------|---------|-----------|
| Web básico | 01-05 | HTML5, CSS3, JavaScript ES6+, TypeScript |
| Angular Core | 06-15 | Componentes, Signals, Forms, Routing, Services |
| Angular Pro | 16-25 | Material, RxJS, Testing, Lazy loading, Performance |
| Arquitectura | 26-30 | Mono-repo, Micro-frontends, Design Systems |

---

## 🌐 english-learning — Inglés Técnico

| Carpeta | Contenido |
|---------|-----------|
| 01-pronunciacion | Reglas, práctica, lecturas |
| (en progreso) | Vocabulario técnico, emails, reuniones |

---

## 🚀 fullstack-2026 — Stack Moderno

```
Java 25 + Angular 21 + PostgreSQL + Redis + Keycloak + Docker
```
Proyecto integrado con las tecnologías más recientes.

---

## 🏗️ fullstack-app — Enterprise

```
Angular + Spring Boot (2 backends) + PostgreSQL + MySQL + Oracle
```
Proyecto enterprise con múltiples bases de datos.

---

## Cómo Usar Esta Academia

### Si eres PRINCIPIANTE (empezar de cero):
```
1. fullstack-roadmap/nivel-00 (fundamentos + instalación)
2. fullstack-roadmap/nivel-01 al 14 (paso a paso)
3. mini-proyectos (practicar funcionalidades específicas)
```

### Si ya sabes JAVA BÁSICO:
```
1. fullstack-roadmap/nivel-03 al 07 (BD + testing + seguridad + arquitectura)
2. fullstack-roadmap/nivel-08 al 10 (Angular + full stack)
3. academia-profesional/proyecto-06 al 11
```

### Si quieres ser SENIOR:
```
1. fullstack-roadmap/nivel-11 al 14 (sistemas completos + Docker)
2. academia-profesional/proyecto-12 al 22 (Docker, Kafka, K8s, Cloud)
3. fullstack-2026 (stack moderno)
4. mini-proyectos (dominar funcionalidades de entrevista)
```

---

## Tabla de Ejecución Rápida

| Proyecto | Comando | URL |
|----------|---------|-----|
| fullstack-roadmap/nivel-03+ | `docker compose up -d` | http://localhost:8080 |
| mini-proyectos/cualquiera | `docker compose up -d` | http://localhost:8080 |
| academia/proyecto-07 | `docker compose up -d` | http://localhost:8080 |
| database-learning | `docker compose up -d` | PgAdmin :5050 |
| fullstack-2026 | `docker compose up --build -d` | http://localhost |
| fullstack-app | `docker compose up --build -d` | http://localhost |

---

## Documentación por Carpeta

| Carpeta | README | Manual Técnico | COMO_EJECUTAR | Docker | Entrevistas |
|---------|:------:|:--------------:|:-------------:|:------:|:-----------:|
| fullstack-roadmap | ✅ | ✅ | ✅ (14 niveles) | ✅ (11 niveles) | — |
| mini-proyectos | ✅ | ✅ (9 proyectos) | ✅ (9 proyectos) | ✅ (9 proyectos) | — |
| academia-profesional | ✅ | ✅ | ✅ | ✅ (parcial) | ✅ |
| database-learning | ✅ | ✅ | ✅ | ✅ | ✅ |
| java-learning | ✅ | ✅ | ✅ | ✅ (parcial) | ✅ |
| frontend-learning | ✅ | — | ✅ | — | ✅ |
| english-learning | ✅ | — | — | — | — |
| fullstack-2026 | ✅ | ✅ | ✅ | ✅ | — |
| fullstack-app | ✅ | ✅ | ✅ | ✅ | — |

---

## Tecnologías Cubiertas

### Backend
- Java 8 → 25 (todas las versiones)
- Spring Boot 3.3 (Web, Security, Mail, Cache, AI, AOP)
- MyBatis + Spring Data JPA
- PostgreSQL, MySQL, Oracle, SQL Server
- Docker, Kubernetes
- Kafka, Redis, RabbitMQ
- JWT, OAuth2, Keycloak, Microsoft Entra ID
- Microservicios (Gateway, Discovery, Config)
- SonarQube (calidad de código)

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
- Nginx (reverse proxy + SSL)
- Prometheus + Grafana (observabilidad)
- Terraform (IaC)
- AWS (EC2, RDS, S3)

---

## Prerrequisitos Globales

```bash
# Verificar TODO instalado:
java -version          # Java 21+
mvn -version           # Maven 3.9+
node -v                # Node.js 22+
ng version             # Angular CLI 20
docker --version       # Docker 24+
docker compose version # Compose v2+
git --version          # Git 2.40+
```
