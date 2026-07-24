# Manual Técnico — Mini-Proyecto 42: Caché con Redis

## Stack
| Componente | Tecnología |
|-----------|-----------|
| Backend | Spring Boot 3.3 + Spring Cache + Spring Data Redis |
| Caché | Redis 7 |
| BD | MySQL 8 |

## Servicios Docker
| Servicio | Puerto | Función |
|----------|--------|---------|
| backend | 8080 | API REST |
| redis | 6379 | Servidor de caché |
| mysql | 3306 | Base de datos |

## Anotaciones clave
| Anotación | Función |
|-----------|---------|
| `@EnableCaching` | Activa el sistema de caché |
| `@Cacheable("productos")` | Guarda resultado en Redis |
| `@CacheEvict(allEntries=true)` | Borra caché cuando datos cambian |
| `@CachePut` | Actualiza caché sin borrarlo todo |

## Performance esperada
| Operación | Sin caché | Con caché |
|-----------|-----------|-----------|
| GET /api/productos | 150-300ms | 2-10ms |
| GET /api/productos/{id} | 50-100ms | 1-5ms |

## TTL (Time-To-Live)
Configurado a 5 minutos. Después de 5 min sin acceso, Redis borra la entrada automáticamente y el siguiente GET consulta la BD de nuevo.
