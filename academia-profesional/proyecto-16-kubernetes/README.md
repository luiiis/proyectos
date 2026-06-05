# Proyecto 16: Kubernetes

## ¿Qué construimos?
Despliegue del sistema en Kubernetes con: auto-scaling, health checks, secrets, persistent storage.

## Componentes
- Namespace (aislamiento)
- Secrets (passwords encriptados)
- PostgreSQL Deployment + PVC (datos persistentes)
- Backend Deployment (2 réplicas + HPA)
- Frontend Deployment (2 réplicas)
- Ingress (punto de entrada)

## Ejecutar
```bash
# Requisito: minikube o cluster K8s
minikube start --memory=4096

cd academia-profesional/proyecto-16-kubernetes
kubectl apply -f k8s/namespace.yml
kubectl apply -f k8s/secrets.yml
kubectl apply -f k8s/postgres.yml
kubectl apply -f k8s/backend.yml
kubectl apply -f k8s/frontend.yml
kubectl apply -f k8s/ingress.yml

kubectl get all -n academia
```

## Conceptos
- Si un pod muere → K8s lo recrea automáticamente
- Si CPU > 70% → HPA crea más pods (hasta 5)
- Rolling update → zero downtime deployments
