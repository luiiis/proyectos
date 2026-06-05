/**
 * MÓDULO 08: ES6+ Features - Demo ejecutable
 * Ejecutar: node es6-demo.js
 */
console.log('═══ MÓDULO 08: ES6+ ═══\n');

// Arrow functions
const sumar = (a, b) => a + b;
const saludar = nombre => `Hola, ${nombre}!`;
console.log('Arrow:', sumar(3, 4), saludar('Carlos'));

// Destructuring
const { nombre, precio, ...resto } = { nombre: 'Laptop', precio: 18999, stock: 25, sku: 'LP-001' };
console.log('\nDestructuring objeto:', nombre, precio, resto);

const [primero, segundo, ...demas] = [1, 2, 3, 4, 5];
console.log('Destructuring array:', primero, segundo, demas);

// Spread
const arr1 = [1, 2, 3];
const arr2 = [...arr1, 4, 5, 6];
console.log('\nSpread array:', arr2);

const obj1 = { a: 1, b: 2 };
const obj2 = { ...obj1, c: 3, b: 99 }; // b se sobreescribe
console.log('Spread objeto:', obj2);

// Template literals
const producto = { nombre: 'Monitor', precio: 12499 };
console.log(`\nTemplate: ${producto.nombre} cuesta $${producto.precio.toLocaleString()}`);

// Optional chaining + Nullish coalescing
const user = { nombre: 'Ana', direccion: { ciudad: 'CDMX' } };
console.log('\nOptional chaining:', user?.direccion?.ciudad); // 'CDMX'
console.log('Sin propiedad:', user?.telefono?.numero); // undefined (no error)
console.log('Nullish:', user?.telefono ?? 'Sin teléfono'); // 'Sin teléfono'

// Modules (import/export) - se usa en archivos separados
console.log('\n── Modules (import/export) ──');
console.log('  export const API_URL = "http://localhost:8080";');
console.log('  import { API_URL } from "./config.js";');

console.log('\n✓ ES6+ demo completada');
