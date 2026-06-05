/**
 * MÓDULO 04: JavaScript - Ejercicios Prácticos
 * Ejecutar: node ejercicios.js
 */

console.log('═══════════════════════════════════════');
console.log('  MÓDULO 04: JavaScript Fundamentals');
console.log('═══════════════════════════════════════\n');

// ═══════ 1. VARIABLES Y SCOPE ═══════
console.log('── 1. Variables y Scope ──');

// let: variable que puede cambiar, scope de bloque
let contador = 0;
contador = 1; // OK

// const: constante, no se puede reasignar
const PI = 3.14159;
// PI = 3; // ERROR: Assignment to constant variable

// const con objetos: el objeto puede mutar, la referencia no
const producto = { nombre: 'Laptop', precio: 18999 };
producto.precio = 19999; // OK (mutas el objeto)
// producto = {}; // ERROR (reasignas la referencia)

console.log('Producto:', producto);

// ═══════ 2. FUNCIONES ═══════
console.log('\n── 2. Funciones ──');

// Function declaration (hoisted: puedes usarla antes de declararla)
function sumar(a, b) {
    return a + b;
}

// Arrow function (no hoisted, más concisa)
const multiplicar = (a, b) => a * b;

// Arrow con cuerpo
const calcularIVA = (precio) => {
    const iva = precio * 0.16;
    return { precio, iva, total: precio + iva };
};

console.log('Suma:', sumar(5, 3));
console.log('Multiplicar:', multiplicar(4, 7));
console.log('IVA:', calcularIVA(1000));

// ═══════ 3. OBJETOS ═══════
console.log('\n── 3. Objetos ──');

const empleado = {
    nombre: 'Carlos',
    apellido: 'García',
    salario: 45000,
    puesto: 'Developer',
    // Método
    nombreCompleto() {
        return `${this.nombre} ${this.apellido}`;
    },
    // Getter
    get salarioMensual() {
        return this.salario / 12;
    }
};

console.log('Empleado:', empleado.nombreCompleto());
console.log('Salario mensual:', empleado.salarioMensual.toFixed(2));

// Destructuring
const { nombre, salario, puesto } = empleado;
console.log(`${nombre} es ${puesto} y gana $${salario}`);

// ═══════ 4. ARRAYS Y MÉTODOS ═══════
console.log('\n── 4. Arrays ──');

const productos = [
    { id: 1, nombre: 'Laptop', precio: 18999, categoria: 'Electrónica' },
    { id: 2, nombre: 'Mouse', precio: 899, categoria: 'Periféricos' },
    { id: 3, nombre: 'Monitor', precio: 12499, categoria: 'Electrónica' },
    { id: 4, nombre: 'Teclado', precio: 2899, categoria: 'Periféricos' },
    { id: 5, nombre: 'Silla', precio: 8999, categoria: 'Mobiliario' },
];

// filter: filtrar elementos
const caros = productos.filter(p => p.precio > 5000);
console.log('Productos > $5000:', caros.map(p => p.nombre));

// map: transformar cada elemento
const nombres = productos.map(p => p.nombre.toUpperCase());
console.log('Nombres:', nombres);

// reduce: combinar en un solo valor
const total = productos.reduce((sum, p) => sum + p.precio, 0);
console.log('Total inventario: $' + total);

// find: encontrar uno
const laptop = productos.find(p => p.nombre === 'Laptop');
console.log('Encontrado:', laptop);

// sort: ordenar (muta el array original)
const ordenados = [...productos].sort((a, b) => b.precio - a.precio);
console.log('Más caro:', ordenados[0].nombre);

// ═══════ 5. CLOSURES ═══════
console.log('\n── 5. Closures ──');

// Un closure es una función que "recuerda" las variables de su scope externo
function crearContador(inicio = 0) {
    let count = inicio; // Esta variable "vive" dentro del closure
    return {
        incrementar: () => ++count,
        decrementar: () => --count,
        valor: () => count,
    };
}

const miContador = crearContador(10);
console.log('Valor:', miContador.valor());       // 10
console.log('Incrementar:', miContador.incrementar()); // 11
console.log('Incrementar:', miContador.incrementar()); // 12
console.log('Decrementar:', miContador.decrementar()); // 11

// ═══════ 6. SPREAD Y REST ═══════
console.log('\n── 6. Spread y Rest ──');

// Spread: "expandir" un array/objeto
const arr1 = [1, 2, 3];
const arr2 = [...arr1, 4, 5, 6]; // [1,2,3,4,5,6]
console.log('Spread array:', arr2);

const original = { a: 1, b: 2 };
const copia = { ...original, c: 3 }; // {a:1, b:2, c:3}
console.log('Spread objeto:', copia);

// Rest: "recoger" argumentos restantes
function sumarTodos(...numeros) {
    return numeros.reduce((sum, n) => sum + n, 0);
}
console.log('Rest:', sumarTodos(1, 2, 3, 4, 5)); // 15

// ═══════ 7. OPTIONAL CHAINING Y NULLISH COALESCING ═══════
console.log('\n── 7. Optional Chaining ──');

const usuario = {
    nombre: 'Ana',
    direccion: { ciudad: 'CDMX' }
    // No tiene "telefono"
};

// Sin optional chaining: usuario.telefono.numero → ERROR
// Con optional chaining: seguro
console.log('Ciudad:', usuario?.direccion?.ciudad); // 'CDMX'
console.log('Teléfono:', usuario?.telefono?.numero); // undefined (no error)

// Nullish coalescing: valor por defecto solo si es null/undefined
const config = { timeout: 0, nombre: '' };
console.log('Timeout:', config.timeout ?? 5000); // 0 (no es null)
console.log('Nombre:', config.nombre ?? 'default'); // '' (no es null)
// vs OR que trata 0 y '' como falsy:
console.log('Timeout ||:', config.timeout || 5000); // 5000 (0 es falsy)

console.log('\n═══════════════════════════════════════');
console.log('  FIN - Ejecuta: node ejercicios.js');
console.log('═══════════════════════════════════════');
