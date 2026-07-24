# Nivel 14: DevOps y Despliegue — Proyecto 14: Publicación Completa

## Objetivo
Llevar tu aplicación desde tu computadora hasta un servidor real en producción.

## Temas
- Perfiles de Spring Boot (dev, staging, prod)
- Variables de entorno (no hardcodear passwords)
- Empaquetado JAR (`mvn package`)
- Compilación Angular (`ng build --prod`)
- Dockerfile (backend + frontend)
- Docker Compose (orquestar servicios)
- Nginx (reverse proxy + SSL)
- Base de datos en contenedor + volúmenes
- Redes Docker
- GitHub Actions (CI/CD)
- Servidor Linux (deploy)
- HTTPS + Dominio
- Backups automáticos
- Logs y monitoreo

## Ambientes
```
local        → tu computadora (Docker Compose)
desarrollo   → servidor de desarrollo (automático con CI)
pruebas      → para QA
preproducción → réplica de producción
producción   → usuarios reales
```

## Documentación
- Manual de despliegue (paso a paso)
- Manual de rollback (cómo revertir)
- Manual de variables de entorno
- Manual de backup y restauración
- Checklist de liberación (antes de cada deploy)
- Evidencias del despliegue
- Plan de continuidad
- Matriz de ambientes (qué tiene cada uno)
