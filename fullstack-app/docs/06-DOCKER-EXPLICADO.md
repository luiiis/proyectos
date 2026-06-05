# Módulo 5: Docker - Explicación Completa

## 5.1 Concepto Teórico: Contenedores

### ¿Qué es Docker?
Docker empaqueta tu aplicación + todas sus dependencias en un "contenedor" que funciona igual en cualquier máquina.

### Problema que resuelve:
```
Sin Docker:
  "En mi máquina funciona" → En el servidor no funciona
  Razón: versiones diferentes de Java, Node, MySQL, etc.

Con Docker:
  "Si funciona en Docker, funciona en CUALQUIER lugar"
  Razón: el contenedor incluye TODO lo necesario
```

### Analogía:
- Contenedor de barco: no importa qué hay dentro, se transporta igual.
- Docker container: no importa qué app hay dentro, se ejecuta igual.

### Docker vs Máquina Virtual:
```
Máquina Virtual:              Docker Container:
┌─────────────────┐           ┌─────────────────┐
│   Tu App        │           │   Tu App        │
│   Librerías     │           │   Librerías     │
│   SO Completo   │ ← pesado  │   (sin SO)      │ ← ligero
│   Hypervisor    │           │   Docker Engine  │
│   Hardware      │           │   SO Host        │
└─────────────────┘           └─────────────────┘
  ~2GB, minutos arrancar        ~100MB, segundos arrancar
```

---

## 5.2 Dockerfile del Backend Auth (Línea por Línea)

```dockerfile
# ============ STAGE 1: Build (Compilación) ============
# "FROM" = imagen base (sistema operativo + herramientas preinstaladas)
# maven:3.9-eclipse-temurin-17 = Ubuntu + Maven 3.9 + Java 17
# "AS build" = nombre de esta etapa (se referencia después)
FROM maven:3.9-eclipse-temurin-17 AS build

# "WORKDIR" = crear y entrar a este directorio (como "mkdir /app && cd /app")
WORKDIR /app

# "COPY" = copiar archivos de tu máquina al contenedor
# Primero copiamos SOLO el pom.xml para cachear dependencias
COPY backend-auth/pom.xml .

# Descargar dependencias (se cachea si pom.xml no cambia)
# -B = modo batch (sin output interactivo)
# Esto es un TRUCO de optimización:
#   Si solo cambias código Java (no pom.xml), Docker reutiliza esta capa cacheada
#   y no vuelve a descargar 200MB de dependencias
RUN mvn dependency:go-offline -B

# Ahora sí copiar el código fuente
COPY backend-auth/src ./src

# Compilar el proyecto (genera el .jar en target/)
# -DskipTests = no ejecutar tests (ya se ejecutaron en CI/CD)
RUN mvn package -DskipTests -B

# ============ STAGE 2: Runtime (Ejecución) ============
# Imagen MUCHO más pequeña: solo tiene Java Runtime (no Maven, no compilador)
# alpine = distribución Linux ultra-ligera (~5MB)
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Crear usuario no-root por SEGURIDAD
# Si un atacante explota la app, no tiene permisos de root
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copiar SOLO el .jar compilado desde la etapa "build"
# --from=build = "toma este archivo de la etapa anterior"
# El resultado: imagen final NO contiene código fuente, ni Maven, ni dependencias de build
COPY --from=build /app/target/*.jar app.jar

# Cambiar al usuario no-root
USER appuser

# Documentar qué puerto usa (informativo, no abre el puerto)
EXPOSE 8080

# Comando que se ejecuta cuando el contenedor arranca
# java -jar app.jar = ejecutar la aplicación Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### ¿Por qué Multi-Stage Build?
```
Sin multi-stage:
  Imagen final: 800MB (incluye Maven, código fuente, dependencias de compilación)

Con multi-stage:
  Imagen final: 200MB (solo Java Runtime + tu .jar)
  
  Stage 1 (build): Se descarta después de compilar
  Stage 2 (runtime): Solo contiene lo necesario para EJECUTAR
```

---

## 5.3 Dockerfile del Frontend (Línea por Línea)

```dockerfile
# Stage 1: Compilar Angular
FROM node:20-alpine AS build
WORKDIR /app

# Copiar package.json primero (cacheo de dependencias)
COPY frontend/package*.json ./

# npm ci = instalación LIMPIA (más rápida y reproducible que npm install)
# Borra node_modules existente y instala exactamente lo que dice package-lock.json
RUN npm ci

# Copiar código fuente
COPY frontend/ .

# Compilar Angular para producción
# Genera archivos estáticos optimizados en dist/
# - Minificación (quita espacios, renombra variables)
# - Tree-shaking (elimina código no usado)
# - AOT compilation (compila templates en build time, no en runtime)
RUN npm run build:prod

# Stage 2: Servir con Nginx
FROM nginx:alpine

# Copiar configuración personalizada de Nginx
COPY docker/nginx.conf /etc/nginx/conf.d/default.conf

# Copiar los archivos compilados de Angular al directorio que sirve Nginx
COPY --from=build /app/dist/fullstack-frontend/browser /usr/share/nginx/html

EXPOSE 80

# Nginx en primer plano (no como daemon)
CMD ["nginx", "-g", "daemon off;"]
```

---

## 5.4 Docker Compose: ¿Qué Ocurre al Ejecutar `docker compose up`?

### Comando:
```bash
docker compose up --build -d
```

### Secuencia de eventos:

```
Paso 1: Docker lee docker-compose.yml
         Identifica 7 servicios: mysql, oracle, sqlserver, sqlserver-init,
         backend-auth, backend-mail, frontend

Paso 2: Crear la red "app-network"
         Red virtual donde todos los contenedores se ven entre sí
         Cada contenedor tiene un DNS interno (mysql, oracle, backend-auth, etc.)

Paso 3: Crear volúmenes (mysql_data, oracle_data, sqlserver_data)
         Directorios persistentes en el host
         Si borras el contenedor, los DATOS sobreviven

Paso 4: Construir imágenes (--build)
         Para cada servicio con "build:", ejecuta su Dockerfile
         backend-auth → docker/backend-auth.Dockerfile
         backend-mail → docker/backend-mail.Dockerfile
         frontend → docker/frontend.Dockerfile

Paso 5: Iniciar bases de datos PRIMERO
         mysql, oracle, sqlserver arrancan en paralelo
         Docker espera a que pasen los healthchecks antes de continuar

Paso 6: Healthchecks
         Cada 10-30 segundos, Docker ejecuta el comando de healthcheck:
         - MySQL: "mysqladmin ping" → ¿responde? → healthy
         - Oracle: "healthcheck.sh" → ¿responde? → healthy (tarda 2-5 min)
         - SQL Server: "sqlcmd SELECT 1" → ¿responde? → healthy

Paso 7: MySQL ejecuta scripts de inicialización
         Docker monta docker/init-scripts/mysql/ en /docker-entrypoint-initdb.d/
         MySQL ejecuta automáticamente todos los .sql en orden alfabético:
         01-schema.sql → Crea tablas
         02-seed-data.sql → Inserta datos de prueba

Paso 8: sqlserver-init se ejecuta
         Espera a que SQL Server esté healthy
         Ejecuta entrypoint.sh que corre los scripts SQL

Paso 9: Iniciar backends (depends_on: condition: service_healthy)
         backend-auth arranca SOLO cuando mysql, oracle Y sqlserver están healthy
         backend-mail arranca SOLO cuando mysql está healthy
         Spring Boot se conecta a las 3 BDs y crea/actualiza tablas (hibernate.hbm2ddl.auto=update)

Paso 10: Iniciar frontend
          Nginx arranca y sirve los archivos estáticos de Angular
          Configura proxy reverso hacia los backends

Paso 11: Todo listo
          Frontend: http://localhost (puerto 80)
          Backend Auth: http://localhost:8080
          Backend Mail: http://localhost:8081
```

### Diagrama temporal:
```
Tiempo →
0s     10s    30s    60s    90s    120s   150s
|------|------|------|------|------|------|
[MySQL starting...][healthy]
[Oracle starting..................][healthy]
[SQL Server starting......][healthy]
                           [sqlserver-init scripts]
                                  [backend-auth starting...][ready]
                    [backend-mail starting...][ready]
                                         [frontend][ready]
                                                   ✓ TODO LISTO
```

---

## 5.5 Docker Compose: Explicación de Cada Sección

```yaml
version: '3.8'  # Versión del formato de docker-compose

services:
  mysql:
    image: mysql:8.0  # Imagen oficial de MySQL 8 desde Docker Hub
    container_name: fullstack-mysql  # Nombre fijo (sin esto, Docker genera uno aleatorio)
    environment:
      # Variables de entorno que MySQL lee al arrancar por primera vez
      MYSQL_ROOT_PASSWORD: rootPassword123!  # Password del usuario root
      MYSQL_DATABASE: auth_db  # Crear esta BD automáticamente
    ports:
      - "3306:3306"  # host:contenedor - exponer puerto al host
      # Esto permite conectarte desde tu máquina con MySQL Workbench
    volumes:
      - mysql_data:/var/lib/mysql  # Persistir datos (sobreviven a docker compose down)
      - ./docker/init-scripts/mysql:/docker-entrypoint-initdb.d  # Scripts de inicialización
    networks:
      - app-network  # Conectar a la red interna
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s   # Verificar cada 10 segundos
      timeout: 5s     # Si no responde en 5s, falla
      retries: 5      # Después de 5 fallos consecutivos → unhealthy

  backend-auth:
    build:
      context: .  # Directorio base para el Dockerfile
      dockerfile: docker/backend-auth.Dockerfile  # Qué Dockerfile usar
    environment:
      # Estas variables las lee application.yml con ${VARIABLE}
      MYSQL_HOST: mysql  # "mysql" es el nombre DNS del contenedor MySQL en la red
      # Dentro de Docker, los contenedores se comunican por NOMBRE, no por localhost
      MYSQL_PASSWORD: ${MYSQL_ROOT_PASSWORD:-rootPassword123!}
      # ${VAR:-default} = leer del archivo .env, si no existe usar el default
    depends_on:
      mysql:
        condition: service_healthy  # NO arrancar hasta que MySQL esté healthy
    restart: unless-stopped  # Si el contenedor muere, reiniciarlo automáticamente
```

---

## 5.6 Nginx como Reverse Proxy (Línea por Línea)

```nginx
server {
    listen 80;  # Escuchar en puerto 80 (HTTP)
    server_name localhost;
    root /usr/share/nginx/html;  # Directorio con archivos de Angular
    index index.html;

    # Angular SPA: TODAS las rutas devuelven index.html
    # ¿Por qué? Angular maneja las rutas en el CLIENTE (JavaScript)
    # Sin esto: /dashboard → Nginx busca archivo "dashboard" → 404
    # Con esto: /dashboard → Nginx devuelve index.html → Angular muestra DashboardComponent
    location / {
        try_files $uri $uri/ /index.html;
        # try_files: intentar en orden:
        # 1. $uri → ¿existe el archivo exacto? (ej: /styles.css → sí, servirlo)
        # 2. $uri/ → ¿existe como directorio?
        # 3. /index.html → si nada coincide, devolver index.html (Angular se encarga)
    }

    # Proxy para el backend de autenticación
    # Cuando Angular pide /api/products → Nginx reenvía a backend-auth:8080/api/products
    location /api/ {
        proxy_pass http://backend-auth:8080/api/;
        # proxy_pass = "reenviar esta petición a otro servidor"
        # backend-auth = nombre DNS del contenedor en la red Docker
        # El navegador NUNCA habla directamente con el backend
        
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        # Pasar la IP real del cliente (sin esto, el backend ve la IP de Nginx)
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # Proxy para el servicio de correos
    location /api/mail/ {
        proxy_pass http://backend-mail:8081/api/mail/;
    }

    # Cache agresivo para archivos estáticos (JS, CSS, imágenes)
    # Angular genera nombres con hash (main.abc123.js) → si cambia el código, cambia el nombre
    # Por eso es seguro cachear 1 año: el navegador pedirá el nuevo archivo con nuevo nombre
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

---
