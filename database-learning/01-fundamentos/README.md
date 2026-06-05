# Módulo 01: Fundamentos de Bases de Datos

## 1. ¿Qué es una Base de Datos?

Una base de datos es un sistema organizado para almacenar, gestionar y recuperar información de forma eficiente.

**Analogía**: Imagina una biblioteca.
- La biblioteca = Base de datos
- Cada estante = Una tabla
- Cada libro = Un registro (fila)
- Las características del libro (título, autor, ISBN) = Columnas

### Tipos de Bases de Datos

| Tipo | Ejemplo | Cuándo usarla |
|------|---------|---------------|
| Relacional (SQL) | PostgreSQL, MySQL, Oracle | Datos estructurados, transacciones, relaciones |
| Documental (NoSQL) | MongoDB, CouchDB | Datos flexibles, JSON, prototipos rápidos |
| Clave-Valor | Redis, DynamoDB | Caché, sesiones, datos simples ultra-rápidos |
| Grafos | Neo4j, Neptune | Redes sociales, recomendaciones |
| Columnar | Cassandra, ClickHouse | Big Data, analytics, series temporales |

**En este curso nos enfocamos en RELACIONALES** (SQL) porque:
- Son las más usadas en empresas (90%+ de aplicaciones)
- Son la base para entender todas las demás
- Son las que piden en entrevistas de trabajo

---

## 2. Conceptos Fundamentales

### 2.1 Tabla (Table)
Una tabla es una colección de datos organizados en filas y columnas.

```
Tabla: empleados
┌────┬──────────┬───────────┬──────────┬─────────┐
│ id │ nombre   │ apellido  │ puesto   │ salario │
├────┼──────────┼───────────┼──────────┼─────────┤
│ 1  │ Carlos   │ García    │ Gerente  │ 45000   │
│ 2  │ María    │ López     │ Analista │ 35000   │
│ 3  │ Juan     │ Martínez  │ Dev Sr   │ 55000   │
└────┴──────────┴───────────┴──────────┴─────────┘
```

### 2.2 Registro / Fila (Row / Record)
Cada fila es UN elemento completo. En el ejemplo: cada empleado es un registro.

### 2.3 Columna / Campo (Column / Field)
Cada columna es una CARACTERÍSTICA. En el ejemplo: nombre, apellido, puesto, salario.

### 2.4 Llave Primaria (Primary Key - PK)
Identificador ÚNICO de cada registro. No se puede repetir ni ser NULL.

```
id = 1 → Solo existe UN empleado con id 1
id = 2 → Solo existe UN empleado con id 2
```

**Reglas:**
- Debe ser única (no repetirse)
- No puede ser NULL
- No debe cambiar con el tiempo
- Generalmente es un número auto-incremental

### 2.5 Llave Foránea (Foreign Key - FK)
Referencia a la llave primaria de OTRA tabla. Crea la RELACIÓN entre tablas.

```
Tabla: departamentos          Tabla: empleados
┌────┬─────────────┐         ┌────┬──────────┬─────────────────┐
│ id │ nombre      │         │ id │ nombre   │ departamento_id │
├────┼─────────────┤         ├────┼──────────┼─────────────────┤
│ 1  │ Ventas      │◄────────│ 1  │ Carlos   │ 1               │
│ 2  │ Tecnología  │◄────────│ 2  │ María    │ 2               │
│ 3  │ RRHH        │         │ 3  │ Juan     │ 2               │
└────┴─────────────┘         └────┴──────────┴─────────────────┘

departamento_id en empleados es una FK que apunta a id en departamentos.
Carlos está en Ventas (departamento_id=1)
María y Juan están en Tecnología (departamento_id=2)
```

---

## 3. Tipos de Relaciones

### 3.1 Uno a Muchos (1:N) — La más común
Un departamento tiene MUCHOS empleados. Un empleado pertenece a UN departamento.

```
departamentos (1) ──────< empleados (N)
```

### 3.2 Muchos a Muchos (M:N)
Un estudiante toma MUCHOS cursos. Un curso tiene MUCHOS estudiantes.
Se implementa con una TABLA INTERMEDIA (pivote).

```
estudiantes (M) >──────< cursos (N)

Se convierte en:
estudiantes (1) ──────< estudiante_cursos >────── cursos (1)
```

### 3.3 Uno a Uno (1:1)
Un empleado tiene UN expediente. Un expediente pertenece a UN empleado.

```
empleados (1) ────── expedientes (1)
```

---

## 4. Diagrama Entidad-Relación (ER)

### Sistema Empresarial (el que construiremos)

```
┌──────────────┐       ┌──────────────┐       ┌──────────────┐
│  sucursales  │       │  empleados   │       │   usuarios   │
├──────────────┤       ├──────────────┤       ├──────────────┤
│ PK id        │       │ PK id        │       │ PK id        │
│ nombre       │◄──┐   │ nombre       │   ┌──>│ username     │
│ direccion    │   │   │ apellido     │   │   │ password     │
│ telefono     │   │   │ email        │   │   │ FK empleado_id│
│ ciudad       │   └───│ FK sucursal_id│   │   │ FK rol_id    │
└──────────────┘       │ puesto       │   │   └──────────────┘
                       │ salario      │   │
                       │ fecha_ingreso│───┘   ┌──────────────┐
                       └──────────────┘       │    roles     │
                                              ├──────────────┤
┌──────────────┐       ┌──────────────┐       │ PK id        │
│ proveedores  │       │  categorias  │       │ nombre       │
├──────────────┤       ├──────────────┤       │ descripcion  │
│ PK id        │       │ PK id        │       └──────────────┘
│ nombre       │       │ nombre       │
│ contacto     │       │ descripcion  │       ┌──────────────┐
│ telefono     │       └──────┬───────┘       │  permisos    │
│ email        │              │               ├──────────────┤
└──────┬───────┘              │               │ PK id        │
       │                      │               │ nombre       │
       │               ┌──────┴───────┐       │ descripcion  │
       │               │  productos   │       └──────────────┘
       │               ├──────────────┤
       └──────────────>│ PK id        │       ┌──────────────┐
                       │ nombre       │       │   clientes   │
                       │ descripcion  │       ├──────────────┤
                       │ precio       │       │ PK id        │
                       │ FK categoria_id│      │ nombre       │
                       │ FK proveedor_id│      │ apellido     │
                       └──────┬───────┘       │ email        │
                              │               │ telefono     │
                       ┌──────┴───────┐       │ direccion    │
                       │  inventario  │       └──────┬───────┘
                       ├──────────────┤              │
                       │ PK id        │              │
                       │ FK producto_id│       ┌──────┴───────┐
                       │ FK sucursal_id│       │   ventas     │
                       │ cantidad     │       ├──────────────┤
                       │ minimo       │       │ PK id        │
                       └──────────────┘       │ fecha        │
                                              │ FK cliente_id │
┌──────────────┐       ┌──────────────┐       │ FK empleado_id│
│   compras    │       │detalle_compra│       │ FK sucursal_id│
├──────────────┤       ├──────────────┤       │ total        │
│ PK id        │       │ PK id        │       └──────┬───────┘
│ fecha        │──────>│ FK compra_id  │              │
│ FK proveedor_id│      │ FK producto_id│       ┌──────┴───────┐
│ FK empleado_id│       │ cantidad     │       │ detalle_venta│
│ total        │       │ precio_unit  │       ├──────────────┤
└──────────────┘       └──────────────┘       │ PK id        │
                                              │ FK venta_id   │
┌──────────────┐                              │ FK producto_id│
│  auditoria   │                              │ cantidad     │
├──────────────┤                              │ precio_unit  │
│ PK id        │                              │ subtotal     │
│ tabla        │                              └──────────────┘
│ operacion    │
│ registro_id  │
│ datos_antes  │
│ datos_despues│
│ usuario      │
│ fecha        │
└──────────────┘
```

---

## 5. Ejercicios del Módulo 1

### Ejercicio 1.1
Identifica en el diagrama anterior:
- ¿Cuántas relaciones 1:N hay?
- ¿Hay alguna relación M:N? ¿Cuál?
- ¿Qué tabla tiene más llaves foráneas?

### Ejercicio 1.2
Diseña un diagrama ER para un sistema de:
- Biblioteca (libros, autores, préstamos, socios)
- Incluye: PK, FK, relaciones

### Ejercicio 1.3
¿Qué tipo de relación existe entre:
- Un cliente y sus ventas?
- Un producto y sus categorías?
- Un empleado y su usuario del sistema?

### Ejercicio 1.4
¿Por qué la tabla `detalle_venta` existe separada de `ventas`?
¿Qué pasaría si pusieras los productos directamente en la tabla ventas?

---

## 6. Errores Comunes de Principiantes

| Error | Por qué es malo | Solución |
|-------|-----------------|----------|
| No poner PK | No puedes identificar registros únicos | SIEMPRE tener PK |
| Usar nombre como PK | Los nombres se repiten | Usar ID numérico |
| Guardar datos repetidos | Desperdicio de espacio, inconsistencias | Normalizar (crear tablas separadas) |
| No usar FK | Datos huérfanos, inconsistencia | Siempre relacionar con FK |
| Una tabla gigante con todo | Imposible de mantener | Dividir en tablas relacionadas |

---

## 7. Comparación entre Motores

| Característica | PostgreSQL | MySQL | Oracle |
|---------------|-----------|-------|--------|
| Licencia | Open Source (gratis) | Open Source (gratis) | Comercial ($$$) |
| Cumplimiento SQL | Muy alto | Medio | Muy alto |
| JSON nativo | JSONB (indexable) | JSON (limitado) | JSON |
| Extensiones | Muchas (PostGIS, etc.) | Pocas | Muchas |
| Rendimiento | Excelente en queries complejas | Excelente en lecturas simples | Excelente en todo (con hardware) |
| Uso típico | Startups, enterprise moderno | Web apps, WordPress, CMS | Banca, gobierno, telecom |
| Comunidad | Grande y activa | La más grande | Corporativa |

---

## Siguiente Módulo
→ [02-DDL: Crear y Modificar Estructuras](../02-ddl/README.md)
