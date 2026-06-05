# Módulo 08: ES6+ - JavaScript Moderno

## 1. Arrow Functions

```javascript
// Sintaxis corta
const sumar = (a, b) => a + b;
const cuadrado = x => x * x;

// Con cuerpo
const procesarUsuario = (usuario) => {
  const nombreCompleto = `${usuario.nombre} ${usuario.apellido}`;
  return { ...usuario, nombreCompleto };
};

// ⚠️ Arrow functions NO tienen su propio `this`
const obj = {
  nombre: 'Servicio',
  // ❌ arrow function hereda this del scope externo
  metodoMalo: () => console.log(this.nombre),  // undefined
  // ✅ function normal tiene su propio this
  metodoBueno() { console.log(this.nombre); }  // 'Servicio'
};
```

## 2. Destructuring

```javascript
// Objetos
const { nombre, email, rol = 'user' } = usuario;

// Renombrar
const { nombre: userName, id: userId } = response.data;

// Anidado
const { direccion: { ciudad, pais } } = usuario;

// Arrays
const [primero, segundo, ...resto] = [1, 2, 3, 4, 5];
// primero=1, segundo=2, resto=[3,4,5]

// En parámetros de función
function crearPedido({ productoId, cantidad, precio }) {
  return { productoId, cantidad, total: cantidad * precio };
}
```

## 3. Spread / Rest

```javascript
// Spread → expandir
const config = { ...defaultConfig, ...userConfig };  // merge objetos
const todos = [...lista1, ...lista2];                // merge arrays
const copia = { ...original };                       // clonar (shallow)

// Rest → agrupar
function log(mensaje, ...args) {
  console.log(`[INFO] ${mensaje}`, ...args);
}

// Patrón: separar una propiedad del resto
const { password, ...usuarioSeguro } = usuario;
// usuarioSeguro tiene todo MENOS password
```

## 4. Modules (import/export)

```javascript
// producto.service.ts (named exports)
export const API_URL = '/api/productos';
export function getProductos() { /* ... */ }
export class ProductoService { /* ... */ }

// app.ts (named imports)
import { ProductoService, API_URL } from './producto.service';

// default export (uno por archivo)
export default class AuthService { /* ... */ }
import AuthService from './auth.service';

// Re-export (barrel file: index.ts)
export { ProductoService } from './producto.service';
export { UsuarioService } from './usuario.service';
```

## 5. Template Literals y Optional Chaining

```javascript
// Template literals
const mensaje = `Hola ${usuario.nombre}, tienes ${pedidos.length} pedidos`;

// Tagged templates (para SQL, HTML, etc.)
const query = sql`SELECT * FROM usuarios WHERE id = ${id}`;

// Optional chaining (?.)
const ciudad = usuario?.direccion?.ciudad ?? 'No especificada';
const primer = pedidos?.[0]?.total ?? 0;
const resultado = usuario?.calcularDescuento?.() ?? 0;

// Nullish coalescing (??)
const puerto = config.puerto ?? 3000;  // solo si es null/undefined
// vs OR (||) que también reemplaza 0, '', false
```

## 6. Ejercicios

1. Refactoriza un código con `var` y funciones clásicas a ES6+ (const, arrow, destructuring).
2. Crea un módulo `utils.ts` con funciones exportadas y un barrel file `index.ts`.
3. Usa spread para implementar un `merge` profundo de objetos de configuración.
4. Implementa una función que use optional chaining para acceder a datos anidados de una API.
5. Usa destructuring en parámetros para crear una función `buildQuery({ page, size, sort, filter })`.

---

## Siguiente Módulo
→ [09-Testing JS](../09-testing-js/README.md)
