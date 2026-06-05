# Proyecto 19: Despliegue en AWS Cloud

## ¿Qué construimos?
Desplegar el sistema ERP completo en AWS usando servicios managed.

## Arquitectura AWS
```
Internet → Route 53 (DNS) → CloudFront (CDN)
                                    │
                              ┌─────┴─────┐
                              │    ALB    │ (Load Balancer)
                              └─────┬─────┘
                         ┌──────────┼──────────┐
                         ▼          ▼          ▼
                    ┌────────┐ ┌────────┐ ┌────────┐
                    │ ECS    │ │ ECS    │ │ ECS    │ (contenedores)
                    │Task 1  │ │Task 2  │ │Task 3  │ (auto-scaling)
                    └────┬───┘ └────┬───┘ └────┬───┘
                         │          │          │
                    ┌────┴──────────┴──────────┴────┐
                    │         VPC (red privada)       │
                    │                                 │
                    │  ┌──────────┐  ┌────────────┐  │
                    │  │   RDS    │  │ElastiCache │  │
                    │  │PostgreSQL│  │  (Redis)   │  │
                    │  └──────────┘  └────────────┘  │
                    └─────────────────────────────────┘
```

## Servicios usados

| Servicio | Reemplaza | Para qué |
|----------|-----------|----------|
| ECS Fargate | Docker en tu máquina | Ejecutar contenedores sin gestionar servidores |
| RDS PostgreSQL | PostgreSQL en Docker | BD managed (backups automáticos, réplicas) |
| ElastiCache | Redis en Docker | Caché managed (sin mantenimiento) |
| ALB | Nginx | Load balancer + health checks |
| S3 | Disco local | Almacenar archivos (imágenes, backups) |
| CloudFront | - | CDN (frontend cerca del usuario) |
| Route 53 | - | DNS (tu dominio → tu app) |
| ECR | Docker Hub | Registry privado para tus imágenes |
| CloudWatch | Prometheus+Grafana | Logs + métricas + alertas |
| Secrets Manager | .env | Passwords encriptados |

## Cómo desplegar (paso a paso)

```bash
# 1. Crear imagen Docker y subirla a ECR
aws ecr get-login-password | docker login --username AWS --password-stdin 123456.dkr.ecr.us-east-1.amazonaws.com
docker build -t mi-api ./backend
docker tag mi-api:latest 123456.dkr.ecr.us-east-1.amazonaws.com/mi-api:latest
docker push 123456.dkr.ecr.us-east-1.amazonaws.com/mi-api:latest

# 2. Crear BD en RDS
aws rds create-db-instance \
  --db-instance-identifier mi-postgres \
  --engine postgres --engine-version 16 \
  --db-instance-class db.t3.micro \
  --allocated-storage 20 \
  --master-username postgres \
  --master-user-password MiPasswordSeguro123!

# 3. Crear cluster ECS y servicio
aws ecs create-cluster --cluster-name mi-cluster
aws ecs create-service --cluster mi-cluster --service-name mi-api \
  --task-definition mi-api:1 --desired-count 2

# 4. Frontend en S3 + CloudFront
aws s3 sync ./frontend/dist/app s3://mi-frontend-bucket
aws cloudfront create-distribution --origin-domain-name mi-frontend-bucket.s3.amazonaws.com
```

## Costos estimados (free tier + mínimo)
```
ECS Fargate (2 tasks):     ~$30/mes
RDS PostgreSQL (t3.micro): ~$15/mes
ElastiCache (t3.micro):    ~$12/mes
S3 + CloudFront:           ~$5/mes
ALB:                       ~$16/mes
TOTAL:                     ~$78/mes (producción mínima)
```
