# 🏢 Academia Profesional Full Stack - Basada en Proyectos Reales

## Filosofía

Esta NO es una academia de teoría. Es una simulación de trabajo real en empresa.

Cada módulo es un **proyecto independiente y funcional** que puedes:
- Compilar y ejecutar
- Conectar a base de datos real
- Desplegar con Docker
- Presentar en una entrevista

---

## Los 18 Proyectos

| # | Proyecto | Tecnología | Qué construyes |
|---|----------|-----------|----------------|
| 01 | Calculadora | Java Core | App de consola con lógica |
| 02 | Sistema Biblioteca | Java POO | Herencia, polimorfismo, interfaces |
| 03 | Sistema Inventario | Java Colecciones | HashMap, ArrayList, búsquedas |
| 04 | Sistema Clientes | Java JDBC | Conexión directa a PostgreSQL |
| 05 | Base de Datos Ventas | SQL Puro | 16 tablas, triggers, procedures |
| 06 | API Clientes | Spring Boot | REST API con validaciones |
| 07 | API Productos | Spring Boot + JPA | CRUD + PostgreSQL + paginación |
| 08 | Autenticación JWT | Spring Security | Login, roles, tokens |
| 09 | Dashboard Admin | Angular | SPA con routing y Material |
| 10 | Sistema Clientes | Angular + PrimeNG | DataTable, Dialog, Forms |
| 11 | Sistema Ventas | Angular + Spring | Full Stack integrado |
| 12 | Contenerización | Docker | Dockerizar todo el stack |
| 13 | Notificaciones | Kafka | Event-driven messaging |
| 14 | Sistema Caché | Redis | Performance optimization |
| 15 | Microservicios | Spring Cloud | Sistema distribuido |
| 16 | Despliegue | Kubernetes | Orquestación producción |
| 17 | Observabilidad | Prometheus + Grafana | Monitoreo y alertas |
| **Final** | **ERP Empresarial** | **Todo integrado** | **Sistema enterprise completo** |

---

## Estructura de Cada Proyecto

```
proyecto-XX-nombre/
├── README.md                 ← Descripción general
├── CONSTRUCCION.md           ← Bitácora: por qué cada decisión
├── MANUAL_TECNICO.md         ← Arquitectura, config, deploy
├── MANUAL_FUNCIONAL.md       ← Casos de uso, flujos, reglas
├── COMO_EJECUTAR.md          ← Paso a paso para levantar
├── docker-compose.yml        ← Infraestructura
├── Dockerfile                ← Imagen del app
├── docs/
│   ├── arquitectura.md       ← Diagramas
│   ├── modelo-er.md          ← Base de datos
│   └── casos-uso.md          ← Historias de usuario
├── sql/
│   ├── schema.sql            ← Crear tablas
│   └── seed.sql              ← Datos de prueba
├── src/                      ← Código fuente
├── tests/                    ← Tests
├── ejercicios/               ← Retos para practicar
├── soluciones/               ← Respuestas explicadas
└── entrevistas/              ← Preguntas técnicas del tema
```

---

## Cómo Usar Esta Academia

```
1. Ir proyecto por proyecto EN ORDEN
2. Leer CONSTRUCCION.md (entender el POR QUÉ)
3. Ejecutar el proyecto (COMO_EJECUTAR.md)
4. Estudiar el código fuente
5. Resolver los ejercicios
6. Responder preguntas de entrevista
7. Solo avanzar cuando domines el proyecto actual
```

---

## Prerrequisitos

```bash
# Verificar instalaciones:
java -version       # Java 21+
mvn -version        # Maven 3.9+
node -version       # Node.js 20+
ng version          # Angular CLI 17+
docker --version    # Docker 24+
docker compose version  # Compose v2+
```
