ahora # Frontend Completo - Parte 1: JavaScript al 100%

---

# 1. QUE ES JAVASCRIPT

JavaScript es el UNICO lenguaje que los navegadores entienden nativamente.
Todo lo que ves interactivo en una pagina web (menus, animaciones, formularios
que validan, datos que se cargan sin recargar) es JavaScript.

```
HTML = estructura (que se muestra)
CSS  = estilo (como se ve)
JS   = comportamiento (que HACE)
```

---

# 2. VARIABLES

```javascript
// var: EVITAR (scope confuso, se puede redeclarar)
var nombre = "Carlos";  // Legacy, no usar en codigo nuevo

// let: variable que PUEDE cambiar
let contador = 0;
contador = 1;  // OK

// const: constante que NO puede reasignarse
const PI = 3.14159;
// PI = 3;  // ERROR: Assignment to constant variable

// PERO: const con objetos permite MUTAR el contenido
const producto = { nombre: "Laptop", precio: 18999 };
producto.precio = 19999;  // OK (mutas el objeto)
// producto = {};  // ERROR (reasignas la referencia)

// REGLA: usar const SIEMPRE, let solo cuando necesites reasignar, var NUNCA
```

## Tipos de datos

```javascript
// Primitivos (inmutables, se comparan por VALOR)
let texto = "Hola";           // string
let numero = 42;              // number (entero y decimal son lo mismo)
let decimal = 3.14;           // number
let booleano = true;          // boolean
let nulo = null;              // null (ausencia intencional de valor)
let indefinido = undefined;   // undefined (no se ha asignado)
let grande = 9007199254740991n; // bigint (numeros muy grandes)
let simbolo = Symbol("id");   // symbol (identificador unico)

// Referencia (mutables, se comparan por REFERENCIA)
let array = [1, 2, 3];
let objeto = { nombre: "Carlos" };
let funcion = () => "hola";

// typeof: verificar tipo
typeof "hola"     // "string"
typeof 42         // "number"
typeof true       // "boolean"
typeof null       // "object" (bug historico de JS, nunca se corrigio)
typeof undefined  // "undefined"
typeof []         // "object" (arrays son objetos)
typeof {}         // "object"
```

## Comparacion: == vs ===

```javascript
// == (igualdad DEBIL): convierte tipos antes de comparar
"5" == 5      // true (convierte "5" a numero)
0 == false    // true (convierte false a 0)
null == undefined  // true
"" == false   // true

// === (igualdad ESTRICTA): NO convierte tipos
"5" === 5     // false (string vs number)
0 === false   // false (number vs boolean)

// REGLA: usar SIEMPRE === (nunca ==)
// La unica excepcion: valor == null (detecta null Y undefined)
```

---

# 3. FUNCIONES

```javascript
// Function declaration (hoisted: puedes usarla antes de declararla)
function sumar(a, b) {
    return a + b;
}

// Function expression (NO hoisted)
const restar = function(a, b) {
    return a - b;
};

// Arrow function (ES6+): mas concisa, no tiene su propio "this"
const multiplicar = (a, b) => a * b;

// Arrow con cuerpo (multiples lineas)
const calcularIVA = (precio) => {
    const iva = precio * 0.16;
    return { precio, iva, total: precio + iva };
};

// Parametros default
const saludar = (nombre = "Mundo") => `Hola, ${nombre}!`;
saludar();         // "Hola, Mundo!"
saludar("Carlos"); // "Hola, Carlos!"

// Rest parameters (recibir N argumentos)
const sumarTodos = (...numeros) => numeros.reduce((sum, n) => sum + n, 0);
sumarTodos(1, 2, 3, 4, 5);  // 15
```

## Closures (funciones que "recuerdan" su entorno)

```javascript
// Un closure es una funcion que accede a variables de su scope EXTERNO
// incluso despues de que la funcion externa haya terminado

function crearContador(inicio = 0) {
    let count = inicio;  // Esta variable "vive" dentro del closure
    return {
        incrementar: () => ++count,
        decrementar: () => --count,
        valor: () => count,
    };
}

const miContador = crearContador(10);
miContador.incrementar();  // 11
miContador.incrementar();  // 12
miContador.valor();        // 12
// "count" no es accesible desde fuera (encapsulamiento)

// Uso real: crear funciones con "memoria" (cache, contadores, configuracion)
```

---

# 4. OBJETOS

```javascript
// Crear objeto
const producto = {
    nombre: "Laptop HP",
    precio: 18999.99,
    stock: 25,
    activo: true,
    
    // Metodo (funcion dentro del objeto)
    getPrecioConIVA() {
        return this.precio * 1.16;
    },
    
    // Getter (se accede como propiedad, no como funcion)
    get disponible() {
        return this.stock > 0 && this.activo;
    }
};

producto.nombre;           // "Laptop HP"
producto.getPrecioConIVA(); // 22039.99
producto.disponible;       // true (sin parentesis, es getter)

// Destructuring: extraer propiedades en variables
const { nombre, precio, stock } = producto;
console.log(nombre);  // "Laptop HP"

// Spread: copiar/combinar objetos
const actualizado = { ...producto, precio: 19999, stock: 20 };
// Copia todo de producto PERO sobreescribe precio y stock

// Optional chaining: acceder a propiedades que pueden no existir
const usuario = { nombre: "Ana", direccion: { ciudad: "CDMX" } };
usuario?.direccion?.ciudad;    // "CDMX"
usuario?.telefono?.numero;     // undefined (no error)

// Nullish coalescing: valor default solo si es null/undefined
const config = { timeout: 0, nombre: "" };
config.timeout ?? 5000;  // 0 (no es null/undefined)
config.nombre ?? "default";  // "" (no es null/undefined)
// vs OR (||) que trata 0 y "" como falsy:
config.timeout || 5000;  // 5000 (0 es falsy con ||)
```

---

# 5. ARRAYS Y METODOS

```javascript
const productos = [
    { id: 1, nombre: "Laptop", precio: 18999, categoria: "Electronica" },
    { id: 2, nombre: "Mouse", precio: 899, categoria: "Perifericos" },
    { id: 3, nombre: "Monitor", precio: 12499, categoria: "Electronica" },
    { id: 4, nombre: "Teclado", precio: 2899, categoria: "Perifericos" },
    { id: 5, nombre: "Silla", precio: 8999, categoria: "Mobiliario" },
];

// FILTER: filtrar elementos (devuelve nuevo array)
const caros = productos.filter(p => p.precio > 5000);
// [{Laptop}, {Monitor}, {Silla}]

// MAP: transformar cada elemento (devuelve nuevo array)
const nombres = productos.map(p => p.nombre);
// ["Laptop", "Mouse", "Monitor", "Teclado", "Silla"]

// FIND: encontrar UNO (devuelve el primero que cumple)
const laptop = productos.find(p => p.nombre === "Laptop");
// {id:1, nombre:"Laptop", ...}

// SOME: al menos uno cumple? (devuelve boolean)
const hayBaratos = productos.some(p => p.precio < 1000);  // true

// EVERY: todos cumplen? (devuelve boolean)
const todosCaros = productos.every(p => p.precio > 500);  // true

// REDUCE: combinar todo en un solo valor
const total = productos.reduce((sum, p) => sum + p.precio, 0);
// 44295

// SORT: ordenar (MUTA el array original, usar [...] para copiar)
const porPrecio = [...productos].sort((a, b) => b.precio - a.precio);
// Ordenado de mayor a menor precio

// INCLUDES: contiene un valor?
[1, 2, 3].includes(2);  // true

// FLAT: aplanar arrays anidados
[[1,2], [3,4], [5,6]].flat();  // [1,2,3,4,5,6]

// ENCADENAR metodos (pipeline)
const resultado = productos
    .filter(p => p.categoria === "Electronica")
    .map(p => ({ nombre: p.nombre, precioConIVA: p.precio * 1.16 }))
    .sort((a, b) => b.precioConIVA - a.precioConIVA);
// [{nombre:"Laptop", precioConIVA:22038.84}, {nombre:"Monitor", precioConIVA:14498.84}]
```

---

# 6. ASINCRONISMO

## Por que existe

```
JavaScript es SINGLE-THREADED (un solo hilo de ejecucion).
Si una operacion tarda 5 segundos (llamada HTTP, leer archivo):
  - Sin async: la pagina se CONGELA 5 segundos (no puedes hacer nada)
  - Con async: la operacion se ejecuta "en segundo plano", JS sigue funcionando

Operaciones asincronas comunes:
  - fetch() (llamadas HTTP)
  - setTimeout/setInterval (timers)
  - Leer/escribir archivos
  - Acceder a BD
```

## Promises

```javascript
// Una Promise representa un valor que EXISTIRA EN EL FUTURO
// Estados: pending → fulfilled (exito) o rejected (error)

function buscarProducto(id) {
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            if (id > 0) {
                resolve({ id, nombre: "Laptop", precio: 18999 });
            } else {
                reject(new Error("ID invalido"));
            }
        }, 1000);  // Simula 1 segundo de espera
    });
}

// Consumir con .then/.catch
buscarProducto(1)
    .then(producto => console.log("Encontrado:", producto.nombre))
    .catch(error => console.error("Error:", error.message));
```

## Async/Await (forma moderna y legible)

```javascript
// async: la funcion SIEMPRE devuelve una Promise
// await: ESPERA a que la Promise se resuelva (sin bloquear el hilo)

async function cargarDatos() {
    try {
        // await "pausa" aqui hasta que fetch responda
        const response = await fetch("http://localhost:8080/api/productos");
        
        // Verificar que la respuesta sea exitosa
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}: ${response.statusText}`);
        }
        
        // Parsear JSON (tambien es async)
        const productos = await response.json();
        
        console.log("Productos:", productos.length);
        return productos;
        
    } catch (error) {
        console.error("Error cargando datos:", error.message);
        return [];  // Retornar array vacio como fallback
    }
}

// Ejecutar multiples en PARALELO (mas rapido)
async function cargarTodo() {
    const [productos, clientes, ventas] = await Promise.all([
        fetch("/api/productos").then(r => r.json()),
        fetch("/api/clientes").then(r => r.json()),
        fetch("/api/ventas").then(r => r.json()),
    ]);
    // Las 3 peticiones se ejecutan AL MISMO TIEMPO
    // Promise.all espera a que TODAS terminen
}
```

---

# 7. MODULOS (ES Modules)

```javascript
// ═══ archivo: services/producto.service.js ═══
export const API_URL = "http://localhost:8080/api";

export async function listarProductos() {
    const res = await fetch(`${API_URL}/productos`);
    return res.json();
}

export async function crearProducto(producto) {
    const res = await fetch(`${API_URL}/productos`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(producto),
    });
    return res.json();
}

// ═══ archivo: main.js ═══
import { listarProductos, crearProducto } from "./services/producto.service.js";

const productos = await listarProductos();
console.log(productos);
```

---

# 8. CLASES (ES6+)

```javascript
class Producto {
    // Campos privados (ES2022+)
    #stock;
    
    constructor(nombre, precio, stock) {
        this.nombre = nombre;
        this.precio = precio;
        this.#stock = stock;
    }
    
    // Getter
    get precioConIVA() {
        return this.precio * 1.16;
    }
    
    // Metodo
    vender(cantidad) {
        if (cantidad > this.#stock) {
            throw new Error(`Stock insuficiente: ${this.#stock}`);
        }
        this.#stock -= cantidad;
    }
    
    // Metodo estatico (no necesita instancia)
    static crear(datos) {
        return new Producto(datos.nombre, datos.precio, datos.stock);
    }
}

// Herencia
class ProductoDigital extends Producto {
    constructor(nombre, precio, licencia) {
        super(nombre, precio, Infinity);  // Stock infinito
        this.licencia = licencia;
    }
}

const laptop = new Producto("Laptop", 18999, 25);
laptop.precioConIVA;  // 22038.84
laptop.vender(3);     // stock: 22
```

---

# 9. EJERCICIOS

1. Implementa una funcion `agruparPor(array, campo)` que agrupe objetos por un campo
2. Implementa `debounce(fn, ms)` que espere X ms sin llamadas antes de ejecutar
3. Implementa una clase `Carrito` con: agregar, eliminar, total, aplicarDescuento
4. Usa fetch + async/await para consumir una API publica (jsonplaceholder.typicode.com)
5. Implementa un mini sistema de eventos (EventEmitter) con on(), emit(), off()
