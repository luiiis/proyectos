# Proyecto 03: Sistema de Inventario

## ¿Qué problema resuelve?
Dominar las colecciones de Java construyendo un sistema de inventario que usa HashMap para productos, ArrayList para movimientos y TreeMap para reportes ordenados.

## Tecnologías
- Java 21
- HashMap (catálogo de productos por código)
- ArrayList (registro de movimientos)
- TreeMap (reportes ordenados por fecha/nombre)
- LinkedList (cola de pedidos pendientes)
- Stream API para filtros y reportes
- Comparator/Comparable para ordenamiento

## Funcionalidades
- CRUD completo de productos
- Registro de entradas y salidas de inventario
- Alertas de stock mínimo
- Reportes de movimientos por fecha
- Búsqueda por nombre, categoría o código
- Valorización total del inventario
- Top productos más movidos
- Exportar reporte a consola formateada

## Estructura de Datos
- `HashMap<String, Producto>` - acceso O(1) por código
- `ArrayList<Movimiento>` - historial cronológico
- `TreeMap<LocalDate, List<Movimiento>>` - movimientos por fecha ordenados
- `PriorityQueue<Producto>` - productos con stock crítico

## Conceptos Clave
- Cuándo usar HashMap vs TreeMap vs LinkedHashMap
- Iteración eficiente con Stream API
- Generics en colecciones tipadas
- Inmutabilidad con Collections.unmodifiableList()
