/**
 * SOLUCIONES - Ejercicios JavaScript B041-B060
 * Ejecutar: node soluciones-javascript-B041-B060.js
 */

console.log('═══ SOLUCIONES JavaScript B041-B060 ═══\n');

// B041: Declarar variables con let/const
console.log('── B041: Variables ──');
const PI = 3.14159;
let contador = 0;
const producto = { nombre: 'Laptop', precio: 18999 };
producto.precio = 19999; // OK: mutas objeto, no reasignas referencia
console.log('PI:', PI, '| Producto:', producto);

// B042: Arrow functions
console.log('\n── B042: Arrow Functions ──');
const sumar = (a, b) => a + b;
const cuadrado = n => n * n;
const saludar = (nombre) => `Hola ${nombre}!`;
const crearProducto = (nombre, precio) => ({ nombre, precio }); // Retorna objeto
console.log(sumar(5, 3), cuadrado(4), saludar('Carlos'));
console.log(crearProducto('Mouse', 899));

// B043: map, filter, reduce
console.log('\n── B043: map/filter/reduce ──');
const productos = [
    { nombre: 'Laptop', precio: 18999, categoria: 'Electrónica' },
    { nombre: 'Mouse', precio: 899, categoria: 'Periféricos' },
    { nombre: 'Monitor', precio: 12499, categoria: 'Electrónica' },
    { nombre: 'Teclado', precio: 2899, categoria: 'Periféricos' },
    { nombre: 'Silla', precio: 8999, categoria: 'Mobiliario' },
];

const nombres = productos.map(p => p.nombre);
console.log('Nombres:', nombres);

const caros = productos.filter(p => p.precio > 5000);
console.log('Caros (>5000):', caros.map(p => p.nombre));

const total = productos.reduce((sum, p) => sum + p.precio, 0);
console.log('Total:', total);

// B044: Destructuring
console.log('\n── B044: Destructuring ──');
const { nombre, precio, ...resto } = productos[0];
console.log(`Nombre: ${nombre}, Precio: ${precio}`, resto);

const [primero, segundo, ...demas] = productos;
console.log('Primero:', primero.nombre, '| Demás:', demas.length);

// B045: Spread operator
console.log('\n── B045: Spread ──');
const copia = [...productos];
const nuevoProducto = { ...productos[0], precio: 20999, id: 6 };
console.log('Nuevo:', nuevoProducto);
const merged = { ...productos[0], ...{ stock: 25, activo: true } };
console.log('Merged:', merged);

// B046: Template literals
console.log('\n── B046: Template Literals ──');
const factura = `
╔══════════════════════════════╗
║  FACTURA                     ║
║  ${productos[0].nombre.padEnd(25)}║
║  Precio: $${productos[0].precio.toLocaleString().padStart(14)}  ║
║  IVA:    $${(productos[0].precio * 0.16).toFixed(2).padStart(14)}  ║
║  Total:  $${(productos[0].precio * 1.16).toFixed(2).padStart(14)}  ║
╚══════════════════════════════╝`;
console.log(factura);

// B047: Optional chaining (?.) y Nullish coalescing (??)
console.log('\n── B047: ?. y ?? ──');
const usuario = { nombre: 'Carlos', direccion: { ciudad: 'CDMX' } };
const usuario2 = { nombre: 'María' };
console.log(usuario?.direccion?.ciudad);   // 'CDMX'
console.log(usuario2?.direccion?.ciudad);  // undefined (no error)
console.log(usuario2?.direccion?.ciudad ?? 'Sin ciudad'); // 'Sin ciudad'

// B048: Array methods avanzados
console.log('\n── B048: Array methods ──');
console.log('find:', productos.find(p => p.precio > 10000)?.nombre);
console.log('findIndex:', productos.findIndex(p => p.nombre === 'Mouse'));
console.log('some (hay caros?):', productos.some(p => p.precio > 15000));
console.log('every (todos activos?):', productos.every(p => p.precio > 0));
console.log('flat:', [[1, 2], [3, 4], [5]].flat());
console.log('includes:', [1, 2, 3, 4, 5].includes(3));

// B049: Object methods
console.log('\n── B049: Object methods ──');
const obj = { a: 1, b: 2, c: 3 };
console.log('keys:', Object.keys(obj));
console.log('values:', Object.values(obj));
console.log('entries:', Object.entries(obj));
const fromEntries = Object.fromEntries([['x', 10], ['y', 20]]);
console.log('fromEntries:', fromEntries);

// B050: Grouping (ES2024)
console.log('\n── B050: Agrupar ──');
const porCategoria = Object.groupBy(productos, p => p.categoria);
Object.entries(porCategoria).forEach(([cat, items]) => {
    console.log(`  ${cat}: ${items.map(p => p.nombre).join(', ')}`);
});

// B051: Promises
console.log('\n── B051: Promises ──');
const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms));
const fetchSimulado = (url) => new Promise((resolve, reject) => {
    setTimeout(() => {
        if (url.includes('error')) reject(new Error('404 Not Found'));
        else resolve({ status: 200, data: [{ id: 1, nombre: 'Laptop' }] });
    }, 100);
});

// B052: async/await
async function demo52() {
    console.log('\n── B052: async/await ──');
    try {
        const res = await fetchSimulado('/api/productos');
        console.log('  Respuesta:', res);
    } catch (err) {
        console.log('  Error:', err.message);
    }
}

// B053: Promise.all
async function demo53() {
    console.log('\n── B053: Promise.all (paralelo) ──');
    const [p1, p2] = await Promise.all([
        fetchSimulado('/api/productos'),
        fetchSimulado('/api/clientes')
    ]);
    console.log('  Ambos resueltos:', p1.status, p2.status);
}

// B054: Closure - contador
console.log('\n── B054: Closure ──');
function crearContador(inicial = 0) {
    let valor = inicial;
    return {
        incrementar: () => ++valor,
        decrementar: () => --valor,
        getValor: () => valor,
        reset: () => { valor = inicial; return valor; }
    };
}
const cnt = crearContador(10);
console.log('  Incrementar:', cnt.incrementar(), cnt.incrementar());
console.log('  Valor:', cnt.getValor());
console.log('  Reset:', cnt.reset());

// B055: Higher-Order Functions
console.log('\n── B055: Higher-Order Functions ──');
const filtrarPor = (campo, valor) => (lista) => lista.filter(item => item[campo] === valor);
const ordenarPor = (campo, desc = false) => (lista) =>
    [...lista].sort((a, b) => desc ? b[campo] - a[campo] : a[campo] - b[campo]);

const soloElectronica = filtrarPor('categoria', 'Electrónica');
const porPrecioDesc = ordenarPor('precio', true);

console.log('  Electrónica:', soloElectronica(productos).map(p => p.nombre));
console.log('  Por precio desc:', porPrecioDesc(productos).map(p => `${p.nombre}($${p.precio})`));

// B056: Debounce
console.log('\n── B056: Debounce ──');
function debounce(fn, delay) {
    let timer;
    return (...args) => {
        clearTimeout(timer);
        timer = setTimeout(() => fn(...args), delay);
    };
}
const buscar = debounce((term) => console.log(`  Buscando: "${term}"`), 300);
buscar('l'); buscar('la'); buscar('lap'); buscar('laptop');
// Solo imprime "laptop" (las anteriores se cancelan)

// B057: Curry
console.log('\n── B057: Curry ──');
const multiplicar = (a) => (b) => a * b;
const duplicar = multiplicar(2);
const triplicar = multiplicar(3);
console.log('  duplicar(5):', duplicar(5));
console.log('  triplicar(5):', triplicar(5));

// B058: Pipe (composición)
console.log('\n── B058: Pipe ──');
const pipe = (...fns) => (x) => fns.reduce((acc, fn) => fn(acc), x);
const procesarPrecio = pipe(
    (p) => p * 0.9,       // 10% descuento
    (p) => p * 1.16,      // + IVA
    (p) => Math.round(p * 100) / 100,  // Redondear 2 decimales
    (p) => `$${p.toLocaleString()}`     // Formatear
);
console.log('  18999 procesado:', procesarPrecio(18999));

// B059: Map como alternativa a objetos (cache)
console.log('\n── B059: Map ──');
const cache = new Map();
function obtenerConCache(id) {
    if (cache.has(id)) { console.log('  Cache hit!'); return cache.get(id); }
    const resultado = `Producto-${id}`;  // Simulación
    cache.set(id, resultado);
    return resultado;
}
console.log('  1ra vez:', obtenerConCache(1));
console.log('  2da vez:', obtenerConCache(1));

// B060: Generators
console.log('\n── B060: Generators ──');
function* rangos(inicio, fin, paso = 1) {
    for (let i = inicio; i <= fin; i += paso) yield i;
}
console.log('  Rango 1-10 paso 2:', [...rangos(1, 10, 2)]);

function* fibonacci() {
    let a = 0, b = 1;
    while (true) { yield a;[a, b] = [b, a + b]; }
}
const fib = fibonacci();
const primeros10 = Array.from({ length: 10 }, () => fib.next().value);
console.log('  Fibonacci(10):', primeros10);

// Ejecutar async demos
(async () => {
    await demo52();
    await demo53();
    // Esperar al debounce
    await delay(500);
    console.log('\n═══ FIN ═══');
})();
