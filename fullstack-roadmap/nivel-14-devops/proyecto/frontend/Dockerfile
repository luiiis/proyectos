# ════════════════════════════════════════════════════════════════
# Dockerfile Frontend — Angular + Nginx
# Stage 1: Compilar Angular (imagen con Node)
# Stage 2: Servir con Nginx (imagen ultra-ligera ~25MB)
# ════════════════════════════════════════════════════════════════

# ═══ STAGE 1: BUILD ═══
FROM node:22-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci --silent
COPY . .
RUN npm run build -- --configuration production

# ═══ STAGE 2: SERVE ═══
FROM nginx:alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/*/browser /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
