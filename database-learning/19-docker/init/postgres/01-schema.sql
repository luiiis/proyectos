-- ════════════════════════════════════════════════════════════════
-- PostgreSQL - Schema Empresarial Completo
-- Base de datos de aprendizaje con 16 tablas
-- ════════════════════════════════════════════════════════════════

-- ═══════ CATÁLOGOS BASE ═══════
CREATE TABLE sucursales (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(200),
    telefono VARCHAR(20),
    ciudad VARCHAR(50),
    estado VARCHAR(50),
    codigo_postal VARCHAR(10),
    activa BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE categorias (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(200),
    activa BOOLEAN DEFAULT TRUE
);

CREATE TABLE proveedores (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    contacto VARCHAR(100),
    telefono VARCHAR(20),
    email VARCHAR(100),
    direccion VARCHAR(200),
    ciudad VARCHAR(50),
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW()
);

-- ═══════ PERSONAS ═══════
CREATE TABLE empleados (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE,
    telefono VARCHAR(20),
    puesto VARCHAR(50),
    salario NUMERIC(10,2) CHECK (salario > 0),
    fecha_ingreso DATE DEFAULT CURRENT_DATE,
    sucursal_id INTEGER REFERENCES sucursales(id),
    jefe_id INTEGER REFERENCES empleados(id),  -- Self-reference (jerarquía)
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE,
    telefono VARCHAR(20),
    direccion VARCHAR(200),
    ciudad VARCHAR(50),
    fecha_nacimiento DATE,
    tipo VARCHAR(20) DEFAULT 'REGULAR' CHECK (tipo IN ('REGULAR', 'VIP', 'MAYORISTA')),
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW()
);

-- ═══════ SEGURIDAD ═══════
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL UNIQUE,
    descripcion VARCHAR(100)
);

CREATE TABLE permisos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(100),
    modulo VARCHAR(30)
);

CREATE TABLE rol_permisos (
    rol_id INTEGER REFERENCES roles(id) ON DELETE CASCADE,
    permiso_id INTEGER REFERENCES permisos(id) ON DELETE CASCADE,
    PRIMARY KEY (rol_id, permiso_id)
);

CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    empleado_id INTEGER REFERENCES empleados(id),
    rol_id INTEGER REFERENCES roles(id),
    ultimo_login TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW()
);

-- ═══════ PRODUCTOS E INVENTARIO ═══════
CREATE TABLE productos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio NUMERIC(10,2) NOT NULL CHECK (precio > 0),
    costo NUMERIC(10,2) CHECK (costo >= 0),
    sku VARCHAR(50) UNIQUE,
    categoria_id INTEGER REFERENCES categorias(id),
    proveedor_id INTEGER REFERENCES proveedores(id),
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE inventario (
    id SERIAL PRIMARY KEY,
    producto_id INTEGER NOT NULL REFERENCES productos(id),
    sucursal_id INTEGER NOT NULL REFERENCES sucursales(id),
    cantidad INTEGER NOT NULL DEFAULT 0 CHECK (cantidad >= 0),
    cantidad_minima INTEGER DEFAULT 10,
    ultima_actualizacion TIMESTAMP DEFAULT NOW(),
    UNIQUE(producto_id, sucursal_id)
);

-- ═══════ VENTAS ═══════
CREATE TABLE ventas (
    id SERIAL PRIMARY KEY,
    numero_venta VARCHAR(20) NOT NULL UNIQUE,
    fecha TIMESTAMP DEFAULT NOW(),
    cliente_id INTEGER REFERENCES clientes(id),
    empleado_id INTEGER NOT NULL REFERENCES empleados(id),
    sucursal_id INTEGER NOT NULL REFERENCES sucursales(id),
    subtotal NUMERIC(12,2) DEFAULT 0,
    impuesto NUMERIC(12,2) DEFAULT 0,
    descuento NUMERIC(12,2) DEFAULT 0,
    total NUMERIC(12,2) DEFAULT 0,
    estado VARCHAR(20) DEFAULT 'COMPLETADA' CHECK (estado IN ('PENDIENTE','COMPLETADA','CANCELADA','DEVUELTA')),
    metodo_pago VARCHAR(20) DEFAULT 'EFECTIVO' CHECK (metodo_pago IN ('EFECTIVO','TARJETA','TRANSFERENCIA','CREDITO'))
);

CREATE TABLE detalle_venta (
    id SERIAL PRIMARY KEY,
    venta_id INTEGER NOT NULL REFERENCES ventas(id) ON DELETE CASCADE,
    producto_id INTEGER NOT NULL REFERENCES productos(id),
    cantidad INTEGER NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMERIC(10,2) NOT NULL,
    descuento NUMERIC(10,2) DEFAULT 0,
    subtotal NUMERIC(12,2) NOT NULL
);

-- ═══════ COMPRAS ═══════
CREATE TABLE compras (
    id SERIAL PRIMARY KEY,
    numero_compra VARCHAR(20) NOT NULL UNIQUE,
    fecha TIMESTAMP DEFAULT NOW(),
    proveedor_id INTEGER NOT NULL REFERENCES proveedores(id),
    empleado_id INTEGER NOT NULL REFERENCES empleados(id),
    sucursal_id INTEGER NOT NULL REFERENCES sucursales(id),
    total NUMERIC(12,2) DEFAULT 0,
    estado VARCHAR(20) DEFAULT 'RECIBIDA' CHECK (estado IN ('PENDIENTE','RECIBIDA','CANCELADA'))
);

CREATE TABLE detalle_compra (
    id SERIAL PRIMARY KEY,
    compra_id INTEGER NOT NULL REFERENCES compras(id) ON DELETE CASCADE,
    producto_id INTEGER NOT NULL REFERENCES productos(id),
    cantidad INTEGER NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMERIC(10,2) NOT NULL,
    subtotal NUMERIC(12,2) NOT NULL
);

-- ═══════ AUDITORÍA ═══════
CREATE TABLE auditoria (
    id SERIAL PRIMARY KEY,
    tabla VARCHAR(50) NOT NULL,
    operacion VARCHAR(10) NOT NULL CHECK (operacion IN ('INSERT','UPDATE','DELETE')),
    registro_id INTEGER,
    datos_antes JSONB,
    datos_despues JSONB,
    usuario VARCHAR(50),
    ip_address INET,
    fecha TIMESTAMP DEFAULT NOW()
);

-- ═══════ ÍNDICES ═══════
CREATE INDEX idx_empleados_sucursal ON empleados(sucursal_id);
CREATE INDEX idx_productos_categoria ON productos(categoria_id);
CREATE INDEX idx_productos_proveedor ON productos(proveedor_id);
CREATE INDEX idx_ventas_fecha ON ventas(fecha);
CREATE INDEX idx_ventas_cliente ON ventas(cliente_id);
CREATE INDEX idx_ventas_empleado ON ventas(empleado_id);
CREATE INDEX idx_detalle_venta_venta ON detalle_venta(venta_id);
CREATE INDEX idx_detalle_venta_producto ON detalle_venta(producto_id);
CREATE INDEX idx_inventario_producto ON inventario(producto_id);
CREATE INDEX idx_auditoria_tabla ON auditoria(tabla);
CREATE INDEX idx_auditoria_fecha ON auditoria(fecha);
