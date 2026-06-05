# ════════════════════════════════════════════════════════════════
# Dockerfile para Angular - Multi-stage Build
# ════════════════════════════════════════════════════════════════

# ═══════ STAGE 1: BUILD (compilar Angular) ═══════
# node:22-alpine: Node.js 22 sobre Alpine Linux (imagen ligera)
FROM node:22-alpine AS build

# Directorio de trabajo
WORKDIR /app

# TRUCO DE CACHÉ: copiar package.json PRIMERO
# Si no cambian las dependencias, Docker reutiliza esta capa
COPY package*.json ./

# npm ci: instalación LIMPIA (más rápida y reproducible que npm install)
# Usa exactamente las versiones de package-lock.json
RUN npm ci

# Ahora copiar el código fuente (cambia frecuentemente)
COPY . .

# Compilar Angular para producción
# Genera archivos estáticos optimizados en dist/
# - Minificación (quita espacios, renombra variables)
# - Tree-shaking (elimina código no usado)
# - AOT (compila templates en build time)
# - Hashing de archivos (main.abc123.js → cache busting)
RUN npm run build -- --configuration production

# ═══════ STAGE 2: RUNTIME (servir con Nginx) ═══════
# nginx:alpine: servidor web ultra-ligero (~25MB)
FROM nginx:alpine

# Copiar configuración personalizada de Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copiar SOLO los archivos compilados (no node_modules, no código fuente)
# El path depende del nombre de tu proyecto en angular.json
COPY --from=build /app/dist/mi-app/browser /usr/share/nginx/html

# Puerto que expone Nginx
EXPOSE 80

# Nginx en primer plano (requerido por Docker)
CMD ["nginx", "-g", "daemon off;"]
