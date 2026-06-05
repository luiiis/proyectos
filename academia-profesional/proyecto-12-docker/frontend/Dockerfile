# ════════════════════════════════════════════════════════════════
# Dockerfile Frontend Angular - Multi-stage
# Imagen final: ~25MB (Nginx Alpine + archivos estáticos)
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
COPY --from=build /app/dist/app/browser /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
