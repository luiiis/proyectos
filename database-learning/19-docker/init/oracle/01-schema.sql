-- ════════════════════════════════════════════════════════════════
-- Oracle XE 21c - Schema Empresarial
-- Se ejecuta como APP_USER (empresa_user)
-- ════════════════════════════════════════════════════════════════

-- Secuencias (Oracle no tiene AUTO_INCREMENT, usa SEQUENCES)
CREATE SEQUENCE seq_sucursales START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_categorias START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_proveedores START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_empleados START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_clientes START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_productos START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_ventas START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_detalle_venta START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_auditoria START WITH 1 INCREMENT BY 1;

-- Tablas
CREATE TABLE sucursales (
    id NUMBER DEFAULT seq_sucursales.NEXTVAL PRIMARY KEY,
    nombre VARCHAR2(100) NOT NULL,
    direccion VARCHAR2(200),
    telefono VARCHAR2(20),
    ciudad VARCHAR2(50),
    estado VARCHAR2(50),
    codigo_postal VARCHAR2(10),
    activa NUMBER(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categorias (
    id NUMBER DEFAULT seq_categorias.NEXTVAL PRIMARY KEY,
    nombre VARCHAR2(50) NOT NULL UNIQUE,
    descripcion VARCHAR2(200),
    activa NUMBER(1) DEFAULT 1
);

CREATE TABLE proveedores (
    id NUMBER DEFAULT seq_proveedores.NEXTVAL PRIMARY KEY,
    nombre VARCHAR2(100) NOT NULL,
    contacto VARCHAR2(100),
    telefono VARCHAR2(20),
    email VARCHAR2(100),
    direccion VARCHAR2(200),
    ciudad VARCHAR2(50),
    activo NUMBER(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE empleados (
    id NUMBER DEFAULT seq_empleados.NEXTVAL PRIMARY KEY,
    nombre VARCHAR2(50) NOT NULL,
    apellido VARCHAR2(50) NOT NULL,
    email VARCHAR2(100) UNIQUE,
    telefono VARCHAR2(20),
    puesto VARCHAR2(50),
    salario NUMBER(10,2) CHECK (salario > 0),
    fecha_ingreso DATE DEFAULT SYSDATE,
    sucursal_id NUMBER REFERENCES sucursales(id),
    jefe_id NUMBER REFERENCES empleados(id),
    activo NUMBER(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE clientes (
    id NUMBER DEFAULT seq_clientes.NEXTVAL PRIMARY KEY,
    nombre VARCHAR2(50) NOT NULL,
    apellido VARCHAR2(50) NOT NULL,
    email VARCHAR2(100) UNIQUE,
    telefono VARCHAR2(20),
    direccion VARCHAR2(200),
    ciudad VARCHAR2(50),
    fecha_nacimiento DATE,
    tipo VARCHAR2(20) DEFAULT 'REGULAR' CHECK (tipo IN ('REGULAR','VIP','MAYORISTA')),
    activo NUMBER(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE productos (
    id NUMBER DEFAULT seq_productos.NEXTVAL PRIMARY KEY,
    nombre VARCHAR2(150) NOT NULL,
    descripcion VARCHAR2(1000),
    precio NUMBER(10,2) NOT NULL CHECK (precio > 0),
    costo NUMBER(10,2),
    sku VARCHAR2(50) UNIQUE,
    categoria_id NUMBER REFERENCES categorias(id),
    proveedor_id NUMBER REFERENCES proveedores(id),
    activo NUMBER(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ventas (
    id NUMBER DEFAULT seq_ventas.NEXTVAL PRIMARY KEY,
    numero_venta VARCHAR2(20) NOT NULL UNIQUE,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cliente_id NUMBER REFERENCES clientes(id),
    empleado_id NUMBER NOT NULL REFERENCES empleados(id),
    sucursal_id NUMBER NOT NULL REFERENCES sucursales(id),
    subtotal NUMBER(12,2) DEFAULT 0,
    impuesto NUMBER(12,2) DEFAULT 0,
    total NUMBER(12,2) DEFAULT 0,
    estado VARCHAR2(20) DEFAULT 'COMPLETADA' CHECK (estado IN ('PENDIENTE','COMPLETADA','CANCELADA','DEVUELTA')),
    metodo_pago VARCHAR2(20) DEFAULT 'EFECTIVO' CHECK (metodo_pago IN ('EFECTIVO','TARJETA','TRANSFERENCIA','CREDITO'))
);

CREATE TABLE detalle_venta (
    id NUMBER DEFAULT seq_detalle_venta.NEXTVAL PRIMARY KEY,
    venta_id NUMBER NOT NULL REFERENCES ventas(id),
    producto_id NUMBER NOT NULL REFERENCES productos(id),
    cantidad NUMBER NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMBER(10,2) NOT NULL,
    subtotal NUMBER(12,2) NOT NULL
);

CREATE TABLE auditoria (
    id NUMBER DEFAULT seq_auditoria.NEXTVAL PRIMARY KEY,
    tabla VARCHAR2(50) NOT NULL,
    operacion VARCHAR2(10) NOT NULL CHECK (operacion IN ('INSERT','UPDATE','DELETE')),
    registro_id NUMBER,
    datos_antes CLOB,
    datos_despues CLOB,
    usuario VARCHAR2(50),
    ip_address VARCHAR2(45),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices
CREATE INDEX idx_emp_sucursal ON empleados(sucursal_id);
CREATE INDEX idx_prod_categoria ON productos(categoria_id);
CREATE INDEX idx_ventas_fecha ON ventas(fecha);
CREATE INDEX idx_ventas_cliente ON ventas(cliente_id);
CREATE INDEX idx_dv_venta ON detalle_venta(venta_id);
