# Entrevista Frontend - JavaScript y TypeScript

## Nivel Junior

### 1. ¿Cuál es la diferencia entre `==` y `===`?
**Respuesta:**
- `==` (igualdad débil): convierte tipos antes de comparar. `"5" == 5` → true
- `===` (igualdad estricta): compara valor Y tipo. `"5" === 5` → false

**Regla:** SIEMPRE usar `===`. Nunca `==`.

---

### 2. ¿Qué es hoisting?
**Respuesta:**
JavaScript "mueve" declaraciones al inicio de su scope antes de ejecutar:
```javascript
console.log(x); // undefined (no error, porque var se "elevó")
var x = 5;

console.log(y); // ReferenceError (let/const NO se elevan)
let y = 10;

saludar(); // Funciona (function declarations se elevan)
function saludar() { console.log("Hola"); }
```

---

### 3. ¿Cuál es la diferencia entre `null` y `undefined`?
**Respuesta:**
- `undefined`: variable declarada pero sin valor asignado. JavaScript lo asigna.
- `null`: ausencia INTENCIONAL de valor. El programador lo asigna.

```javascript
let x;          // undefined (no tiene valor)
let y = null;   // null (explícitamente "vacío")
typeof undefined // "undefined"
typeof null      // "object" (bug histórico de JS)
```

---

### 4. ¿Qué es un closure?
**Respuesta:**
Una función que "recuerda" las variables del scope donde fue creada, incluso después de que ese scope terminó:
```javascript
function crearContador() {
  let count = 0;  // Variable "capturada" por el closure
  return {
    incrementar: () => ++count,
    getValor: () => count
  };
}
const contador = crearContador();
contador.incrementar(); // 1
contador.incrementar(); // 2
// count no es accesible directamente (encapsulamiento)
```

---

### 5. ¿Qué es la diferencia entre `map`, `filter` y `reduce`?
**Respuesta:**
```javascript
const nums = [1, 2, 3, 4, 5];

// map: transforma cada elemento (1:1)
nums.map(n => n * 2);        // [2, 4, 6, 8, 10]

// filter: selecciona elementos que cumplen condición
nums.filter(n => n > 3);     // [4, 5]

// reduce: acumula todo en un valor
nums.reduce((acc, n) => acc + n, 0);  // 15 (suma)
```

---

### 6. ¿Qué es destructuring?
**Respuesta:**
Extraer valores de objetos/arrays en variables separadas:
```javascript
// Objetos:
const { nombre, precio, ...resto } = { nombre: "Laptop", precio: 18999, stock: 25 };

// Arrays:
const [primero, segundo, ...demas] = [1, 2, 3, 4, 5];

// En parámetros:
function mostrar({ nombre, precio }) {
  console.log(`${nombre}: $${precio}`);
}
```

---

### 7. ¿Qué es async/await?
**Respuesta:**
Forma de escribir código asíncrono que PARECE síncrono:
```javascript
// Con Promises (encadenado):
fetch('/api/productos')
  .then(res => res.json())
  .then(data => console.log(data))
  .catch(err => console.error(err));

// Con async/await (más legible):
async function cargarProductos() {
  try {
    const res = await fetch('/api/productos');
    const data = await res.json();
    console.log(data);
  } catch (err) {
    console.error(err);
  }
}
```

---

## TypeScript

### 8. ¿Qué es TypeScript y por qué usarlo?
**Respuesta:**
TypeScript = JavaScript + tipos estáticos. Se compila a JavaScript.

Beneficios:
- Errores en COMPILACIÓN (no en runtime)
- Autocompletado en IDE
- Refactoring seguro
- Documentación implícita (los tipos SON la doc)

---

### 9. ¿Cuál es la diferencia entre `interface` y `type`?
**Respuesta:**
```typescript
// Interface: para objetos, se puede extender
interface Producto {
  nombre: string;
  precio: number;
}
interface ProductoConStock extends Producto {
  stock: number;
}

// Type: para cualquier tipo, más flexible
type ID = string | number;                    // Union
type Respuesta = { data: Producto } | null;  // Union con objeto
type Punto = [number, number];               // Tuple
```
Regla: usa `interface` para objetos. `type` para uniones, intersecciones, tipos complejos.

---

### 10. ¿Qué son los Generics en TypeScript?
**Respuesta:**
Tipos parametrizados que funcionan con cualquier tipo:
```typescript
// Función genérica:
function primero<T>(arr: T[]): T | undefined {
  return arr[0];
}
primero<string>(["a","b"]); // tipo: string
primero([1, 2, 3]);         // tipo inferido: number

// Interface genérica:
interface ApiResponse<T> {
  data: T;
  status: number;
  message: string;
}
// ApiResponse<Producto[]> → data es Producto[]
// ApiResponse<Usuario>    → data es Usuario
```

---

## Nivel Mid

### 11. ¿Qué es el Event Loop?
**Respuesta:**
Mecanismo que permite a JavaScript (single-threaded) manejar async:
```
1. Call Stack: ejecuta código síncrono
2. Cuando hay async (setTimeout, fetch): va a Web APIs
3. Al completarse: el callback va a la Task Queue
4. Event Loop: cuando Call Stack está vacío → mueve de Queue a Stack
```
Por eso `setTimeout(fn, 0)` no se ejecuta inmediato: espera a que el stack se vacíe.

---

### 12. ¿Qué son los decoradores en TypeScript?
**Respuesta:**
Funciones que modifican clases, métodos o propiedades (metaprogramación):
```typescript
// Angular los usa extensivamente:
@Component({ selector: 'app-root', template: '...' })
class AppComponent {}

@Injectable({ providedIn: 'root' })
class MiServicio {}

// Custom decorator:
function Log(target: any, key: string, descriptor: PropertyDescriptor) {
  const original = descriptor.value;
  descriptor.value = function(...args: any[]) {
    console.log(`Llamando ${key} con args:`, args);
    return original.apply(this, args);
  };
}
```

---

### 13. ¿Qué es el patrón Module en JavaScript?
**Respuesta:**
Encapsular código en módulos que exportan solo lo necesario:
```javascript
// ES Modules (estándar):
// math.js
export const PI = 3.14159;
export function sumar(a, b) { return a + b; }
function interno() {} // NO exportado, privado

// uso.js
import { PI, sumar } from './math.js';
import * as math from './math.js';  // Todo el módulo
```

---

### 14. Explica `this` en JavaScript
**Respuesta:**
`this` depende de CÓMO se llama la función, no dónde se declara:
```javascript
const obj = {
  nombre: "Carlos",
  saludar() { console.log(this.nombre); },    // this = obj
  saludarArrow: () => console.log(this.nombre) // this = window/undefined
};
obj.saludar();      // "Carlos"
obj.saludarArrow(); // undefined (arrow NO tiene su propio this)

const fn = obj.saludar;
fn();               // undefined (this = window, se perdió el contexto)
fn.call(obj);       // "Carlos" (forzar this con call/bind)
```

---

### 15. ¿Qué son los Utility Types en TypeScript?
**Respuesta:**
```typescript
interface Producto { id: number; nombre: string; precio: number; stock: number; }

Partial<Producto>      // Todos los campos opcionales (para updates parciales)
Required<Producto>     // Todos obligatorios
Pick<Producto, 'nombre' | 'precio'>  // Solo esos campos
Omit<Producto, 'id'>  // Todos excepto id (para crear sin ID)
Readonly<Producto>     // Inmutable
Record<string, number> // { [key: string]: number }

// Uso real:
type ProductoCreate = Omit<Producto, 'id'>;
type ProductoUpdate = Partial<Omit<Producto, 'id'>>;
```
