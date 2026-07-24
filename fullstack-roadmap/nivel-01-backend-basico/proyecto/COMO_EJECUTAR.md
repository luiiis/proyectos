# Cómo Ejecutar — Proyecto 1: API de Saludos

## Requisitos
- Java 21
- Maven 3.9+

## Ejecutar

```cmd
cd nivel-01-backend-basico/proyecto/backend
mvn spring-boot:run
```

## Probar

```bash
# Saludo general
curl http://localhost:8080/api/saludos

# Saludo personalizado
curl http://localhost:8080/api/saludos/Carlos

# Saludo en JSON
curl http://localhost:8080/api/saludos/json/Maria

# Calculadora
curl "http://localhost:8080/api/saludos/calculadora/sumar?a=10&b=20"

# Hora actual
curl http://localhost:8080/api/saludos/hora
```

## Estructura

```
src/main/java/com/softwarelee/apisaludos/
├── ApiSaludosApplication.java  ← Clase principal
└── controller/
    └── SaludoController.java   ← 5 endpoints
```
