# Ejercicios y Retos - Proyecto 16: Kubernetes

## Reto 1: Escalar manualmente
Escala tu backend de 2 a 5 réplicas y verifica:
```bash
kubectl scale deployment backend --replicas=5 -n academia
kubectl get pods -n academia  # Ver 5 pods corriendo
# Hacer requests y ver que se distribuyen entre los 5
```

**Lo que practicas:** Scaling, pods, load balancing

---

## Reto 2: ConfigMaps y Secrets
Externaliza la configuración:
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: backend-config
data:
  SPRING_PROFILES_ACTIVE: "production"
  SERVER_PORT: "8080"
---
apiVersion: v1
kind: Secret
metadata:
  name: db-credentials
type: Opaque
data:
  DB_PASSWORD: cG9zdGdyZXMxMjM=  # base64 encoded
```

**Lo que practicas:** ConfigMaps, Secrets, separación de config y código

---

## Reto 3: Rolling Update con zero downtime
Actualiza la imagen del backend sin downtime:
```bash
kubectl set image deployment/backend backend=mi-api:2.0.0 -n academia
kubectl rollout status deployment/backend -n academia
# Si falla:
kubectl rollout undo deployment/backend -n academia
```
Verifica que durante el update no hay errores 5xx.

**Lo que practicas:** Rolling updates, rollback, disponibilidad

---

## Reto 4: HPA (Horizontal Pod Autoscaler)
Configura auto-scaling basado en CPU:
```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
spec:
  scaleTargetRef:
    name: backend
  minReplicas: 2
  maxReplicas: 10
  metrics:
    - type: Resource
      resource:
        name: cpu
        target:
          type: Utilization
          averageUtilization: 70
```
Genera carga y observa cómo se crean pods automáticamente.

**Lo que practicas:** Auto-scaling, resource management, elasticidad

---

## Reto 5: Probes (Liveness + Readiness)
Configura health probes para que K8s sepa cuándo tu app está sana:
```yaml
livenessProbe:     # ¿Está vivo? Si no → restart
  httpGet:
    path: /actuator/health/liveness
    port: 8080
  initialDelaySeconds: 30
  
readinessProbe:    # ¿Puede recibir tráfico? Si no → no le envía requests
  httpGet:
    path: /actuator/health/readiness
    port: 8080
  initialDelaySeconds: 10
```

**Lo que practicas:** Health probes, resiliencia, graceful startup

---

## Reto 6 (Avanzado): Helm Chart
Empaqueta tu deployment como un Helm chart reutilizable:
```
mi-app-chart/
├── Chart.yaml
├── values.yaml         # Variables configurables
├── templates/
│   ├── deployment.yaml
│   ├── service.yaml
│   └── ingress.yaml
```
Instalar: `helm install mi-app ./mi-app-chart --set replicas=3`

**Lo que practicas:** Helm, packaging, despliegue parametrizado
