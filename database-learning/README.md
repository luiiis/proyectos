# 🎓 Academia Completa: SQL + Bases de Datos + Java Spring Boot

## De Principiante Absoluto a Senior Backend/DBA

Este proyecto es una academia estructurada en 21 módulos que te lleva desde "¿qué es una base de datos?" hasta construir aplicaciones empresariales completas con Java Spring Boot + PostgreSQL + MySQL + Oracle.

---

## Estructura del Proyecto

```
database-learning/
├── 01-fundamentos/          → Conceptos básicos de BD
├── 02-ddl/                  → Crear y modificar estructuras
├── 03-dml/                  → Manipular datos (INSERT, UPDATE, DELETE)
├── 04-consultas/            → SELECT desde básico hasta avanzado
├── 05-joins/                → Unir tablas (INNER, LEFT, RIGHT, FULL)
├── 06-funciones/            → Funciones de texto, números, fechas
├── 07-subconsultas/         → Queries dentro de queries
├── 08-vistas/               → Vistas y seguridad
├── 09-indices/              → Performance y optimización
├── 10-triggers/             → Automatización y auditoría
├── 11-procedimientos/       → Stored Procedures y Functions
├── 12-transacciones/        → ACID, COMMIT, ROLLBACK
├── 13-seguridad/            → Usuarios, roles, permisos
├── 14-administracion/       → Backup, restore, monitoreo
├── 15-optimizacion/         → EXPLAIN, tuning, planes de ejecución
├── 16-postgresql/           → PostgreSQL específico
├── 17-mysql/                → MySQL específico
├── 18-oracle/               → Oracle específico
├── 19-docker/               → Contenedores para todas las BDs
├── 20-springboot/           → Java + Spring Boot + JPA
├── 21-proyecto-final/       → Sistema empresarial completo
├── ejercicios/              → 900 ejercicios clasificados
├── soluciones/              → Respuestas (separadas)
├── evaluaciones/            → Exámenes por módulo
└── docker-compose.yml       → Levantar TODO con un comando
```

## Cómo Usar Este Proyecto

### 1. Levantar el entorno (Docker)
```bash
docker compose up -d
# Levanta: PostgreSQL + MySQL + Oracle + PgAdmin + Adminer
```

### 2. Seguir los módulos en orden
Cada módulo tiene:
- `README.md` → Teoría + diagramas
- `scripts/` → SQL ejecutable
- `ejercicios.md` → Práctica sin respuestas
- `casos-reales.md` → Escenarios empresariales

### 3. Resolver ejercicios
- No ver las soluciones hasta intentar
- Las soluciones están en `/soluciones/`

### 4. Tomar evaluaciones
- Exámenes por módulo en `/evaluaciones/`
- Retos tipo entrevista técnica

---

## Requisitos
- Docker Desktop (6GB+ RAM)
- Java 17+ (para módulo 20-21)
- Node.js 20+ (para proyecto final frontend)
- Un editor SQL (DBeaver recomendado, o usar PgAdmin/Adminer incluidos)

## Credenciales por Defecto

| BD | Host | Puerto | Usuario | Password | Base |
|----|------|--------|---------|----------|------|
| PostgreSQL | localhost | 5432 | postgres | postgres123 | empresa_db |
| MySQL | localhost | 3306 | root | mysql123 | empresa_db |
| Oracle XE | localhost | 1521 | system | oracle123 | XEPDB1 |
| PgAdmin | localhost | 5050 | admin@admin.com | admin123 | - |
| Adminer | localhost | 8081 | - | - | - |

Para empezar:
cd database-learning
docker compose up -d
# Esperar 2 minutos
# PostgreSQL: localhost:5432 (postgres/postgres123)
# PgAdmin: http://localhost:5050
# Adminer: http://localhost:8081
