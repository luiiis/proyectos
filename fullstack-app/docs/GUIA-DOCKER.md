# Guía de Docker - Despliegue Completo

## Prerrequisitos

1. **Docker Desktop** instalado (Windows/Mac) o Docker Engine (Linux)
2. **Docker Compose v2** (incluido en Docker Desktop)
3. **Mínimo 8GB RAM** disponible para Docker
4. Puertos libres: 80, 3306, 1521, 1433, 8080, 8081

## Paso a Paso

### 1. Preparar el entorno

```bash
# Clonar el proyecto
git clone <tu-repositorio>
cd fullstack-app

# Crear archivo de variables de entorno
cp .env.example .env
```

### 2. Configurar variables de entorno

Editar el archivo `.env`:

```env
# Cambiar estos valores para producción
MYSQL_ROOT_PASSWORD=tuPasswordSeguro123!
ORACLE_PASSWORD=oracleSeguro123!
SQLSERVER_PASSWORD=SqlSeguro123!
JWT_SECRET=tuClaveJWTBase64De256Bits

# Configurar correo real
MAIL_USERNAME=tu-correo-real@gmail.com
MAIL_PASSWORD=tu-app-password-gmail
```

### 3. Construir las imágenes

```bash
# Construir todas las imágenes
docker compose build

# O construir una específica
docker compose build backend-auth
docker compose build backend-mail
docker compose build frontend
```

### 4. Levantar los servicios

```bash
# Levantar todo en background
docker compose up -d

# Ver el progreso
docker compose logs -f

# Esperar a que las BDs estén healthy (puede tomar 2-3 min)
docker compose ps
```

### 5. Verificar que todo funciona

```bash
# Verificar servicios
docker compose ps

# Probar backend auth
curl http://localhost:8080/api/auth/login

# Probar frontend
# Abrir http://localhost en el navegador
```

### 6. Crear usuario admin inicial

```bash
# Registrar primer usuario via API
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "email": "admin@example.com",
    "password": "admin123",
    "firstName": "Admin",
    "lastName": "System"
  }'
```

Luego asignar rol ADMIN directamente en MySQL:
```bash
docker exec -it fullstack-mysql mysql -uroot -p

# Dentro de MySQL:
USE auth_db;
INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'admin' AND r.name = 'ADMIN';
```

---

## Comandos Útiles

```bash
# Ver logs de un servicio específico
docker compose logs -f backend-auth

# Reiniciar un servicio
docker compose restart backend-auth

# Escalar un servicio (múltiples instancias)
docker compose up -d --scale backend-auth=2

# Entrar a un contenedor
docker exec -it fullstack-mysql bash
docker exec -it fullstack-backend-auth sh

# Ver uso de recursos
docker stats

# Limpiar todo (CUIDADO: elimina datos)
docker compose down -v
docker system prune -a
```

---

## Construir Imágenes para Registry

```bash
# Tag para Docker Hub
docker tag fullstack-app-backend-auth:latest tu-usuario/backend-auth:1.0.0
docker tag fullstack-app-backend-mail:latest tu-usuario/backend-mail:1.0.0
docker tag fullstack-app-frontend:latest tu-usuario/frontend:1.0.0

# Push a Docker Hub
docker push tu-usuario/backend-auth:1.0.0
docker push tu-usuario/backend-mail:1.0.0
docker push tu-usuario/frontend:1.0.0
```

---

## Troubleshooting

### Oracle tarda mucho en iniciar
Es normal. Oracle XE puede tardar 2-5 minutos en estar ready. El healthcheck lo maneja.

### Error de memoria
Oracle necesita bastante RAM. Asignar al menos 4GB a Docker:
- Docker Desktop → Settings → Resources → Memory: 8GB

### Puerto ya en uso
```bash
# Ver qué usa el puerto
netstat -ano | findstr :3306
# Cambiar el puerto en docker-compose.yml
ports:
  - "3307:3306"  # Usar 3307 externamente
```

### Rebuild después de cambios en código
```bash
docker compose up --build -d
```

---

## Producción

Para producción, considerar:

1. **No exponer puertos de BD** (quitar ports de mysql, oracle, sqlserver)
2. **Usar secrets de Docker** en lugar de variables de entorno
3. **Agregar HTTPS** con certificado SSL en Nginx
4. **Limitar recursos** por contenedor
5. **Backup automático** de volúmenes
6. **Monitoreo** con Prometheus/Grafana

```yaml
# Ejemplo de límites de recursos
services:
  backend-auth:
    deploy:
      resources:
        limits:
          cpus: '1.0'
          memory: 512M
```
