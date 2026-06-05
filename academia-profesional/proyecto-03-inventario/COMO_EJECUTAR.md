# Cómo Ejecutar - Proyecto 03: Sistema de Inventario

## Requisitos
- Java 21+ instalado (`java -version`)

## Compilar y Ejecutar

```bash
cd academia-profesional/proyecto-03-inventario/src

# Compilar todas las clases
javac *.java

# Ejecutar
java Main
```

## Compilación con estructura de paquetes

```bash
cd academia-profesional/proyecto-03-inventario

# Compilar
javac -d bin src/**/*.java

# Ejecutar
java -cp bin com.inventario.Main
```

## Uso
```
═══ SISTEMA DE INVENTARIO ═══
1. Agregar producto
2. Registrar entrada
3. Registrar salida
4. Buscar producto
5. Reporte de stock bajo
6. Movimientos por fecha
7. Valorización total
8. Top productos
9. Salir
Opción: 5
⚠️  Productos con stock bajo:
  [P001] Teclado USB - Stock: 2 (Mínimo: 5)
  [P007] Mouse Wireless - Stock: 1 (Mínimo: 3)
```

## Solución de errores

| Error | Causa | Solución |
|-------|-------|----------|
| `javac: command not found` | Java no instalado | Instalar JDK 21 |
| `ConcurrentModificationException` | Modificar colección mientras se itera | Usar Iterator.remove() o Stream |
| `NullPointerException` | Producto no existe en HashMap | Validar con containsKey() antes |
