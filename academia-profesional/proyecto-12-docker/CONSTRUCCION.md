# Bitácora de Construcción - Proyecto 12: Docker

## ¿Qué problema resuelve Docker?
"En mi máquina funciona" → Con Docker funciona IGUAL en todas las máquinas.

## Decisiones técnicas

| Decisión | Razón | Alternativa |
|----------|-------|-------------|
| Multi-stage build | Imagen final pequeña (200MB vs 800MB) | Single stage (más grande) |
| Alpine base | ~5MB vs ~100MB de Ubuntu | Ubuntu/Debian (más compatible) |
| Usuario no-root | Seguridad (si hackean la app, no tienen root) | Root (inseguro) |
| Health checks | Docker sabe si el servicio está REALMENTE listo | Sin healthcheck (solo "running") |
| ZGC | Garbage collector de baja latencia para contenedores | G1GC (default, más pausas) |
| MaxRAMPercentage=75 | Deja 25% para el SO del contenedor | Sin límite (puede OOM) |
| Nginx como proxy | Un solo punto de entrada, maneja CORS | Exponer backend directamente |
| Volúmenes nombrados | Datos persisten entre reinicios | Bind mounts (menos portable) |
| Red bridge | Contenedores se ven por nombre DNS | Host network (menos aislado) |

## Tamaños de imagen resultantes
```
postgres:16-alpine    → ~80MB
redis:7-alpine        → ~30MB
backend (multi-stage) → ~200MB
frontend (nginx)      → ~25MB
pgadmin               → ~350MB
TOTAL                 → ~685MB
```

## Orden de arranque (depends_on + healthcheck)
```
1. postgres (healthcheck: pg_isready)
2. redis (healthcheck: redis-cli ping)
3. backend (espera a postgres + redis healthy)
4. frontend (espera a backend)
5. pgadmin (espera a postgres)
```
