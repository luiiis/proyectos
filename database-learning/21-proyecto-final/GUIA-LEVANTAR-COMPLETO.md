# 🚀 Guía Completa: Levantar el Proyecto Final (BD + Backend + Frontend)

## Visión General

Este proyecto levanta un sistema empresarial completo con 3 capas:

```
┌─────────────────────────────────────────────────────────────┐
│  CAPA 1: BASE DE DATOS (PostgreSQL)                          │
│  - 16 tablas con relaciones                                  │
│  - 10,000+ registros de prueba                               │
│  - Triggers de auditoría                                     │
│  - Puerto: 5432                                              │
├─────────────────────────────────────────────────────────────┤
│  CAPA 2: BACKEND (Spring Boot + Java 21)                     │
│  - API REST con CRUD completo                                │
│  - Conexión a PostgreSQL via JPA                             │
│  - Swagger/OpenAPI documentación                             │
│  - Puerto: 8080                                              │
├─────────────────────────────────────────────────────────────┤
│  CAPA 3: FRONTEND (Angular)                                  │
│  - Dashboard con KPIs                                        │
│  - Tabla de productos con filtros                            │
│  - Formularios de alta/edición                               │
│  - Puerto: 4200 (dev) / 80 (Docker)                         │
└─────────────────────────────────────────────────────────────┘
```

---

## Prerrequisitos

```bash
# Verificar que tienes instalado:
docker --version          # Docker 24+
docker compose version    # Docker Compose v2+
java -version             # Java 21+ (para desarrollo local)
mvn -version              # Maven 3.9+
node -version             # Node.js 20+ (para frontend)
ng version                # Angular CLI 17+
```

Si te falta algo:
- Docker: https://www.docker.com/products/docker-desktop/
- Java 21: https://adoptium.net/
- Node.js: https://nodejs.org/
- Angular CLI: `npm install -g @angular/cli`

---

## PASO 1: Levantar la Base de Datos

### 1.1 Iniciar PostgreSQL con Docker

```bash
cd database-learning/21-proyecto-final/proyecto

# Levantar SOLO PostgreSQL primero
docker compose up postgres -d

# Verificar que está corriendo
docker compose ps
# Debe decir: "healthy"

# Esperar 10 segundos y verificar datos
docker exec -it proyecto-final-postgres psql -U postgres -d empresa_db \
  -c "SELECT tablename FROM pg_tables WHERE schemaname='public' ORDER BY tablename;"
```

### 1.2 Verificar que las tablas existen

```bash
docker exec -it proyecto-final-postgres psql -U postgres -d empresa_db
```

Dentro de psql ejecuta:
```sql
-- Ver todas las tablas
\dt

-- Verificar datos
SELECT 'sucursales' AS tabla, COUNT(*) AS registros FROM sucursales
UNION ALL SELECT 'empleados', COUNT(*) FROM empleados
UNION ALL SELECT 'clientes', COUNT(*) FROM clientes
UNION ALL SELECT 'productos', COUNT(*) FROM productos
UNION ALL SELECT 'ventas', COUNT(*) FROM ventas
UNION ALL SELECT 'detalle_venta', COUNT(*) FROM detalle_venta;

-- Resultado esperado:
-- sucursales     | 5
-- empleados      | 100
-- clientes       | 1000
-- productos      | 500
-- ventas         | 10000
-- detalle_venta  | ~24000

\q
```

### 1.3 Si las tablas NO existen (primera vez o reset)

```bash
# Los scripts se ejecutan automáticamente al crear el contenedor.
# Si necesitas re-ejecutarlos manualmente:

docker exec -i proyecto-final-postgres psql -U postgres -d empresa_db \
  < ../../19-docker/init/postgres/01-schema.sql

docker exec -i proyecto-final-postgres psql -U postgres -d empresa_db \
  < ../../19-docker/init/postgres/02-seed-data.sql
```

### 1.4 Crear triggers de auditoría (módulo 10)

```bash
docker exec -i proyecto-final-postgres psql -U postgres -d empresa_db <<'EOF'
-- Función de auditoría genérica
CREATE OR REPLACE FUNCTION fn_auditoria()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO auditoria (tabla, operacion, registro_id, datos_despues, usuario, fecha)
        VALUES (TG_TABLE_NAME, 'INSERT', NEW.id, to_jsonb(NEW), current_user, NOW());
        RETURN NEW;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO auditoria (tabla, operacion, registro_id, datos_antes, datos_despues, usuario, fecha)
        VALUES (TG_TABLE_NAME, 'UPDATE', NEW.id, to_jsonb(OLD), to_jsonb(NEW), current_user, NOW());
        RETURN NEW;
    ELSIF TG_OP = 'DELETE' THEN
        INSERT INTO auditoria (tabla, operacion, registro_id, datos_antes, usuario, fecha)
        VALUES (TG_TABLE_NAME, 'DELETE', OLD.id, to_jsonb(OLD), current_user, NOW());
        RETURN OLD;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Aplicar a productos
DROP TRIGGER IF EXISTS trg_audit_productos ON productos;
CREATE TRIGGER trg_audit_productos
    AFTER INSERT OR UPDATE OR DELETE ON productos
    FOR EACH ROW EXECUTE FUNCTION fn_auditoria();

-- Aplicar a ventas
DROP TRIGGER IF EXISTS trg_audit_ventas ON ventas;
CREATE TRIGGER trg_audit_ventas
    AFTER INSERT OR UPDATE ON ventas
    FOR EACH ROW EXECUTE FUNCTION fn_auditoria();

SELECT 'Triggers de auditoría creados ✓' AS resultado;
EOF
```

---

## PASO 2: Levantar el Backend (Spring Boot)

### Opción A: Desarrollo local (recomendado para aprender)

```bash
cd database-learning/21-proyecto-final/proyecto/app

# Compilar y ejecutar
mvn spring-boot:run

# Debes ver:
# "Started Application in X seconds"
# "Tomcat started on port 8080"
```

### Opción B: Con Docker

```bash
cd database-learning/21-proyecto-final/proyecto

# Levantar backend + postgres
docker compose up postgres api --build -d

# Ver logs del backend
docker compose logs -f api
```

### 2.1 Verificar que el backend funciona

```bash
# Health check
curl http://localhost:8080/actuator/health 2>/dev/null || curl http://localhost:8080/api/productos?size=3

# Listar productos (primeros 3)
curl -s http://localhost:8080/api/productos?size=3 | python -m json.tool

# Buscar por nombre
curl -s "http://localhost:8080/api/productos/buscar?q=laptop"

# Productos con stock bajo
curl -s http://localhost:8080/api/productos/stock-bajo?minimo=10

# Productos más vendidos
curl -s http://localhost:8080/api/productos/mas-vendidos?limite=5

# Crear un producto nuevo
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Producto Test","precio":999.99,"stock":50,"sku":"TEST-001","categoriaId":1,"proveedorId":1}'

# Swagger UI (documentación interactiva)
# Abrir en navegador: http://localhost:8080/swagger-ui.html
```

---

## PASO 3: Levantar el Frontend (Angular)

### 3.1 Crear el proyecto Angular (primera vez)

```bash
cd database-learning/21-proyecto-final/proyecto

# Crear proyecto Angular
ng new frontend --standalone --style=scss --routing --skip-tests
cd frontend

# Instalar Angular Material
ng add @angular/material

# Instalar PrimeNG (opcional)
npm install primeng primeicons
```

### 3.2 Configurar conexión al backend

Editar `frontend/src/environments/environment.ts`:
```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```

### 3.3 Crear servicio de productos

```bash
ng generate service core/services/producto
```

Editar `frontend/src/app/core/services/producto.service.ts`:
```typescript
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Producto {
  id: number;
  nombre: string;
  descripcion: string;
  precio: number;
  stock: number;
  sku: string;
  categoriaId: number;
  activo: boolean;
}

@Injectable({ providedIn: 'root' })
export class ProductoService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/productos`;

  listar(page = 0, size = 20): Observable<any> {
    return this.http.get(`${this.url}?page=${page}&size=${size}`);
  }

  buscar(nombre: string): Observable<Producto[]> {
    return this.http.get<Producto[]>(`${this.url}/buscar?q=${nombre}`);
  }

  crear(producto: Partial<Producto>): Observable<Producto> {
    return this.http.post<Producto>(this.url, producto);
  }

  actualizar(id: number, producto: Partial<Producto>): Observable<Producto> {
    return this.http.put<Producto>(`${this.url}/${id}`, producto);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
```

### 3.4 Ejecutar el frontend

```bash
cd frontend
ng serve
# → http://localhost:4200
```

---

## PASO 4: Levantar TODO junto (Docker Compose)

### 4.1 Comando único para todo

```bash
cd database-learning/21-proyecto-final/proyecto

# Levantar las 3 capas
docker compose up --build -d

# Ver estado
docker compose ps

# Ver logs
docker compose logs -f
```

### 4.2 Verificar que todo funciona

```
✅ PostgreSQL: docker exec -it proyecto-final-postgres psql -U postgres -d empresa_db -c "SELECT COUNT(*) FROM productos;"
✅ Backend:    curl http://localhost:8080/api/productos?size=2
✅ Swagger:    Abrir http://localhost:8080/swagger-ui.html
✅ Frontend:   Abrir http://localhost:4200 (si está en Docker: http://localhost)
```

---

## PASO 5: Operaciones Comunes

### Resetear la base de datos (borrar todo y recrear)
```bash
docker compose down -v
docker compose up -d
# Esperar 30 segundos a que se re-ejecuten los scripts de init
```

### Ver logs del backend
```bash
docker compose logs -f api
```

### Conectar a la BD con herramienta GUI
```
DBeaver / pgAdmin / Adminer:
  Host: localhost
  Puerto: 5432
  Base de datos: empresa_db
  Usuario: postgres
  Password: postgres123
```

### Ejecutar queries directamente
```bash
docker exec -it proyecto-final-postgres psql -U postgres -d empresa_db

-- Dentro de psql:
SELECT nombre, precio, stock FROM productos ORDER BY precio DESC LIMIT 10;
SELECT * FROM auditoria ORDER BY fecha DESC LIMIT 5;
\q
```

### Reconstruir solo el backend (después de cambiar código)
```bash
docker compose up --build api -d
```

---

## Troubleshooting

| Problema | Causa | Solución |
|----------|-------|----------|
| `connection refused :5432` | PostgreSQL no ha arrancado | `docker compose up postgres -d` y esperar 10s |
| `relation "productos" does not exist` | Scripts de init no se ejecutaron | `docker compose down -v` y recrear |
| `mvn: command not found` | Maven no instalado | Instalar Maven o usar Docker |
| Backend no conecta a BD | URL incorrecta | Verificar `application.yml` → `localhost:5432` (local) o `postgres:5432` (Docker) |
| CORS error en frontend | Backend no permite el origen | Verificar `@CrossOrigin` en controllers |
| Puerto 8080 ocupado | Otro servicio lo usa | Cambiar puerto en `application.yml` o matar el proceso |

---

## Resumen de Puertos

| Servicio | Puerto | URL |
|----------|--------|-----|
| PostgreSQL | 5432 | `jdbc:postgresql://localhost:5432/empresa_db` |
| Backend API | 8080 | http://localhost:8080/api/productos |
| Swagger | 8080 | http://localhost:8080/swagger-ui.html |
| Frontend | 4200 | http://localhost:4200 |
| Redis | 6379 | localhost:6379 |
