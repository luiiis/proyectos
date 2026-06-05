# Módulo 28: Kubernetes

## Conceptos clave
```
Pod         = 1+ contenedores (unidad mínima)
Deployment  = Define cómo ejecutar pods (réplicas, imagen, recursos)
Service     = DNS interno para acceder a pods
Ingress     = Punto de entrada desde Internet
ConfigMap   = Configuración no-sensible
Secret      = Configuración sensible (passwords, tokens)
```

## Deployment de Spring Boot
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: productos-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: productos
  template:
    spec:
      containers:
        - name: productos
          image: mi-registro/productos-service:1.0.0
          ports:
            - containerPort: 8080
          env:
            - name: DB_HOST
              value: postgres-service
            - name: DB_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: db-secrets
                  key: password
          resources:
            requests: { memory: "256Mi", cpu: "250m" }
            limits: { memory: "512Mi", cpu: "500m" }
          livenessProbe:
            httpGet: { path: /actuator/health, port: 8080 }
            initialDelaySeconds: 30
---
apiVersion: v1
kind: Service
metadata:
  name: productos-service
spec:
  selector:
    app: productos
  ports:
    - port: 8080
```

## Comandos básicos
```bash
kubectl apply -f deployment.yml
kubectl get pods
kubectl logs -f deployment/productos-service
kubectl scale deployment productos-service --replicas=5
kubectl rollout restart deployment/productos-service
```

## Ejercicios
1. Despliega tu app Spring Boot en Kubernetes (minikube)
2. Configura auto-scaling basado en CPU
3. Implementa rolling updates sin downtime
4. Configura secrets para passwords de BD
