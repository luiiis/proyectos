# Módulo 26: Docker para Angular

## 1. Dockerfile Multi-Stage

```dockerfile
# ============ Stage 1: Build ============
FROM node:20-alpine AS build

WORKDIR /app

# Copiar package.json primero (cache de dependencias)
COPY package.json package-lock.json ./
RUN npm ci

# Copiar código y compilar
COPY . .
RUN npm run build -- --configuration=production

# ============ Stage 2: Serve ============
FROM nginx:alpine AS production

# Copiar configuración de Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copiar build de Angular
COPY --from=build /app/dist/mi-app/browser /usr/share/nginx/html

# Puerto
EXPOSE 80

# Health check
HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget --quiet --tries=1 --spider http://localhost/ || exit 1

CMD ["nginx", "-g", "daemon off;"]
```

## 2. Nginx Configuration (SPA)

```nginx
# nginx.conf
server {
    listen 80;
    server_name localhost;
    root /usr/share/nginx/html;
    index index.html;

    # Gzip compression
    gzip on;
    gzip_types text/plain text/css application/json application/javascript text/xml;
    gzip_min_length 1000;

    # Cache para assets estáticos
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff2)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }

    # SPA: redirigir todas las rutas a index.html
    location / {
        try_files $uri $uri/ /index.html;
    }

    # Proxy para API (evitar CORS)
    location /api/ {
        proxy_pass http://backend:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    # Security headers
    add_header X-Frame-Options "SAMEORIGIN" always;
    add_header X-Content-Type-Options "nosniff" always;
    add_header X-XSS-Protection "1; mode=block" always;
}
```

## 3. Docker Compose (Full Stack)

```yaml
# docker-compose.yml
services:
  frontend:
    build:
      context: ./frontend
      dockerfile: Dockerfile
    ports:
      - "80:80"
    depends_on:
      - backend

  backend:
    build:
      context: ./backend
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/app
    depends_on:
      - db

  db:
    image: postgres:16-alpine
    environment:
      POSTGRES_DB: app
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: secret
    volumes:
      - pgdata:/var/lib/postgresql/data

volumes:
  pgdata:
```

## 4. Comandos Útiles

```bash
# Construir imagen
docker build -t mi-app-frontend .

# Ejecutar contenedor
docker run -d -p 80:80 --name frontend mi-app-frontend

# Docker Compose
docker compose up -d          # levantar todo
docker compose logs frontend  # ver logs
docker compose down           # detener todo

# Optimización: ver tamaño de imagen
docker images mi-app-frontend
# Con multi-stage: ~25MB (nginx:alpine + build estático)
# Sin multi-stage: ~1.2GB (node + node_modules + build)
```

## 5. .dockerignore

```
node_modules
dist
.git
.angular
*.md
.env
```

## 6. Ejercicios

1. Crea un Dockerfile multi-stage para tu app Angular (build + nginx).
2. Configura nginx.conf con SPA routing, gzip y proxy para API.
3. Crea un docker-compose.yml con frontend + backend + base de datos.
4. Optimiza el tamaño de la imagen Docker (comparar con/sin multi-stage).
5. Agrega health check y security headers a la configuración de Nginx.

---

## Siguiente Módulo
→ [27-CI/CD](../27-cicd/README.md)
