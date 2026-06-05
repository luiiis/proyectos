# Bitácora de Construcción - Proyecto 16: Kubernetes

## ¿Qué problema resuelve Kubernetes sobre Docker Compose?

| Aspecto | Docker Compose | Kubernetes |
|---------|---------------|------------|
| Si un contenedor muere | Se queda muerto | Se recrea automáticamente |
| Escalar | Manual (docker compose scale) | Automático (HPA por CPU/RAM) |
| Rolling updates | Downtime | Zero downtime |
| Secrets | .env en texto plano | Encriptados en etcd |
| Load balancing | No incluido | Incluido (Service) |
| Health checks | Reinicia contenedor | Saca de rotación + reinicia |
| Multi-nodo | No | Sí (cluster de máquinas) |

## Conceptos implementados

### Pod
Unidad mínima. Contiene 1+ contenedores. Efímero (puede morir y recrearse).

### Deployment
Define: qué imagen, cuántas réplicas, qué recursos, health checks.
Kubernetes GARANTIZA que siempre haya N réplicas corriendo.

### Service
DNS interno estable. Los pods cambian de IP, el Service siempre apunta a los correctos.
`backend-service:8080` → resuelve a los pods del backend.

### HorizontalPodAutoscaler (HPA)
Si CPU > 70% → crear más pods (hasta 5). Si baja → reducir (mínimo 2).

### Secret
Passwords encriptados. Se inyectan como variables de entorno en los pods.

### PersistentVolumeClaim (PVC)
Disco persistente para PostgreSQL. Si el pod muere, los datos sobreviven.

### Ingress
Punto de entrada desde Internet. Rutea por path:
- `/api/*` → backend-service
- `/*` → frontend-service

## Diagrama
```
Internet → Ingress → Service → Pod(s)
                                 ↑
                          Deployment (mantiene N réplicas)
                                 ↑
                          HPA (escala automáticamente)
```
