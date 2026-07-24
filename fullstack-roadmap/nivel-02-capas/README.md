# Nivel 2: Backend Organizado por Capas — Proyecto 2: Administrador de Tareas

## Objetivo
Crear un CRUD completo sin base de datos (datos en memoria) y aprender a separar el código en capas profesionales.

## Funcionalidades
- Crear tareas
- Consultar todas las tareas
- Consultar una tarea por ID
- Modificar tareas
- Eliminar tareas
- Marcar tareas como completadas

## 5 Versiones (progresivas)

| Versión | Qué agrega | Archivos nuevos |
|---------|------------|-----------------|
| V1 | Todo en el Controller | 1 archivo |
| V2 | Separar Controller → Service → Repository | 4 archivos |
| V3 | Agregar DTO (separar lo que recibe de lo que devuelve) | +2 archivos |
| V4 | Agregar validaciones (@Valid) | +1 archivo config |
| V5 | Agregar manejo global de errores | +2 archivos |

## Conceptos que aprenderás
- CRUD (Create, Read, Update, Delete)
- Inyección de dependencias (@Autowired / constructor)
- Interfaces y sus implementaciones
- DTO (Data Transfer Object)
- Mapeadores (Entity ↔ DTO)
- Validaciones con Jakarta Validation
- Excepciones personalizadas
- @RestControllerAdvice para errores globales
- Códigos HTTP correctos (200, 201, 204, 400, 404)

## Endpoints

```
POST   /api/tareas              → Crear tarea
GET    /api/tareas              → Listar todas
GET    /api/tareas/{id}         → Buscar por ID
PUT    /api/tareas/{id}         → Modificar tarea
DELETE /api/tareas/{id}         → Eliminar tarea
PATCH  /api/tareas/{id}/completar → Marcar como completada
```

## Documentación que crearemos
- Documento de requerimientos
- Diagrama de capas
- Casos de uso
- Contrato de la API (endpoints + request + response)
- Evidencias de Postman
- Manual técnico
- Historial de versiones
