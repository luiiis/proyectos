# Cómo Ejecutar — Proyecto 14: DevOps y Despliegue

## Requisitos
- Docker + Docker Compose
- Git + cuenta GitHub
- (Opcional) servidor Linux para producción

## Desarrollo local con Docker

```cmd
cd nivel-14-devops/proyecto
docker-compose up -d
```

Esto levanta:
- MySQL en puerto 3306
- Backend en puerto 8080
- Frontend en puerto 80

Acceder: http://localhost

## Parar todo

```cmd
docker-compose down
```

## Parar todo y borrar datos

```cmd
docker-compose down -v
```

## Ver logs

```cmd
docker-compose logs -f backend
docker-compose logs -f frontend
```

## Rebuild después de cambios

```cmd
docker-compose up -d --build
```

## CI/CD con GitHub Actions

El archivo `.github/workflows/ci.yml` se ejecuta automáticamente cuando:
- Haces push a `main`
- Abres un Pull Request hacia `main`

Pipeline:
1. Backend: compila + ejecuta tests
2. Frontend: instala deps + build producción
3. Deploy: solo si los tests pasan y es push a main

## Ambientes

| Ambiente | URL | BD | Descripción |
|----------|-----|-----|-------------|
| Local | localhost:4200 | localhost:3306 | Tu máquina |
| Docker local | localhost:80 | container mysql | Todo en contenedores |
| Desarrollo | dev.tudominio.com | RDS/Cloud | Automático con CI |
| Producción | app.tudominio.com | RDS/Cloud | Manual o CI controlado |

## Checklist pre-deploy

- [ ] Tests pasan (mvn test)
- [ ] Build Angular sin errores (ng build --prod)
- [ ] Variables de entorno configuradas
- [ ] Migrations de BD revisadas
- [ ] CORS configurado para el dominio
- [ ] HTTPS activo
- [ ] Backup de BD antes de migrar
- [ ] Rollback plan definido

## Variables de entorno (producción)

```env
# Base de datos
DB_HOST=tu-servidor-mysql
DB_PORT=3306
DB_NAME=erp_db
DB_USERNAME=erp_user
DB_PASSWORD=contraseña-segura

# JWT
JWT_SECRET=clave-de-256-bits-generada-con-openssl

# Mail
MAIL_HOST=smtp.gmail.com
MAIL_USERNAME=correo@empresa.com
MAIL_PASSWORD=app-password

# Spring
SPRING_PROFILES_ACTIVE=prod
```

## Generar clave JWT segura

```bash
openssl rand -base64 64
```
