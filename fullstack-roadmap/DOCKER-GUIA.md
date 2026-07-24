# 🐳 Guía Docker — Levantar cada proyecto

## Comando universal
Desde la carpeta `proyecto/` de cada nivel:
```cmd
docker compose up -d
```

Para parar:
```cmd
docker compose down
```

Para reconstruir después de cambios en código:
```cmd
docker compose up -d --build
```

---

## Estado Docker por nivel

| Nivel | docker-compose.yml | Servicios | Comando |
|-------|:------------------:|-----------|---------|
| 01 | ❌ No necesita | Solo Java (sin BD) | `mvn spring-boot:run` |
| 02 | ❌ No necesita | Solo Java en memoria | `mvn spring-boot:run` |
| 03 | ✅ | MySQL + Backend | `docker compose up -d` |
| 04 | ✅ | MySQL (para integración) | `docker compose up -d` luego `mvn test` |
| 05 | ✅ | MySQL + Backend | `docker compose up -d` |
| 06 | ✅ | MySQL + Backend + MailHog | `docker compose up -d` |
| 07 | ✅ | MySQL + Backend | `docker compose up -d` |
| 08 | ❌ No necesita | Solo Angular (sin backend) | `ng serve` |
| 09 | ✅ | MySQL + Backend + Frontend (Nginx) | `docker compose up -d` |
| 10 | ✅ | MySQL + Backend + Frontend | `docker compose up -d` |
| 11 | ✅ | MySQL + Backend + Frontend + MailHog | `docker compose up -d` |
| 12 | ✅ | MySQL + Backend + Frontend + MailHog | `docker compose up -d` |
| 13 | ✅ | MySQL + Backend | `docker compose up -d` |
| 14 | ✅ | MySQL + Backend + Frontend + Nginx + CI/CD | `docker compose up -d` |

---

## Puertos por servicio

| Servicio | Puerto | URL |
|----------|--------|-----|
| Frontend (Angular/Nginx) | 80 | http://localhost |
| Backend (Spring Boot) | 8080 | http://localhost:8080 |
| MySQL | 3306 | `mysql -h localhost -P 3306 -u root -p` |
| MailHog UI | 8025 | http://localhost:8025 |
| Swagger | 8080 | http://localhost:8080/swagger-ui.html |

---

## Conflictos de puertos

Si ya tienes MySQL instalado localmente en el puerto 3306:

### Opción A: Parar MySQL local
```cmd
:: Windows
net stop mysql

:: O usar Services (services.msc) → MySQL → Stop
```

### Opción B: Cambiar puerto en docker-compose.yml
```yaml
ports:
  - "3307:3306"  # Usar puerto 3307 externamente
```
Y en el backend cambiar la URL:
```yaml
SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/...  # Dentro de Docker sigue siendo 3306
```

---

## Ver logs

```cmd
:: Todos los servicios
docker compose logs -f

:: Solo backend
docker compose logs -f backend

:: Solo MySQL
docker compose logs -f mysql

:: Últimas 50 líneas
docker compose logs --tail 50 backend
```

---

## Conectar DBeaver a MySQL en Docker

```
Host: localhost
Port: 3306
Database: (el nombre del nivel)
Username: root
Password: Root123!
```

---

## Limpiar todo (espacio en disco)

```cmd
:: Parar y borrar volúmenes de un proyecto
docker compose down -v

:: Borrar TODAS las imágenes no usadas
docker system prune -a

:: Ver espacio usado por Docker
docker system df
```

---

## Troubleshooting

| Problema | Causa | Solución |
|----------|-------|----------|
| "Port 3306 already in use" | MySQL local corriendo | Parar MySQL local o cambiar puerto |
| "Port 8080 already in use" | Otro Spring Boot corriendo | Parar la otra app |
| Backend no conecta a MySQL | MySQL no terminó de arrancar | healthcheck se encarga, espera 30s |
| "Cannot connect to Docker" | Docker Desktop no está abierto | Abrir Docker Desktop |
| Build muy lento | Primera vez descarga dependencias | Normal, la segunda vez usa cache |
| "No space left on device" | Docker acumuló imágenes | `docker system prune -a` |
