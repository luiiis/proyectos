# TypeScript Fundamentos — Lo que necesitas ANTES del Nivel 8

## ¿Qué es TypeScript?
TypeScript es JavaScript con TIPOS. Angular usa TypeScript, no JavaScript puro. Los tipos te ayudan a detectar errores ANTES de ejecutar el código.

```typescript
// JavaScript (sin tipos, puede explotar en runtime):
let precio = "hola";
precio * 2;  // NaN — no te avisa hasta que ejecutas

// TypeScript (con tipos, te avisa en el editor):
let precio: number = "hola";  // ❌ Error: "hola" no es un number
```

---

## 1. Tipos Básicos

```typescript
// Primitivos
let nombre: string = "Carlos";
let edad: number = 25;
let activo: boolean = true;
let nada: null = null;
let indefinido: undefined = undefined;

// TypeScript puede INFERIR el tipo (no siempre necesitas escribirlo)
let precio = 199.99;       // TypeScript sabe que es number
let mensaje = "Hola";     // TypeScript sabe que es string

// ¿Cuándo SÍ escribir el tipo explícito?
// Cuando no es obvio o cuando recibes datos de una API
let respuesta: any;        // evitar, pero a veces necesario
let id: number | null = null;  // puede ser number o null
```

---

## 2. Arrays

```typescript
// Array de un tipo
let numeros: number[] = [1, 2, 3, 4, 5];
let nombres: string[] = ["Ana", "Luis", "María"];

// Array de objetos (lo más común en APIs)
let productos: Producto[] = [];

// Operaciones comunes
numeros.push(6);              // agregar
numeros.filter(n => n > 3);   // filtrar → [4, 5, 6]
numeros.map(n => n * 2);      // transformar → [2, 4, 6, 8, 10, 12]
numeros.find(n => n === 3);   // buscar uno → 3
numeros.length;               // tamaño → 6
```

---

## 3. Interfaces (definir la forma de un objeto)

```typescript
// Una interface define la ESTRUCTURA de un objeto
interface Producto {
  id: number;
  nombre: string;
  precio: number;
  existencia: number;
  categoriaId: number;
  activo: boolean;
  categoriaNombre?: string;  // el ? significa OPCIONAL
}

// Ahora TypeScript valida que cumplas la estructura:
const laptop: Producto = {
  id: 1,
  nombre: "Laptop HP",
  precio: 18999,
  existencia: 25,
  categoriaId: 1,
  activo: true
};

// ❌ Error: falta 'precio'
const malo: Producto = {
  id: 2,
  nombre: "Mouse"
};

// Interface para la respuesta de la API
interface ApiResponse<T> {
  success: boolean;
  data: T;
  total?: number;
  message?: string;
}

// Uso:
const respuesta: ApiResponse<Producto[]> = {
  success: true,
  data: [laptop],
  total: 1
};
```

### ¿Para qué las interfaces?
- **Autocompletado**: el editor te sugiere campos
- **Errores en tiempo real**: te avisa si falta algo
- **Documentación**: sabes qué esperar de una API
- **Angular las usa en todo**: servicios, componentes, modelos

---

## 4. Funciones Tipadas

```typescript
// Función con tipos de parámetros y retorno
function sumar(a: number, b: number): number {
  return a + b;
}

// Arrow function (la más usada en Angular)
const multiplicar = (a: number, b: number): number => a * b;

// Función que no retorna nada
function saludar(nombre: string): void {
  console.log(`Hola ${nombre}`);
}

// Parámetros opcionales
function buscar(nombre: string, activo?: boolean): Producto[] {
  // activo puede ser undefined
}

// Parámetros con valor por defecto
function paginar(page: number = 0, size: number = 10): void {
  // si no pasas page, es 0
}

// Función que puede fallar
function buscarPorId(id: number): Producto | null {
  // retorna Producto o null si no existe
  return null;
}
```

---

## 5. Clases

```typescript
// Clase con constructor y métodos
class ProductoService {
  private productos: Producto[] = [];

  constructor(private nombre: string) {}

  agregar(producto: Producto): void {
    this.productos.push(producto);
  }

  buscarPorId(id: number): Producto | undefined {
    return this.productos.find(p => p.id === id);
  }

  listarActivos(): Producto[] {
    return this.productos.filter(p => p.activo);
  }

  get total(): number {
    return this.productos.length;
  }
}

// Uso:
const service = new ProductoService("Mi servicio");
service.agregar(laptop);
console.log(service.total);  // 1
```

### Modificadores de acceso
| Modificador | Acceso |
|-------------|--------|
| `public` | Desde cualquier lado (por defecto) |
| `private` | Solo dentro de la clase |
| `protected` | Dentro de la clase y sus hijos |
| `readonly` | No se puede modificar después |

---

## 6. Generics (tipos genéricos)

```typescript
// Sin generics: no sabes qué tipo es la data
interface Respuesta {
  success: boolean;
  data: any;  // ← "any" pierde el tipado
}

// Con generics: el tipo se define al usar
interface Respuesta<T> {
  success: boolean;
  data: T;
}

// Ahora puedes tipar la respuesta:
const resp1: Respuesta<Producto> = { success: true, data: laptop };
const resp2: Respuesta<string[]> = { success: true, data: ["a", "b"] };

// TypeScript sabe que resp1.data es Producto
resp1.data.nombre;  // ✅ autocompletado

// Angular HttpClient usa generics:
this.http.get<Producto[]>('/api/productos');  // sabe que retorna Producto[]
```

---

## 7. Módulos (import / export)

```typescript
// ═══ producto.model.ts ═══
export interface Producto {
  id: number;
  nombre: string;
  precio: number;
}

// ═══ producto.service.ts ═══
import { Producto } from './producto.model';

export class ProductoService {
  listar(): Producto[] {
    return [];
  }
}

// ═══ producto.component.ts ═══
import { ProductoService } from './producto.service';
import { Producto } from './producto.model';
```

### Reglas de imports en Angular
- Un archivo = una responsabilidad
- Cada clase/interface/función se exporta con `export`
- Se importa con `import { Nombre } from './ruta'`
- Las rutas son relativas (`./`, `../`) o de paquetes (`@angular/core`)

---

## 8. Async/Await y Promises

```typescript
// Una Promise es una operación que TARDARÁ en completarse
// (como una petición HTTP a la API)

// Con Promises (estilo antiguo):
function obtenerProductos(): Promise<Producto[]> {
  return fetch('/api/productos')
    .then(response => response.json())
    .then(data => data as Producto[]);
}

// Con async/await (estilo moderno, más legible):
async function obtenerProductos(): Promise<Producto[]> {
  const response = await fetch('/api/productos');
  const data = await response.json();
  return data as Producto[];
}

// Manejar errores:
async function obtener() {
  try {
    const productos = await obtenerProductos();
    console.log(productos);
  } catch (error) {
    console.error("Error:", error);
  }
}
```

### En Angular se usan Observables (no Promises)
```typescript
// Angular usa RxJS Observables en vez de Promises:
this.http.get<Producto[]>('/api/productos').subscribe({
  next: (productos) => console.log(productos),
  error: (err) => console.error(err)
});

// Es parecido pero con subscribe en vez de await
// Se explica en detalle en el Nivel 9
```

---

## 9. Destructuring (desestructuración)

```typescript
// Extraer propiedades de un objeto
const producto = { id: 1, nombre: "Laptop", precio: 18999 };
const { nombre, precio } = producto;
console.log(nombre);  // "Laptop"
console.log(precio);  // 18999

// Extraer de un array
const [primero, segundo, ...resto] = [1, 2, 3, 4, 5];
console.log(primero);  // 1
console.log(resto);    // [3, 4, 5]

// En funciones (muy usado en Angular)
function mostrar({ nombre, precio }: Producto): void {
  console.log(`${nombre}: $${precio}`);
}
```

---

## 10. Spread Operator (...) 

```typescript
// Copiar un array
const original = [1, 2, 3];
const copia = [...original];          // [1, 2, 3]
const agregado = [...original, 4, 5]; // [1, 2, 3, 4, 5]

// Copiar un objeto (y modificar una propiedad)
const producto = { id: 1, nombre: "Laptop", precio: 18999 };
const actualizado = { ...producto, precio: 19999 };
// { id: 1, nombre: "Laptop", precio: 19999 }

// MUY usado en Angular con signals:
this.productos.update(list => [...list, nuevoProducto]);
// Crea una NUEVA lista con todos los anteriores + el nuevo
```

---

## 11. Template Literals (template strings)

```typescript
const nombre = "Carlos";
const edad = 25;

// Concatenación antigua:
const mensaje = "Hola " + nombre + ", tienes " + edad + " años";

// Template literal (backticks `):
const mensaje = `Hola ${nombre}, tienes ${edad} años`;

// Multi-línea:
const html = `
  <div>
    <h1>${nombre}</h1>
    <p>Edad: ${edad}</p>
  </div>
`;
```

---

## 12. Enums (valores constantes)

```typescript
// Enum: un conjunto fijo de opciones
enum EstadoPedido {
  PENDIENTE = 'PENDIENTE',
  PROCESANDO = 'PROCESANDO',
  ENVIADO = 'ENVIADO',
  ENTREGADO = 'ENTREGADO',
  CANCELADO = 'CANCELADO'
}

// Uso:
let estado: EstadoPedido = EstadoPedido.PENDIENTE;

// En una función:
function puedesCancelar(estado: EstadoPedido): boolean {
  return estado === EstadoPedido.PENDIENTE || estado === EstadoPedido.PROCESANDO;
}
```

---

## 13. Type vs Interface

```typescript
// Ambos definen la forma de un objeto. ¿Cuál usar?

// Interface (preferida en Angular para modelos de datos):
interface Producto {
  id: number;
  nombre: string;
}

// Type (para uniones, intersecciones, alias):
type ID = number | string;
type Estado = 'activo' | 'inactivo' | 'pendiente';
type ProductoConCategoria = Producto & { categoriaNombre: string };
```

### Regla práctica
- **Interface** → para objetos/modelos (Producto, Usuario, ApiResponse)
- **Type** → para uniones, alias, o tipos complejos

---

## Ejercicios para practicar ANTES del Nivel 8

### Ejercicio 1: Interface + array
Define una interface `Tarea` (id, titulo, completada). Crea un array de 5 tareas. Filtra las pendientes. Cuenta las completadas.

### Ejercicio 2: Función genérica
Crea una función `primero<T>(lista: T[]): T | undefined` que devuelva el primer elemento de cualquier array.

### Ejercicio 3: Clase servicio
Crea una clase `TareaService` con métodos: agregar, listar, completar(id), eliminar(id). Usa un array interno como "base de datos".

### Ejercicio 4: Async
Simula una función `fetchProductos()` que retorne una Promise que se resuelve después de 1 segundo con un array de productos.

---

## Siguiente paso
Cuando domines interfaces, funciones tipadas y arrays → ve al **Nivel 8: Angular** con confianza.
