# Módulo 26: Docker para Angular - Manual Técnico

## ¿Qué construimos?
Dockerfile multi-stage que compila Angular y lo sirve con Nginx.

## Cómo ejecutar
```bash
cd frontend-learning/26-docker/proyecto
docker build -t mi-angular-app .
docker run -p 80:80 mi-angular-app
# Abrir: http://localhost
```

## Dockerfile explicado
Ver archivo `Dockerfile` con comentarios.
