# Examen Fase 1: Web Fundamentals (Módulos 1-9)

## Instrucciones
- Tiempo: 90 minutos
- Puedes usar VS Code sin copilot/IA
- Los archivos deben funcionar en el navegador

---

## Sección A: Teoría (20 puntos)

1. ¿Qué pasa cuando escribes una URL en el navegador? (DNS → HTTP → render)
2. ¿Cuál es la diferencia entre `localStorage`, `sessionStorage` y cookies?
3. Explica el Event Loop con un ejemplo de `setTimeout` + `Promise`
4. ¿Qué es el box model? ¿Cómo funciona `box-sizing: border-box`?
5. ¿Cuál es la diferencia entre `interface` y `type` en TypeScript?

---

## Sección B: HTML + CSS (20 puntos)

### B1. Crear un layout responsive (10 pts)
Crea una página con:
- Header con logo y navegación
- Sidebar (250px en desktop, se oculta en mobile)
- Contenido principal con grid de 3 cards por fila (1 en mobile)
- Footer

Requisitos: CSS Grid o Flexbox, responsive sin JavaScript, semántica HTML5.

### B2. Card de producto (10 pts)
Crea un componente card con:
- Imagen del producto
- Nombre, precio, rating (estrellas)
- Botón "Agregar al carrito" con hover effect
- Sombra y border-radius
- Transition al hacer hover (scale + shadow)

---

## Sección C: JavaScript (30 puntos)

### C1. Manipular array de objetos (10 pts)
Dado:
```javascript
const productos = [
  { id: 1, nombre: "Laptop", precio: 18999, categoria: "Electrónica", stock: 25 },
  { id: 2, nombre: "Mouse", precio: 899, categoria: "Periféricos", stock: 50 },
  // ...10 productos más
];
```
Escribe funciones para:
1. Filtrar por categoría y rango de precio
2. Ordenar por precio (asc/desc)
3. Calcular el valor total del inventario (precio × stock)
4. Agrupar por categoría con total por grupo

### C2. Async/Await (10 pts)
Implementa una función que:
1. Haga fetch a `/api/productos` (simula con setTimeout)
2. Si falla, reintente 3 veces con delay creciente (1s, 2s, 4s)
3. Si después de 3 intentos falla → lanza error
4. Usa async/await (no .then chains)

### C3. Closure + Module Pattern (10 pts)
Implementa un "carrito de compras" usando closures:
```javascript
const carrito = crearCarrito();
carrito.agregar({ id: 1, nombre: "Laptop", precio: 18999, cantidad: 1 });
carrito.agregar({ id: 2, nombre: "Mouse", precio: 899, cantidad: 2 });
carrito.getTotal();     // 20797
carrito.getItems();     // [{...}, {...}]
carrito.remover(1);     // Elimina laptop
carrito.getTotal();     // 1798
```

---

## Sección D: TypeScript (30 puntos)

### D1. Tipos e Interfaces (10 pts)
Define tipos para un sistema de e-commerce:
- `Producto` con campos tipados
- `Carrito` con items y métodos
- `Orden` con estado (union type), fecha, items
- `ApiResponse<T>` genérica con data, error, loading

### D2. Generics (10 pts)
Implementa una clase genérica `Repository<T>`:
```typescript
interface HasId { id: number; }

class Repository<T extends HasId> {
  findAll(): T[];
  findById(id: number): T | undefined;
  create(item: Omit<T, 'id'>): T;
  update(id: number, item: Partial<T>): T;
  delete(id: number): boolean;
}
```

### D3. Utility Types (10 pts)
Dado `interface Producto { id: number; nombre: string; precio: number; stock: number; activo: boolean; }`:
1. Tipo para crear (sin id)
2. Tipo para actualizar (todo opcional excepto id)
3. Tipo para listado (solo id, nombre, precio)
4. Tipo para formulario (todo string excepto id)

---

## Aprobación
- 50+ = Aprobado (puede empezar Angular)
- 70+ = Sólido en fundamentals
- 85+ = Listo para entrevista junior frontend
