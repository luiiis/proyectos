# Manual Técnico — Mini-Proyecto 16: Subir Imágenes

## Stack
| Componente | Tecnología |
|-----------|-----------|
| Backend | Spring Boot 3.3 |
| Almacenamiento | Disco local (volumen Docker) |

## Validaciones
- Solo formatos: JPG, PNG, WebP
- Tamaño máximo: 5MB
- Nombre generado con UUID (evita colisiones)

## Endpoints
| Método | URL | Función |
|--------|-----|---------|
| POST | /api/archivos/subir | Subir una imagen |
| GET | /api/archivos/listar | Ver archivos subidos |
| GET | /uploads/{nombre} | Ver imagen directamente |
