# Scripts de Base de Datos

## Resumen de Tablas

### MySQL (auth_db) - Puerto 3306
| Tabla | Propósito | Campos principales |
|-------|-----------|-------------------|
| `users` | Usuarios del sistema | id, username, email, password, first_name, last_name, phone, is_active |
| `roles` | Roles disponibles | id, name, description |
| `user_roles` | Relación usuarios-roles (M:N) | user_id, role_id |
| `password_reset_tokens` | Tokens de recuperación | id, token, email, expires_at, used |
| `email_logs` | Historial de correos enviados | id, recipient, subject, body, status, sent_at |

### Oracle XE (XEPDB1) - Puerto 1521
| Tabla | Propósito | Campos principales |
|-------|-----------|-------------------|
| `products` | Catálogo de productos | id, name, description, price, stock, category, sku, is_active |
| `inventory_movements` | Movimientos de inventario | id, product_id, type(ENTRY/EXIT), quantity, reason, created_by |
| `sales` | Ventas realizadas | id, sale_number, customer_name, total_amount, status |
| `sale_details` | Detalle de cada venta | id, sale_id, product_id, quantity, unit_price, subtotal |

### SQL Server (logs_db) - Puerto 1433
| Tabla | Propósito | Campos principales |
|-------|-----------|-------------------|
| `audit_logs` | Auditoría de acciones | id, action, entity, entity_id, username, details, ip_address |
| `system_logs` | Logs técnicos del sistema | id, level, service, message, stack_trace, duration_ms |
| `login_attempts` | Intentos de inicio de sesión | id, username, ip_address, success, failure_reason, user_agent |
| `notifications` | Notificaciones a usuarios | id, user_id, title, message, type, is_read |

---

## Credenciales de Prueba

### Usuarios pre-cargados

| Usuario | Contraseña | Roles | Descripción |
|---------|-----------|-------|-------------|
| `admin` | `admin123` | ADMIN, USER | Administrador con acceso total |
| `manager1` | `manager123` | MANAGER, USER | Gerente - gestiona productos y ventas |
| `user1` | `user123` | USER | Usuario estándar |
| `user2` | `user123` | USER | Usuario estándar |
| `user3` | `user123` | USER | Usuario estándar |

### Conexiones a Base de Datos

| BD | Host | Puerto | Usuario | Contraseña | Base/SID |
|----|------|--------|---------|-----------|----------|
| MySQL | localhost | 3306 | root | rootPassword123! | auth_db |
| Oracle | localhost | 1521 | products_user | productsPass123! | XEPDB1 |
| SQL Server | localhost | 1433 | sa | SqlServer123! | logs_db |

---

## Ejecución Manual de Scripts

Si necesitas ejecutar los scripts manualmente (sin Docker):

### MySQL
```bash
# Conectar a MySQL
mysql -h localhost -u root -p

# Ejecutar scripts
source docker/init-scripts/mysql/01-schema.sql
source docker/init-scripts/mysql/02-seed-data.sql
```

### Oracle
```bash
# Conectar con SQLPlus
sqlplus products_user/productsPass123!@localhost:1521/XEPDB1

# Ejecutar scripts
@docker/init-scripts/oracle/01-schema.sql
@docker/init-scripts/oracle/02-seed-data.sql
```

### SQL Server
```bash
# Conectar con sqlcmd
sqlcmd -S localhost,1433 -U sa -P 'SqlServer123!'

# Ejecutar scripts
:r docker/init-scripts/sqlserver/01-schema.sql
GO
:r docker/init-scripts/sqlserver/02-seed-data.sql
GO
```

O con herramientas gráficas:
- **MySQL**: MySQL Workbench, DBeaver
- **Oracle**: SQL Developer, DBeaver
- **SQL Server**: SSMS (SQL Server Management Studio), Azure Data Studio

---

## Diagrama Entidad-Relación

### MySQL (auth_db)
```
┌──────────────┐       ┌──────────────┐       ┌──────────────┐
│    users     │       │  user_roles  │       │    roles     │
├──────────────┤       ├──────────────┤       ├──────────────┤
│ id (PK)      │──┐    │ user_id (FK) │    ┌──│ id (PK)      │
│ username     │  └───>│ role_id (FK) │<───┘  │ name         │
│ email        │       └──────────────┘       │ description  │
│ password     │                              └──────────────┘
│ first_name   │
│ last_name    │       ┌────────────────────────┐
│ phone        │       │ password_reset_tokens  │
│ is_active    │       ├────────────────────────┤
│ created_at   │       │ id (PK)               │
│ updated_at   │       │ token                 │
└──────────────┘       │ email                 │
                       │ expires_at            │
                       │ used                  │
                       └────────────────────────┘

┌──────────────┐
│  email_logs  │
├──────────────┤
│ id (PK)      │
│ recipient    │
│ subject      │
│ body         │
│ status       │
│ error_message│
│ sent_at      │
└──────────────┘
```

### Oracle (products)
```
┌──────────────────┐       ┌─────────────────────┐
│    products      │       │ inventory_movements │
├──────────────────┤       ├─────────────────────┤
│ id (PK)          │<──────│ product_id (FK)     │
│ name             │       │ id (PK)             │
│ description      │       │ type (ENTRY/EXIT)   │
│ price            │       │ quantity            │
│ stock            │       │ reason              │
│ category         │       │ created_by          │
│ sku              │       │ created_at          │
│ is_active        │       └─────────────────────┘
│ created_at       │
│ updated_at       │
└──────────────────┘
        │
        │
┌───────┴──────────┐       ┌──────────────────┐
│  sale_details    │       │      sales       │
├──────────────────┤       ├──────────────────┤
│ id (PK)          │       │ id (PK)          │
│ sale_id (FK)     │──────>│ sale_number      │
│ product_id (FK)  │       │ customer_name    │
│ quantity         │       │ customer_email   │
│ unit_price       │       │ total_amount     │
│ subtotal         │       │ status           │
└──────────────────┘       │ notes            │
                           │ created_by       │
                           │ created_at       │
                           └──────────────────┘
```

### SQL Server (logs_db)
```
┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐
│   audit_logs     │  │   system_logs    │  │ login_attempts   │  │  notifications   │
├──────────────────┤  ├──────────────────┤  ├──────────────────┤  ├──────────────────┤
│ id (PK)          │  │ id (PK)          │  │ id (PK)          │  │ id (PK)          │
│ action           │  │ level            │  │ username         │  │ user_id          │
│ entity           │  │ service          │  │ ip_address       │  │ title            │
│ entity_id        │  │ message          │  │ success          │  │ message          │
│ username         │  │ stack_trace      │  │ failure_reason   │  │ type             │
│ details          │  │ request_url      │  │ user_agent       │  │ is_read          │
│ ip_address       │  │ request_method   │  │ created_at       │  │ read_at          │
│ created_at       │  │ response_status  │  └──────────────────┘  │ created_at       │
└──────────────────┘  │ duration_ms      │                        └──────────────────┘
                      │ username         │
                      │ ip_address       │
                      │ created_at       │
                      └──────────────────┘
```

---

## Datos Poblados

### Productos (15 productos de ejemplo)
- Electrónica: Laptops, Monitores, Tablets
- Periféricos: Teclados, Mouse, Webcams
- Audio: Audífonos
- Mobiliario: Sillas, Escritorios
- Accesorios: Hubs USB, Cables
- Almacenamiento: SSDs
- Componentes: RAM
- Energía: UPS
- Impresión: Impresoras

### Ventas (3 ventas de ejemplo)
- V-2024-001: 3 Laptops HP → $56,999.97
- V-2024-002: 5 Teclados Logitech → $14,495.00
- V-2024-003: 2 Audífonos Sony → $13,998.00

### Auditoría (20 registros)
- Registros de creación de usuarios
- Logins exitosos
- Creación de productos
- Movimientos de inventario
- Ventas registradas

### Intentos de Login (10 registros)
- 7 exitosos de usuarios legítimos
- 3 fallidos (1 usuario inexistente, 2 contraseña incorrecta)

### Notificaciones (10 registros)
- Bienvenidas a nuevos usuarios
- Alertas de stock bajo
- Confirmaciones de ventas
- Alertas de seguridad
