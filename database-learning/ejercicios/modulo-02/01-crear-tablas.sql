-- ════════════════════════════════════════════════════════════════
-- MÓDULO 02 - EJERCICIO 1: Crear Tablas
-- ════════════════════════════════════════════════════════════════
-- INSTRUCCIONES:
-- 1. Conecta a PostgreSQL: docker exec -it learn-postgres psql -U postgres -d empresa_db
-- 2. Ejecuta cada bloque de código
-- 3. Verifica con \dt que las tablas se crearon
-- ════════════════════════════════════════════════════════════════

-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 2.1: Crear tabla departamentos
-- ═══════════════════════════════════════════════════════════════
-- Crea una tabla "departamentos" con:
-- - id: entero auto-incremental, llave primaria
-- - nombre: texto de máximo 50 caracteres, obligatorio, único
-- - presupuesto: número decimal (12,2), debe ser mayor a 0
-- - gerente_id: referencia a empleados(id), puede ser null
-- - activo: booleano, por defecto true
-- - created_at: timestamp, por defecto la fecha actual

-- TU CÓDIGO AQUÍ:




-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 2.2: Crear tabla proyectos
-- ═══════════════════════════════════════════════════════════════
-- Crea una tabla "proyectos" con:
-- - id: entero auto-incremental, PK
-- - nombre: texto 100 chars, obligatorio
-- - descripcion: texto largo (TEXT)
-- - fecha_inicio: fecha, obligatoria
-- - fecha_fin: fecha, puede ser null
-- - presupuesto: decimal(12,2)
-- - departamento_id: FK a departamentos(id)
-- - estado: solo puede ser 'ACTIVO', 'PAUSADO', 'COMPLETADO', 'CANCELADO'
-- - prioridad: entero entre 1 y 5

-- TU CÓDIGO AQUÍ:




-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 2.3: Crear tabla asignaciones (relación M:N)
-- ═══════════════════════════════════════════════════════════════
-- Un empleado puede estar en muchos proyectos.
-- Un proyecto puede tener muchos empleados.
-- Crea la tabla pivote "asignaciones" con:
-- - empleado_id: FK a empleados(id)
-- - proyecto_id: FK a proyectos(id)
-- - horas_asignadas: entero, obligatorio, mayor a 0
-- - rol: texto 30 chars ('LIDER', 'DESARROLLADOR', 'TESTER', 'ANALISTA')
-- - fecha_asignacion: fecha, default hoy
-- - PK compuesta: (empleado_id, proyecto_id)

-- TU CÓDIGO AQUÍ:




-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 2.4: Crear tabla con restricciones complejas
-- ═══════════════════════════════════════════════════════════════
-- Crea una tabla "promociones" con:
-- - id: serial PK
-- - nombre: varchar(100) NOT NULL
-- - descuento_porcentaje: decimal(5,2), entre 0.01 y 99.99
-- - fecha_inicio: date NOT NULL
-- - fecha_fin: date NOT NULL
-- - CHECK: fecha_fin debe ser POSTERIOR a fecha_inicio
-- - producto_id: FK a productos(id)
-- - activa: boolean default true
-- - UNIQUE: no puede haber 2 promociones activas para el mismo producto
--   (producto_id, activa) debe ser único cuando activa=true

-- TU CÓDIGO AQUÍ:




-- ═══════════════════════════════════════════════════════════════
-- VERIFICACIÓN
-- ═══════════════════════════════════════════════════════════════
-- Ejecuta estos comandos para verificar que todo se creó bien:
-- \dt                          -- Ver todas las tablas
-- \d departamentos             -- Ver estructura de departamentos
-- \d proyectos                 -- Ver estructura de proyectos
-- \d asignaciones              -- Ver estructura de asignaciones
-- \d promociones               -- Ver estructura de promociones
