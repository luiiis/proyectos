# Cómo Ejecutar — Proyecto 2: Administrador de Tareas

## Requisitos
- Java 21 (`java -version`)
- Maven 3.9+ (`mvn -version`)

## Ejecutar

```cmd
cd nivel-02-capas/proyecto/backend
mvn spring-boot:run
```

Debes ver: `Started Application in X seconds`

## Probar con Postman o curl

### Crear tarea
```bash
curl -X POST http://localhost:8080/api/tareas \
  -H "Content-Type: application/json" \
  -d '{"titulo": "Aprender Spring Boot", "descripcion": "Nivel 2 del roadmap"}'
```

### Listar todas
```bash
curl http://localhost:8080/api/tareas
```

### Buscar por ID
```bash
curl http://localhost:8080/api/tareas/1
```

### Modificar
```bash
curl -X PUT http://localhost:8080/api/tareas/1 \
  -H "Content-Type: application/json" \
  -d '{"titulo": "Aprender Spring Boot (actualizado)", "descripcion": "Ya casi"}'
```

### Completar
```bash
curl -X PATCH http://localhost:8080/api/tareas/1/completar
```

### Eliminar
```bash
curl -X DELETE http://localhost:8080/api/tareas/1
```

### Probar validación (título vacío → error 400)
```bash
curl -X POST http://localhost:8080/api/tareas \
  -H "Content-Type: application/json" \
  -d '{"titulo": "", "descripcion": "Sin título"}'
```

### Probar 404 (ID que no existe)
```bash
curl http://localhost:8080/api/tareas/999
```

## Estructura del código

```
src/main/java/com/softwarelee/tareas/
├── Application.java           ← Punto de entrada
├── controller/
│   └── TareaController.java  ← Recibe HTTP requests
├── service/
│   └── TareaService.java     ← Lógica de negocio
├── repository/
│   ├── TareaRepository.java      ← Interface (contrato)
│   └── TareaRepositoryImpl.java  ← Implementación en memoria
├── model/
│   └── Tarea.java             ← Entidad de negocio
├── dto/
│   ├── TareaRequest.java     ← Lo que el cliente envía
│   └── TareaResponse.java    ← Lo que el servidor devuelve
└── exception/
    ├── TareaNotFoundException.java   ← Error 404 custom
    └── GlobalExceptionHandler.java   ← Manejo global de errores
```

## Diagrama de capas

```
REQUEST → Controller → Service → Repository → Memoria (Map)
                          ↓
                    DTO ↔ Model (conversión)
                          ↓
RESPONSE ← Controller ← Service
```
