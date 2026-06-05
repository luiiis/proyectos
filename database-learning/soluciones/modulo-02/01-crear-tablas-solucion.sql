-- ════════════════════════════════════════════════════════════════
-- SOLUCIÓN - MÓDULO 02 - EJERCICIO 1: Crear Tablas
-- ════════════════════════════════════════════════════════════════

-- EJERCICIO 2.1: departamentos
CREATE TABLE departamentos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    presupuesto NUMERIC(12,2) CHECK (presupuesto > 0),
    gerente_id INTEGER REFERENCES empleados(id),
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW()
);
-- EXPLICACIÓN:
-- SERIAL = INTEGER + secuencia auto-incremental (1, 2, 3...)
-- PRIMARY KEY = UNIQUE + NOT NULL (identificador único)
-- CHECK = validación a nivel de BD (el dato NUNCA puede violar esto)
-- REFERENCES = Foreign Key (garantiza integridad referencial)
-- DEFAULT = valor automático si no se especifica al insertar

-- EJERCICIO 2.2: proyectos
CREATE TABLE proyectos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    presupuesto NUMERIC(12,2),
    departamento_id INTEGER REFERENCES departamentos(id),
    estado VARCHAR(20) DEFAULT 'ACTIVO' 
        CHECK (estado IN ('ACTIVO', 'PAUSADO', 'COMPLETADO', 'CANCELADO')),
    prioridad INTEGER CHECK (prioridad BETWEEN 1 AND 5)
);
-- EXPLICACIÓN:
-- TEXT = texto sin límite de longitud (vs VARCHAR que tiene máximo)
-- DATE = solo fecha (sin hora). Para fecha+hora usar TIMESTAMP
-- CHECK con IN = lista de valores permitidos (como un ENUM)
-- BETWEEN = rango inclusivo (1, 2, 3, 4 o 5)

-- EJERCICIO 2.3: asignaciones (tabla pivote M:N)
CREATE TABLE asignaciones (
    empleado_id INTEGER NOT NULL REFERENCES empleados(id) ON DELETE CASCADE,
    proyecto_id INTEGER NOT NULL REFERENCES proyectos(id) ON DELETE CASCADE,
    horas_asignadas INTEGER NOT NULL CHECK (horas_asignadas > 0),
    rol VARCHAR(30) CHECK (rol IN ('LIDER', 'DESARROLLADOR', 'TESTER', 'ANALISTA')),
    fecha_asignacion DATE DEFAULT CURRENT_DATE,
    PRIMARY KEY (empleado_id, proyecto_id)
);
-- EXPLICACIÓN:
-- PRIMARY KEY compuesta = la COMBINACIÓN de ambos campos es única
--   (un empleado puede estar en muchos proyectos, pero no 2 veces en el mismo)
-- ON DELETE CASCADE = si se borra el empleado/proyecto, se borran sus asignaciones
-- CURRENT_DATE = fecha de hoy (sin hora)

-- EJERCICIO 2.4: promociones con restricciones complejas
CREATE TABLE promociones (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descuento_porcentaje NUMERIC(5,2) 
        CHECK (descuento_porcentaje BETWEEN 0.01 AND 99.99),
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    producto_id INTEGER REFERENCES productos(id),
    activa BOOLEAN DEFAULT TRUE,
    CONSTRAINT chk_fechas CHECK (fecha_fin > fecha_inicio),
    CONSTRAINT uq_promo_activa UNIQUE (producto_id, activa)
);
-- EXPLICACIÓN:
-- CONSTRAINT con nombre = puedes darle nombre al constraint para identificarlo en errores
-- CHECK (fecha_fin > fecha_inicio) = la BD RECHAZA datos donde fin <= inicio
-- UNIQUE (producto_id, activa) = no puede haber 2 filas con mismo producto Y activa=true
--   NOTA: En PostgreSQL, NULL no viola UNIQUE, así que si activa=NULL no cuenta

-- ═══════════════════════════════════════════════════════════════
-- INSERTAR DATOS DE PRUEBA
-- ═══════════════════════════════════════════════════════════════
INSERT INTO departamentos (nombre, presupuesto, gerente_id) VALUES
('Tecnología', 500000.00, 1),
('Ventas', 300000.00, 2),
('Marketing', 200000.00, 3),
('Operaciones', 400000.00, 4),
('Recursos Humanos', 150000.00, 5);

INSERT INTO proyectos (nombre, fecha_inicio, presupuesto, departamento_id, estado, prioridad) VALUES
('Migración a Cloud', '2024-01-15', 250000, 1, 'ACTIVO', 5),
('App Móvil v2', '2024-03-01', 180000, 1, 'ACTIVO', 4),
('Campaña Verano', '2024-06-01', 80000, 3, 'COMPLETADO', 3),
('Nuevo ERP', '2024-02-01', 500000, 1, 'ACTIVO', 5),
('Expansión Norte', '2024-04-15', 350000, 4, 'PAUSADO', 4);

INSERT INTO asignaciones (empleado_id, proyecto_id, horas_asignadas, rol) VALUES
(1, 1, 40, 'LIDER'),
(7, 1, 30, 'DESARROLLADOR'),
(8, 1, 30, 'DESARROLLADOR'),
(9, 2, 40, 'LIDER'),
(10, 2, 35, 'TESTER'),
(1, 4, 20, 'LIDER'),
(7, 4, 40, 'DESARROLLADOR');
