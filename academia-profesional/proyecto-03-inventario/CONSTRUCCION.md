# Cómo se Construyó - Proyecto 03: Inventario

## Paso 1: ¿Qué necesita un inventario?
- Registrar productos (nombre, precio, categoría)
- Controlar stock (entradas y salidas)
- Reportes (stock bajo, valor total, por categoría)

## Paso 2: Elegir estructuras de datos
```
HashMap<String, Producto>  → buscar producto por SKU en O(1)
HashMap<String, Integer>   → stock actual por SKU
ArrayList<Movimiento>      → historial de entradas/salidas
TreeMap                    → reportes ordenados
```

## Paso 3: Flujo del programa
```
1. Registrar productos (se agregan al HashMap)
2. Hacer entradas (stock += cantidad, registrar movimiento)
3. Hacer salidas (validar stock, stock -= cantidad, registrar)
4. Generar reportes (streams sobre las colecciones)
```

## Paso 4: Ejecutar
```bash
cd academia-profesional/proyecto-03-inventario/src
javac SistemaInventario.java && java SistemaInventario
```
Verás: registro de productos, movimientos, reportes de stock, valor del inventario.
