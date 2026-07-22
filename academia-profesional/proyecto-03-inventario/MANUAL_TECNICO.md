# Manual Técnico - Proyecto 03: Sistema Inventario

## Objetivo de Aprendizaje
Dominar las colecciones de Java y saber CUÁNDO usar cada una:
- **HashMap**: acceso instantáneo por clave (O(1))
- **ArrayList**: lista ordenada para historial cronológico
- **TreeMap**: datos ordenados automáticamente
- **Stream API**: filtrar, transformar y agregar datos de forma declarativa

---

## Arquitectura

```
┌────────────────────────────────────────────────────────────┐
│                  SistemaInventario.java                      │
├────────────────────────────────────────────────────────────┤
│                                                            │
│  DATOS:                                                    │
│  ┌─────────────────────────────────────────────────────┐   │
│  │ HashMap<String, Producto> productos                  │   │
│  │   "LAP-001" → Producto("LAP-001","Laptop",18999)    │   │
│  │   "MON-001" → Producto("MON-001","Monitor",12499)   │   │
│  │   Acceso O(1) por SKU                                │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                            │
│  ┌─────────────────────────────────────────────────────┐   │
│  │ HashMap<String, Integer> stock                       │   │
│  │   "LAP-001" → 22                                    │   │
│  │   "MON-001" → 15                                    │   │
│  │   Separado de Producto (puede cambiar sin tocar el  │   │
│  │   catálogo)                                          │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                            │
│  ┌─────────────────────────────────────────────────────┐   │
│  │ ArrayList<Movimiento> movimientos                    │   │
│  │   [ENTRADA, LAP-001, +25, "Compra proveedor HP"]    │   │
│  │   [SALIDA,  LAP-001, -3,  "Venta #V-001"]          │   │
│  │   Historial cronológico (solo se agregan al final)  │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                            │
│  OPERACIONES:                                              │
│  registrar() → agrega producto + stock inicial 0           │
│  entrada()   → stock += cantidad + registra movimiento     │
│  salida()    → valida stock + stock -= cantidad            │
│                                                            │
│  REPORTES (Stream API):                                    │
│  reporteStock()       → TreeMap ordena por SKU             │
│  reporteStockBajo()   → filter + sorted                    │
│  reportePorCategoria() → groupingBy + counting             │
│  reporteMovimientos() → filter por SKU                     │
│  valorInventario()    → mapToDouble + sum                  │
│                                                            │
└────────────────────────────────────────────────────────────┘
```

---

## Records (Modelos de Datos)

```java
record Producto(String sku, String nombre, double precio, String categoria) {}
record Movimiento(String sku, String tipo, int cantidad, LocalDateTime fecha, String motivo) {}
```

| Record | Campos | Para qué |
|--------|--------|----------|
| Producto | sku, nombre, precio, categoria | Datos del catálogo (no cambian) |
| Movimiento | sku, tipo, cantidad, fecha, motivo | Registro de cada entrada/salida |

---

## Métodos Clave Explicados

### `stock.merge(sku, cantidad, Integer::sum)`
```java
// merge = "si existe, aplica la función; si no, inserta el valor"
stock.merge("LAP-001", 25, Integer::sum);
// Si "LAP-001" tiene 0 → 0 + 25 = 25
// Si "LAP-001" tiene 25 → 25 + 25 = 50
// Para salidas: merge(sku, -cantidad, Integer::sum) → resta
```

### `stock.getOrDefault(sku, 0)`
```java
// Si la clave no existe, devuelve 0 en vez de null
int actual = stock.getOrDefault("XXX-999", 0);  // → 0 (no existe)
```

### Stream: groupingBy
```java
productos.values().stream()
    .collect(Collectors.groupingBy(Producto::categoria, Collectors.counting()));
// Resultado: {"Electrónica": 2, "Periféricos": 2, "Mobiliario": 1}
```

### Stream: mapToDouble + sum
```java
double total = stock.entrySet().stream()
    .mapToDouble(e -> productos.get(e.getKey()).precio() * e.getValue())
    .sum();
// Multiplica precio × cantidad de cada producto y suma todo
```

---

## Comparativa de Colecciones Usadas

| Colección | Uso en este proyecto | Ventaja | Desventaja |
|-----------|---------------------|---------|------------|
| `HashMap<String, Producto>` | Catálogo por SKU | O(1) buscar/insertar | Sin orden |
| `HashMap<String, Integer>` | Stock por SKU | O(1) actualizar stock | Sin orden |
| `ArrayList<Movimiento>` | Historial | Mantiene orden cronológico | O(n) buscar |
| `TreeMap` (en reporte) | Ordenar por clave | Siempre ordenado | O(log n) insertar |

---

## Flujo de Ejecución

```
1. Registrar productos → HashMap vacío con stock = 0
2. Entradas de inventario:
   - stock.merge(sku, +cantidad) → actualiza stock
   - movimientos.add(nuevo Movimiento tipo ENTRADA)
3. Salidas de inventario:
   - Validar: ¿hay suficiente stock?
   - Si NO → mensaje de error, no se descuenta
   - Si SÍ → stock.merge(sku, -cantidad) + registrar movimiento
4. Reportes:
   - Stock general → TreeMap ordena las claves
   - Stock bajo → filter(valor < mínimo) + sorted
   - Por categoría → groupingBy + counting
   - Movimientos → filter por SKU
   - Valorización → mapToDouble(precio × stock) + sum
```

---

## Cuándo Usar Cada Colección (Regla General)

```
¿Necesitas buscar por clave?
  → SÍ: HashMap (O(1))
  → SÍ + ordenado: TreeMap (O(log n))

¿Necesitas una lista ordenada?
  → SÍ + acceso por índice: ArrayList
  → SÍ + muchas inserciones en medio: LinkedList

¿Necesitas evitar duplicados?
  → SÍ: HashSet
  → SÍ + ordenado: TreeSet

¿Necesitas una cola (FIFO)?
  → SÍ: LinkedList como Queue
  → SÍ + por prioridad: PriorityQueue
```

---

## Estructura de Archivos
```
proyecto-03-inventario/
├── src/
│   └── SistemaInventario.java  ← Todo el código (records + lógica)
├── ejercicios/
│   └── retos.md                ← Ejercicios prácticos
├── entrevistas/
│   └── preguntas.md            ← Preguntas de entrevista
├── README.md                   ← Descripción general
├── CONSTRUCCION.md             ← Bitácora de decisiones
├── COMO_EJECUTAR.md            ← Instrucciones de ejecución
└── MANUAL_TECNICO.md           ← Este archivo
```
