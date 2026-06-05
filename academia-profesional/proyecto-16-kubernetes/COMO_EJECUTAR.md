# Cómo Ejecutar - Proyecto 16: Kubernetes

## Prerrequisitos
```bash
# Instalar minikube (Kubernetes local)
# Windows: choco install minikube
# Mac: brew install minikube
# Linux: curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64

# Iniciar cluster
minikube start --memory=4096 --cpus=2

# Verificar
kubectl cluster-info
kubectl get nodes
```

## Desplegar
```bash
cd academia-profesional/proyecto-16-kubernetes

# 1. Crear namespace
kubectl apply -f k8s/namespace.yml

# 2. Crear secrets (passwords)
kubectl apply -f k8s/secrets.yml

# 3. Desplegar PostgreSQL
kubectl apply -f k8s/postgres.yml

# 4. Desplegar Backend
kubectl apply -f k8s/backend.yml

# 5. Desplegar Frontend
kubectl apply -f k8s/frontend.yml

# 6. Crear Ingress (punto de entrada)
kubectl apply -f k8s/ingress.yml

# Ver estado
kubectl get all -n academia
```

## Acceder
```bash
# Obtener URL del servicio
minikube service frontend-service -n academia --url

# O con port-forward
kubectl port-forward svc/backend-service 8080:8080 -n academia
kubectl port-forward svc/frontend-service 4200:80 -n academia
```

## Comandos útiles
```bash
kubectl get pods -n academia              # Ver pods
kubectl logs -f deploy/backend -n academia # Ver logs
kubectl scale deploy/backend --replicas=3 -n academia  # Escalar
kubectl describe pod <nombre> -n academia  # Detalles de un pod
kubectl exec -it <pod> -n academia -- sh   # Entrar a un pod
```
