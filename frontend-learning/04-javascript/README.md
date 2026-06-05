# Módulo 04: JavaScript - Fundamentos del Lenguaje

## 1. Variables: let vs const

```javascript
// const → valor que NO cambia (preferir siempre)
const API_URL = 'https://api.empresa.com';
const usuario = { nombre: 'Juan', edad: 30 };
usuario.edad = 31;  // ✅ Puedes mutar propiedades
// usuario = {};    // ❌ No puedes reasignar

// let → valor que SÍ cambia
let contador = 0;
contador++;  // ✅

// var → NUNCA usar (problemas de scope y hoisting)
```

## 2. Funciones

```javascript
// Declaración (se eleva por hoisting)
function sumar(a, b) {
  return a + b;
}

// Expresión (arrow function - NO se eleva)
const multiplicar = (a, b) => a * b;

// Con valores por defecto
const crearUsuario = (nombre, rol = 'user') => ({ nombre, rol });

// Callback
const numeros = [1, 2, 3, 4, 5];
const pares = numeros.filter(n => n % 2 === 0);  // [2, 4]
const dobles = numeros.map(n => n * 2);           // [2, 4, 6, 8, 10]
const suma = numeros.reduce((acc, n) => acc + n, 0); // 15
```

## 3. Objetos y Arrays

```javascript
// Objetos
const producto = {
  id: 1,
  nombre: 'Laptop',
  precio: 999,
  getTotal(cantidad) {
    return this.precio * cantidad;
  }
};

// Destructuring
const { nombre, precio } = producto;

// Spread
const productoActualizado = { ...producto, precio: 899 };

// Arrays
const usuarios = [
  { id: 1, nombre: 'Ana', activo: true },
  { id: 2, nombre: 'Luis', activo: false },
];
const activos = usuarios.filter(u => u.activo);
const nombres = usuarios.map(u => u.nombre);
const encontrado = usuarios.find(u => u.id === 1);
```

## 4. Closures

```javascript
// Un closure "recuerda" el scope donde fue creado
function crearContador(inicial = 0) {
  let count = inicial;
  return {
    incrementar: () => ++count,
    decrementar: () => --count,
    getValor: () => count,
  };
}

const contador = crearContador(10);
contador.incrementar(); // 11
contador.incrementar(); // 12
contador.getValor();    // 12
```

## 5. Hoisting y Scope

```javascript
// Hoisting: las declaraciones se "elevan" al inicio
console.log(x); // undefined (var se eleva, pero no su valor)
var x = 5;

// console.log(y); // ❌ ReferenceError (let NO se eleva)
let y = 5;

// Scope
function ejemplo() {
  if (true) {
    var a = 1;   // scope de función (accesible fuera del if)
    let b = 2;   // scope de bloque (solo dentro del if)
    const c = 3; // scope de bloque
  }
  console.log(a); // 1
  // console.log(b); // ❌ ReferenceError
}
```

## 6. Ejercicios

1. Crea una función `agruparPor(array, propiedad)` que agrupe objetos por una propiedad.
2. Implementa un closure que simule un carrito de compras (agregar, eliminar, getTotal).
3. Usa `map`, `filter` y `reduce` para: dado un array de productos, obtener el total de los activos.
4. Explica qué imprime este código y por qué:
   ```javascript
   for (var i = 0; i < 3; i++) {
     setTimeout(() => console.log(i), 100);
   }
   ```
5. Crea una función `pipe(...fns)` que componga funciones de izquierda a derecha.

---

## Siguiente Módulo
→ [05-TypeScript](../05-typescript/README.md)
