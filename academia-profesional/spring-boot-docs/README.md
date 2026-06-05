# Documentacion Spring Boot Completa

## Archivos

| # | Archivo | Contenido |
|---|---------|-----------|
| 1 | `01-FUNDAMENTOS.md` | Que es Spring Boot, como funciona, estructura, anotaciones, application.yml |
| 2 | `02-JPA-HIBERNATE.md` | Entities, relaciones (1:N, M:N), Repository, paginacion, transacciones |
| 3 | `03-SECURITY-JWT.md` | Spring Security, JWT completo, filtros, login, registro, roles |
| 4 | `04-REST-API-COMPLETA.md` | Controller, Service, DTOs, validacion, errores, Swagger, curl |

## Como leer

```
1. Leer 01-FUNDAMENTOS (entender que es y como arranca)
2. Leer 02-JPA-HIBERNATE (entender como se conecta a la BD)
3. Leer 03-SECURITY-JWT (entender autenticacion y autorizacion)
4. Leer 04-REST-API-COMPLETA (ver todo junto: endpoint profesional)
```

## Para practicar

Ir al proyecto: `academia-profesional/proyecto-07-api-productos/`
```bash
docker compose up -d    # Levantar PostgreSQL
mvn spring-boot:run     # Ejecutar la API
# Swagger: http://localhost:8080/swagger-ui.html
```
