# Preguntas de Entrevista - Tema: Docker y Contenedores

## Nivel Junior

### 1. ¿Qué es Docker y para qué sirve?
**Respuesta:**
Docker empaqueta una aplicación con TODAS sus dependencias en un contenedor. "Funciona en mi máquina" se convierte en "funciona en CUALQUIER máquina".
- Sin Docker: instalar Java 21, PostgreSQL 16, Redis, configurar paths... en cada máquina
- Con Docker: `docker compose up` → todo listo en 30 segundos

---

### 2. ¿Cuál es la diferencia entre imagen y contenedor?
**Respuesta:**
- **Imagen**: plantilla de solo lectura (como un CD). Se construye con Dockerfile.
- **Contenedor**: instancia ejecutándose de una imagen (como el programa corriendo desde el CD).

Puedes crear múltiples contenedores de la misma imagen (como instalar el mismo programa varias veces).

---

### 3. ¿Qué es un Dockerfile?
**Respuesta:**
Archivo con instrucciones para construir una imagen:
```dockerfile
FROM eclipse-temurin:21        # Base: Java 21
WORKDIR /app                   # Directorio de trabajo
COPY target/*.jar app.jar      # Copiar el JAR
EXPOSE 8080                    # Documentar puerto
ENTRYPOINT ["java", "-jar", "app.jar"]  # Comando al iniciar
```

---

### 4. ¿Qué es Docker Compose?
**Respuesta:**
Herramienta para definir y ejecutar múltiples contenedores como un solo sistema:
```yaml
services:
  backend:   # Spring Boot
  postgres:  # Base de datos
  redis:     # Caché
  nginx:     # Proxy/frontend
```
`docker compose up -d` levanta TODO. `docker compose down` detiene TODO.

---

### 5. ¿Cuál es la diferencia entre COPY y ADD en Dockerfile?
**Respuesta:**
- `COPY`: copia archivos del host al contenedor (simple, recomendado)
- `ADD`: como COPY pero además: descomprime .tar.gz y puede descargar URLs

Usar `COPY` siempre, excepto cuando necesites descomprimir automáticamente.

---

## Nivel Mid

### 6. ¿Qué es un multi-stage build y por qué es importante?
**Respuesta:**
Usar múltiples `FROM` en un Dockerfile para separar compilación de ejecución:
- Stage 1: imagen con Maven + JDK (800MB) → compila el código
- Stage 2: imagen con solo JRE (200MB) → ejecuta el JAR

Resultado: imagen final 4x más pequeña, sin herramientas de compilación (más segura).

---

### 7. ¿Cómo persisten datos en Docker si los contenedores son efímeros?
**Respuesta:**
Con **volúmenes**:
- `volumes: [postgres-data:/var/lib/postgresql/data]`
- Los datos viven fuera del contenedor
- Sobreviven a `docker compose down` (pero no a `down -v`)

Tipos:
- Named volume: Docker lo gestiona (recomendado para BD)
- Bind mount: directorio del host mapeado al contenedor (para desarrollo)

---

### 8. ¿Cuál es la diferencia entre CMD y ENTRYPOINT?
**Respuesta:**
- `ENTRYPOINT`: comando que SIEMPRE se ejecuta (no se puede sobreescribir fácilmente)
- `CMD`: argumentos por defecto (se pueden sobreescribir al hacer `docker run`)

```dockerfile
ENTRYPOINT ["java", "-jar"]  # Siempre ejecuta java -jar
CMD ["app.jar"]              # Por defecto: app.jar, pero puedes pasar otro
# docker run mi-imagen otro.jar → ejecuta: java -jar otro.jar
```

---

### 9. ¿Cómo optimizas el build de Docker para que sea rápido?
**Respuesta:**
Aprovechar el **layer cache**:
```dockerfile
# Las capas que cambian POCO van primero:
COPY pom.xml .                    # Cambia raramente
RUN mvn dependency:go-offline     # Cache de dependencias (lento, pero se cachea)

# Las capas que cambian MUCHO van al final:
COPY src ./src                    # Cambia en cada build
RUN mvn package                   # Se ejecuta cada vez
```
Si solo cambias código fuente, Docker reutiliza el cache de dependencias.

---

### 10. ¿Qué son las redes en Docker y por qué importan?
**Respuesta:**
Docker crea redes virtuales donde los contenedores se comunican por nombre de servicio:
```yaml
services:
  backend:
    environment:
      DB_HOST: postgres  # ← nombre del servicio, no localhost ni IP
  postgres:
    # ...
```
- Los contenedores en la misma red se ven entre sí
- Los que están en redes diferentes están aislados (seguridad)

---

## Nivel Senior

### 11. ¿Cómo manejas secretos en Docker en producción?
**Respuesta:**
NUNCA en variables de entorno del Dockerfile ni en docker-compose.yml (quedan en la imagen/historial).

Opciones:
1. **Docker Secrets** (Swarm): archivos montados en `/run/secrets/`
2. **Vault (HashiCorp)**: secrets dinámicos con TTL
3. **AWS Secrets Manager / Parameter Store**: en cloud
4. **`.env` file** solo en desarrollo (NUNCA commitear)

---

### 12. ¿Docker en producción vs Kubernetes?
**Respuesta:**
- **Docker Compose**: 1 máquina, pocos contenedores, desarrollo/staging
- **Kubernetes**: múltiples máquinas, auto-scaling, auto-healing, rolling updates, producción enterprise

Docker Compose NO tiene: auto-restart entre nodos, load balancing, rolling updates, horizontal scaling. Para producción seria → Kubernetes o ECS.
