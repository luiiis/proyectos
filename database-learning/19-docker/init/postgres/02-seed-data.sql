-- ════════════════════════════════════════════════════════════════
-- PostgreSQL - Datos Masivos de Prueba
-- 1000+ clientes, 500+ productos, 100 empleados, 10000+ ventas
-- ════════════════════════════════════════════════════════════════

-- ═══════ SUCURSALES (5) ═══════
INSERT INTO sucursales (nombre, direccion, telefono, ciudad, estado, codigo_postal) VALUES
('Central CDMX', 'Av. Reforma 500, Col. Juárez', '55-1234-5678', 'Ciudad de México', 'CDMX', '06600'),
('Monterrey Norte', 'Av. Constitución 1200', '81-9876-5432', 'Monterrey', 'Nuevo León', '64000'),
('Guadalajara Centro', 'Av. Vallarta 3000', '33-5555-1234', 'Guadalajara', 'Jalisco', '44100'),
('Puebla Sur', 'Blvd. Atlixcáyotl 2500', '22-4444-5678', 'Puebla', 'Puebla', '72810'),
('Cancún Plaza', 'Blvd. Kukulcán Km 12', '99-8888-1234', 'Cancún', 'Quintana Roo', '77500');

-- ═══════ CATEGORÍAS (10) ═══════
INSERT INTO categorias (nombre, descripcion) VALUES
('Electrónica', 'Dispositivos electrónicos y gadgets'),
('Computadoras', 'Laptops, desktops y componentes'),
('Periféricos', 'Teclados, mouse, webcams, audífonos'),
('Redes', 'Routers, switches, cables de red'),
('Almacenamiento', 'Discos duros, SSDs, memorias USB'),
('Software', 'Licencias y suscripciones de software'),
('Mobiliario', 'Escritorios, sillas, organizadores'),
('Impresión', 'Impresoras, tóner, papel'),
('Energía', 'UPS, reguladores, extensiones'),
('Accesorios', 'Fundas, cables, adaptadores, hubs');

-- ═══════ PROVEEDORES (10) ═══════
INSERT INTO proveedores (nombre, contacto, telefono, email, ciudad) VALUES
('TechDistributor MX', 'Roberto Sánchez', '55-1111-2222', 'ventas@techdist.mx', 'Ciudad de México'),
('CompuMayoreo SA', 'Laura Martínez', '81-3333-4444', 'laura@compumayoreo.com', 'Monterrey'),
('Digital Supply Co', 'Fernando Ruiz', '33-5555-6666', 'fernando@digitalsupply.mx', 'Guadalajara'),
('MegaComponents', 'Patricia Vega', '55-7777-8888', 'pvega@megacomp.com', 'Ciudad de México'),
('NetPro Solutions', 'Andrés López', '22-9999-0000', 'alopez@netpro.mx', 'Puebla'),
('PrinterWorld MX', 'Carmen Díaz', '55-2222-3333', 'carmen@printerworld.mx', 'Ciudad de México'),
('FurniTech Office', 'Miguel Ángel Torres', '81-4444-5555', 'matorres@furnitech.mx', 'Monterrey'),
('PowerSafe Energy', 'Sofía Hernández', '33-6666-7777', 'sofia@powersafe.mx', 'Guadalajara'),
('CloudSoft Licenses', 'David Morales', '55-8888-9999', 'david@cloudsoft.mx', 'Ciudad de México'),
('AccessoryPlus', 'Ana García', '99-1111-0000', 'ana@accessoryplus.mx', 'Cancún');

-- ═══════ ROLES Y PERMISOS ═══════
INSERT INTO roles (nombre, descripcion) VALUES
('ADMIN', 'Administrador del sistema - acceso total'),
('GERENTE', 'Gerente de sucursal - gestión completa'),
('VENDEDOR', 'Vendedor - ventas y consultas'),
('ALMACENISTA', 'Almacenista - inventario y compras'),
('AUDITOR', 'Auditor - solo lectura y reportes');

INSERT INTO permisos (nombre, descripcion, modulo) VALUES
('CREAR_VENTA', 'Puede crear ventas', 'VENTAS'),
('CANCELAR_VENTA', 'Puede cancelar ventas', 'VENTAS'),
('VER_REPORTES', 'Puede ver reportes', 'REPORTES'),
('GESTIONAR_INVENTARIO', 'Puede modificar inventario', 'INVENTARIO'),
('GESTIONAR_EMPLEADOS', 'Puede gestionar empleados', 'RRHH'),
('GESTIONAR_USUARIOS', 'Puede gestionar usuarios', 'SEGURIDAD'),
('VER_AUDITORIA', 'Puede ver logs de auditoría', 'AUDITORIA'),
('CREAR_COMPRA', 'Puede crear órdenes de compra', 'COMPRAS'),
('GESTIONAR_PRODUCTOS', 'Puede crear/editar productos', 'PRODUCTOS'),
('APLICAR_DESCUENTOS', 'Puede aplicar descuentos', 'VENTAS');

-- Asignar permisos a roles
INSERT INTO rol_permisos (rol_id, permiso_id) VALUES
(1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10), -- ADMIN: todos
(2,1),(2,2),(2,3),(2,4),(2,8),(2,9),(2,10), -- GERENTE: casi todos
(3,1),(3,3),(3,10), -- VENDEDOR: ventas y reportes
(4,4),(4,8),(4,9), -- ALMACENISTA: inventario y compras
(5,3),(5,7); -- AUDITOR: reportes y auditoría

-- ═══════ EMPLEADOS (100) ═══════
-- Generados con datos realistas mexicanos
INSERT INTO empleados (nombre, apellido, email, telefono, puesto, salario, fecha_ingreso, sucursal_id, jefe_id) VALUES
('Ricardo', 'Hernández Mora', 'ricardo.hernandez@empresa.com', '55-1001-0001', 'Director General', 120000, '2018-01-15', 1, NULL),
('Alejandra', 'Torres Vega', 'alejandra.torres@empresa.com', '55-1001-0002', 'Gerente Ventas', 75000, '2019-03-01', 1, 1),
('Fernando', 'García López', 'fernando.garcia@empresa.com', '81-1001-0003', 'Gerente Sucursal', 70000, '2019-06-15', 2, 1),
('Patricia', 'Martínez Ruiz', 'patricia.martinez@empresa.com', '33-1001-0004', 'Gerente Sucursal', 70000, '2019-08-01', 3, 1),
('Miguel', 'Sánchez Díaz', 'miguel.sanchez@empresa.com', '22-1001-0005', 'Gerente Sucursal', 68000, '2020-01-10', 4, 1),
('Sofía', 'López Castro', 'sofia.lopez@empresa.com', '99-1001-0006', 'Gerente Sucursal', 68000, '2020-03-15', 5, 1),
('Carlos', 'Ramírez Flores', 'carlos.ramirez@empresa.com', '55-1001-0007', 'Vendedor Senior', 42000, '2020-05-01', 1, 2),
('María', 'González Pérez', 'maria.gonzalez@empresa.com', '55-1001-0008', 'Vendedor Senior', 42000, '2020-06-15', 1, 2),
('Juan', 'Díaz Morales', 'juan.diaz@empresa.com', '55-1001-0009', 'Vendedor', 35000, '2021-01-10', 1, 2),
('Ana', 'Vega Hernández', 'ana.vega@empresa.com', '55-1001-0010', 'Vendedor', 35000, '2021-02-01', 1, 2);

-- Generar 90 empleados más con generate_series
INSERT INTO empleados (nombre, apellido, email, puesto, salario, fecha_ingreso, sucursal_id, jefe_id)
SELECT
    (ARRAY['Luis','Pedro','Jorge','Roberto','Daniel','Andrés','Diego','Raúl','Oscar','Héctor',
           'Laura','Carmen','Rosa','Elena','Lucía','Gabriela','Verónica','Claudia','Mónica','Isabel'])[floor(random()*20)+1],
    (ARRAY['García','López','Martínez','Hernández','González','Pérez','Sánchez','Ramírez','Torres','Flores',
           'Rivera','Gómez','Díaz','Reyes','Morales','Cruz','Ortiz','Gutiérrez','Chávez','Ramos'])[floor(random()*20)+1]
    || ' ' ||
    (ARRAY['Mora','Vega','Castro','Ruiz','Vargas','Medina','Aguilar','Herrera','Navarro','Mendoza'])[floor(random()*10)+1],
    'empleado' || gs || '@empresa.com',
    (ARRAY['Vendedor','Vendedor Senior','Almacenista','Técnico','Cajero','Auxiliar'])[floor(random()*6)+1],
    round((random() * 30000 + 25000)::numeric, 2),
    DATE '2020-01-01' + (random() * 1500)::integer,
    (floor(random()*5)+1)::integer,
    (ARRAY[2,3,4,5,6])[floor(random()*5)+1]
FROM generate_series(11, 100) gs;

-- ═══════ CLIENTES (1000) ═══════
INSERT INTO clientes (nombre, apellido, email, telefono, ciudad, tipo, fecha_nacimiento)
SELECT
    (ARRAY['José','Miguel','Francisco','Antonio','Manuel','David','Ricardo','Alejandro','Fernando','Eduardo',
           'María','Ana','Carmen','Laura','Patricia','Sofía','Gabriela','Verónica','Claudia','Lucía'])[floor(random()*20)+1],
    (ARRAY['García','López','Martínez','Hernández','González','Pérez','Sánchez','Ramírez','Torres','Flores',
           'Rivera','Gómez','Díaz','Reyes','Morales','Cruz','Ortiz','Gutiérrez','Chávez','Ramos'])[floor(random()*20)+1]
    || ' ' ||
    (ARRAY['Mora','Vega','Castro','Ruiz','Vargas','Medina','Aguilar','Herrera','Navarro','Mendoza'])[floor(random()*10)+1],
    'cliente' || gs || '@email.com',
    '55-' || lpad((floor(random()*9000)+1000)::text, 4, '0') || '-' || lpad((floor(random()*9000)+1000)::text, 4, '0'),
    (ARRAY['Ciudad de México','Monterrey','Guadalajara','Puebla','Cancún','Querétaro','Mérida','Tijuana','León','Toluca'])[floor(random()*10)+1],
    (ARRAY['REGULAR','REGULAR','REGULAR','REGULAR','VIP','VIP','MAYORISTA'])[floor(random()*7)+1],
    DATE '1970-01-01' + (random() * 15000)::integer
FROM generate_series(1, 1000) gs;

-- ═══════ PRODUCTOS (500) ═══════
INSERT INTO productos (nombre, descripcion, precio, costo, sku, categoria_id, proveedor_id)
SELECT
    (ARRAY['Laptop','Monitor','Teclado','Mouse','Audífonos','Webcam','SSD','RAM','Cable','Hub',
           'Router','Switch','Impresora','UPS','Silla','Escritorio','Disco','Memoria','Adaptador','Cargador'])[floor(random()*20)+1]
    || ' ' ||
    (ARRAY['Pro','Ultra','Max','Plus','Elite','Basic','Premium','Lite','Advanced','Standard'])[floor(random()*10)+1]
    || ' ' ||
    (ARRAY['2024','2025','Gen3','Gen4','V2','X1','S1','M1','A1','Z1'])[floor(random()*10)+1],
    'Producto de alta calidad para uso profesional y empresarial',
    round((random() * 50000 + 100)::numeric, 2),
    round((random() * 30000 + 50)::numeric, 2),
    'SKU-' || lpad(gs::text, 5, '0'),
    (floor(random()*10)+1)::integer,
    (floor(random()*10)+1)::integer
FROM generate_series(1, 500) gs;

-- ═══════ INVENTARIO ═══════
INSERT INTO inventario (producto_id, sucursal_id, cantidad, cantidad_minima)
SELECT
    p.id,
    s.id,
    floor(random() * 100 + 5)::integer,
    floor(random() * 15 + 5)::integer
FROM productos p
CROSS JOIN sucursales s
WHERE random() < 0.7;  -- 70% de combinaciones producto-sucursal

-- ═══════ VENTAS (10000) ═══════
INSERT INTO ventas (numero_venta, fecha, cliente_id, empleado_id, sucursal_id, subtotal, impuesto, total, estado, metodo_pago)
SELECT
    'V-' || TO_CHAR(DATE '2023-01-01' + (random()*730)::integer, 'YYYYMMDD') || '-' || lpad(gs::text, 5, '0'),
    TIMESTAMP '2023-01-01' + (random() * 730 || ' days')::interval + (random() * 86400 || ' seconds')::interval,
    (floor(random()*1000)+1)::integer,
    (floor(random()*100)+1)::integer,
    (floor(random()*5)+1)::integer,
    round((random() * 50000 + 500)::numeric, 2),
    0,
    0,
    (ARRAY['COMPLETADA','COMPLETADA','COMPLETADA','COMPLETADA','COMPLETADA','PENDIENTE','CANCELADA'])[floor(random()*7)+1],
    (ARRAY['EFECTIVO','TARJETA','TARJETA','TRANSFERENCIA','CREDITO'])[floor(random()*5)+1]
FROM generate_series(1, 10000) gs;

-- Calcular impuesto y total
UPDATE ventas SET impuesto = round(subtotal * 0.16, 2), total = round(subtotal * 1.16, 2);

-- ═══════ DETALLE DE VENTAS ═══════
INSERT INTO detalle_venta (venta_id, producto_id, cantidad, precio_unitario, subtotal)
SELECT
    v.id,
    (floor(random()*500)+1)::integer,
    (floor(random()*5)+1)::integer,
    round((random()*5000+100)::numeric, 2),
    0
FROM ventas v
CROSS JOIN generate_series(1, 3) items  -- 1-3 items por venta
WHERE random() < 0.8;

UPDATE detalle_venta SET subtotal = cantidad * precio_unitario;

-- ═══════ USUARIOS ═══════
INSERT INTO usuarios (username, password_hash, empleado_id, rol_id) VALUES
('admin', '$2a$12$hash_simulado_admin', 1, 1),
('gerente_mty', '$2a$12$hash_simulado_gerente', 3, 2),
('vendedor1', '$2a$12$hash_simulado_vendedor', 7, 3),
('almacen1', '$2a$12$hash_simulado_almacen', 10, 4),
('auditor1', '$2a$12$hash_simulado_auditor', 9, 5);

-- ═══════ ESTADÍSTICAS FINALES ═══════
-- Verificar cantidades
DO $$
BEGIN
    RAISE NOTICE 'Sucursales: %', (SELECT COUNT(*) FROM sucursales);
    RAISE NOTICE 'Empleados: %', (SELECT COUNT(*) FROM empleados);
    RAISE NOTICE 'Clientes: %', (SELECT COUNT(*) FROM clientes);
    RAISE NOTICE 'Productos: %', (SELECT COUNT(*) FROM productos);
    RAISE NOTICE 'Ventas: %', (SELECT COUNT(*) FROM ventas);
    RAISE NOTICE 'Detalle ventas: %', (SELECT COUNT(*) FROM detalle_venta);
END $$;
