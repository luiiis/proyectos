# Manual Técnico — Mini-Proyecto 29: Auditoría Automática

## Stack
| Componente | Tecnología |
|-----------|-----------|
| Backend | Spring Boot 3.3 + Spring AOP |
| BD | MySQL 8 + Flyway |

## Cómo funciona
- Se usa AOP (Aspect Oriented Programming) con @Aspect
- Se interceptan métodos anotados con @Auditable
- Se registra automáticamente: tabla, operación, datos antes/después, usuario, IP, fecha
- El código de negocio NO se modifica (la auditoría es transparente)

## Endpoints
| Método | URL | Función |
|--------|-----|---------|
| GET | /api/auditoria | Listar registros de auditoría |
| GET | /api/auditoria?tabla=productos | Filtrar por tabla |
| GET | /api/auditoria?usuario=admin | Filtrar por usuario |

## Tabla SQL
```sql
CREATE TABLE auditoria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tabla VARCHAR(50),
    operacion VARCHAR(10),
    registro_id BIGINT,
    datos_antes JSON,
    datos_despues JSON,
    usuario VARCHAR(50),
    ip VARCHAR(45),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```
