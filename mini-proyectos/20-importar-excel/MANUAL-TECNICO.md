# Manual Técnico — Mini-Proyecto 20: Importar Excel/CSV

## Stack
| Componente | Tecnología |
|-----------|-----------|
| Backend | Spring Boot 3.3 + Apache POI |
| BD | MySQL 8 + Flyway |

## Endpoints
| Método | URL | Función |
|--------|-----|---------|
| POST | /api/importar/productos | Importar archivo CSV o Excel |
| GET | /api/importar/plantilla | Descargar plantilla de ejemplo |

## Respuesta de importación
```json
{
  "importados": 95,
  "errores": 5,
  "detalleErrores": [
    {"fila": 12, "error": "Precio inválido"},
    {"fila": 23, "error": "SKU duplicado"}
  ]
}
```

## Validaciones por fila
- Nombre obligatorio
- Precio numérico > 0
- SKU sin duplicados
- Categoría existente
