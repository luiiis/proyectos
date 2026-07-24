# Mini-Proyecto 29: Auditoría Automática

## Qué aprenderás
- Registrar automáticamente quién hizo qué y cuándo
- Interceptar operaciones CRUD sin modificar el código existente
- Guardar datos antes/después del cambio
- Consultar historial de un registro
- Filtrar por usuario, fecha, tabla, operación

## Tabla de auditoría
```sql
CREATE TABLE auditoria (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    tabla         VARCHAR(50) NOT NULL,
    operacion     VARCHAR(10) NOT NULL,  -- INSERT, UPDATE, DELETE
    registro_id   BIGINT,
    datos_antes   JSON,
    datos_despues JSON,
    usuario       VARCHAR(50),
    ip            VARCHAR(45),
    fecha         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_tabla ON auditoria(tabla);
CREATE INDEX idx_audit_usuario ON auditoria(usuario);
CREATE INDEX idx_audit_fecha ON auditoria(fecha);
```

## Implementación con AOP (Aspect-Oriented Programming)
```java
@Aspect
@Component
public class AuditoriaAspect {

    @AfterReturning(pointcut = "@annotation(Auditable)", returning = "result")
    public void auditar(JoinPoint joinPoint, Object result) {
        // Extraer: usuario actual, tabla, operación, datos
        // Guardar en tabla auditoria
    }
}

// Uso: solo agregar @Auditable al método
@Auditable(operacion = "CREAR", tabla = "productos")
public Producto crear(Producto p) { ... }
```

## Endpoints de consulta
```
GET /api/auditoria?tabla=productos&usuario=admin&desde=2026-07-01
GET /api/auditoria/registro/productos/1  → historial de cambios del producto 1
```

## Ejemplo de registro
```json
{
  "id": 456,
  "tabla": "productos",
  "operacion": "UPDATE",
  "registro_id": 1,
  "datos_antes": {"precio": 18999.00, "existencia": 25},
  "datos_despues": {"precio": 19999.00, "existencia": 25},
  "usuario": "admin",
  "ip": "192.168.1.100",
  "fecha": "2026-07-23T14:30:00"
}
```
