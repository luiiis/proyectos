# Cómo se Construyó - Proyecto 02: Biblioteca

## Paso 1: Identificar las entidades del mundo real
```
Biblioteca tiene: Libros, Revistas, Usuarios, Préstamos
```

## Paso 2: Diseñar la jerarquía de clases
```
Prestable (interface) ← define qué se puede prestar
    │
MaterialBiblioteca (abstracta) ← estructura común
    ├── Libro (hereda + agrega: páginas)
    └── Revista (hereda + agrega: edición, artículos)

Usuario (record) ← datos inmutables
Prestamo (record) ← registro de quién tiene qué
Biblioteca ← orquesta todo (HashMap + ArrayList)
```

## Paso 3: Decisiones
- Interface `Prestable`: permite que en el futuro DVDs, Juegos, etc. también sean prestables
- Clase abstracta `MaterialBiblioteca`: comparte código entre Libro y Revista
- Records para Usuario y Prestamo: son solo contenedores de datos (inmutables)
- HashMap para catálogo: búsqueda O(1) por identificador
- ArrayList para préstamos: iteración frecuente

## Paso 4: Ejecutar y probar
```bash
cd academia-profesional/proyecto-02-biblioteca/src
javac *.java && java Main
```
Verás: catálogo, préstamos, devoluciones, búsqueda, estadísticas.
