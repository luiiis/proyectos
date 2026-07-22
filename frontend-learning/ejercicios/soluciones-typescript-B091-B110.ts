/**
 * SOLUCIONES - Ejercicios TypeScript B091-B110
 * Ejecutar: npx ts-node soluciones-typescript-B091-B110.ts
 * O: npx tsx soluciones-typescript-B091-B110.ts
 */

console.log('═══ SOLUCIONES TypeScript B091-B110 ═══\n');

// B091: Interfaces básicas
console.log('── B091: Interfaces ──');
interface Producto {
  id: number;
  nombre: string;
  precio: number;
  stock: number;
  categoria: string;
  activo: boolean;
}

interface ProductoCreate extends Omit<Producto, 'id'> {}
interface ProductoUpdate extends Partial<Omit<Producto, 'id'>> {}

const laptop: Producto = { id: 1, nombre: 'Laptop', precio: 18999, stock: 25, categoria: 'Electrónica', activo: true };
console.log('Producto:', laptop.nombre, '$' + laptop.precio);

// B092: Enums
console.log('\n── B092: Enums ──');
enum EstadoPedido {
  PENDIENTE = 'PENDIENTE',
  PROCESANDO = 'PROCESANDO',
  ENVIADO = 'ENVIADO',
  ENTREGADO = 'ENTREGADO',
  CANCELADO = 'CANCELADO'
}

enum Prioridad {
  BAJA = 1,
  MEDIA = 2,
  ALTA = 3,
  CRITICA = 4
}

const estado: EstadoPedido = EstadoPedido.PROCESANDO;
console.log('Estado:', estado);
console.log('Prioridad ALTA:', Prioridad.ALTA);

// B093: Union Types y Type Guards
console.log('\n── B093: Union Types ──');
type ID = string | number;
type Respuesta<T> = { success: true; data: T } | { success: false; error: string };

function procesarId(id: ID): string {
  if (typeof id === 'string') return `String ID: ${id.toUpperCase()}`;
  return `Numeric ID: ${id.toFixed(0)}`;
}
console.log(procesarId('abc-123'));
console.log(procesarId(42));

function handleResponse(res: Respuesta<Producto>): void {
  if (res.success) {
    console.log('Éxito:', res.data.nombre);
  } else {
    console.log('Error:', res.error);
  }
}
handleResponse({ success: true, data: laptop });
handleResponse({ success: false, error: 'No encontrado' });

// B094: Generics
console.log('\n── B094: Generics ──');
interface ApiResponse<T> {
  data: T;
  status: number;
  timestamp: string;
  pagination?: { page: number; total: number; pageSize: number };
}

function crearRespuesta<T>(data: T, status: number = 200): ApiResponse<T> {
  return { data, status, timestamp: new Date().toISOString() };
}

const res1: ApiResponse<Producto[]> = crearRespuesta([laptop]);
const res2: ApiResponse<string> = crearRespuesta('OK');
console.log('Response productos:', res1.status, '| items:', res1.data.length);
console.log('Response string:', res2.data);

// B095: Generic constraints
console.log('\n── B095: Generic Constraints ──');
interface HasId { id: number; }
interface HasNombre { nombre: string; }

function buscarPorId<T extends HasId>(items: T[], id: number): T | undefined {
  return items.find(item => item.id === id);
}

function filtrarPorNombre<T extends HasNombre>(items: T[], term: string): T[] {
  return items.filter(item => item.nombre.toLowerCase().includes(term.toLowerCase()));
}

const productos: Producto[] = [
  laptop,
  { id: 2, nombre: 'Mouse MX', precio: 1899, stock: 50, categoria: 'Periféricos', activo: true },
  { id: 3, nombre: 'Monitor Dell', precio: 12499, stock: 15, categoria: 'Electrónica', activo: true },
];

console.log('Buscar id=2:', buscarPorId(productos, 2)?.nombre);
console.log('Filtrar "M":', filtrarPorNombre(productos, 'M').map(p => p.nombre));

// B096: Mapped Types
console.log('\n── B096: Mapped Types ──');
type Readonly<T> = { readonly [K in keyof T]: T[K] };
type Nullable<T> = { [K in keyof T]: T[K] | null };
type FormFields<T> = { [K in keyof T]: string };  // Todo como string para formularios

type ProductoForm = FormFields<Omit<Producto, 'id' | 'activo'>>;
const form: ProductoForm = { nombre: 'Laptop', precio: '18999', stock: '25', categoria: 'Electrónica' };
console.log('Form:', form);

// B097: Utility Types
console.log('\n── B097: Utility Types ──');
type ProductoResumen = Pick<Producto, 'id' | 'nombre' | 'precio'>;
type ProductoSinId = Omit<Producto, 'id'>;
type ProductoParcial = Partial<Producto>;
type ProductoRequerido = Required<Producto>;

const resumen: ProductoResumen = { id: 1, nombre: 'Laptop', precio: 18999 };
console.log('Resumen:', resumen);

// Record
type CategoriaStock = Record<string, number>;
const stockPorCategoria: CategoriaStock = { 'Electrónica': 52, 'Periféricos': 110, 'Mobiliario': 13 };
console.log('Stock:', stockPorCategoria);

// B098: Discriminated Unions (patrón muy usado en Angular/Redux)
console.log('\n── B098: Discriminated Unions ──');
type Accion =
  | { type: 'CARGAR_PRODUCTOS'; payload?: undefined }
  | { type: 'PRODUCTOS_CARGADOS'; payload: Producto[] }
  | { type: 'ERROR'; payload: string }
  | { type: 'AGREGAR_PRODUCTO'; payload: Producto };

function reducer(estado: Producto[], accion: Accion): Producto[] {
  switch (accion.type) {
    case 'PRODUCTOS_CARGADOS': return accion.payload;
    case 'AGREGAR_PRODUCTO': return [...estado, accion.payload];
    case 'ERROR': console.log('Error:', accion.payload); return estado;
    default: return estado;
  }
}

let state = reducer([], { type: 'PRODUCTOS_CARGADOS', payload: productos });
console.log('Estado después de cargar:', state.length, 'productos');

// B099: Type Narrowing avanzado
console.log('\n── B099: Type Narrowing ──');
interface Perro { tipo: 'perro'; ladrar(): string; }
interface Gato { tipo: 'gato'; maullar(): string; }
type Animal = Perro | Gato;

function hacerSonido(animal: Animal): string {
  switch (animal.tipo) {
    case 'perro': return animal.ladrar();
    case 'gato': return animal.maullar();
  }
}

const rex: Perro = { tipo: 'perro', ladrar: () => 'Guau!' };
const michi: Gato = { tipo: 'gato', maullar: () => 'Miau!' };
console.log(hacerSonido(rex), hacerSonido(michi));

// B100: Class con TypeScript
console.log('\n── B100: Classes ──');
class Repositorio<T extends HasId> {
  private items: T[] = [];

  agregar(item: T): T {
    this.items.push(item);
    return item;
  }

  buscarPorId(id: number): T | undefined {
    return this.items.find(i => i.id === id);
  }

  listar(): T[] {
    return [...this.items]; // Copia defensiva
  }

  eliminar(id: number): boolean {
    const idx = this.items.findIndex(i => i.id === id);
    if (idx === -1) return false;
    this.items.splice(idx, 1);
    return true;
  }

  count(): number { return this.items.length; }
}

const repo = new Repositorio<Producto>();
repo.agregar(laptop);
repo.agregar({ id: 2, nombre: 'Mouse', precio: 899, stock: 50, categoria: 'Periféricos', activo: true });
console.log('Repo count:', repo.count());
console.log('Buscar id=1:', repo.buscarPorId(1)?.nombre);
console.log('Eliminar id=2:', repo.eliminar(2));
console.log('Repo count after delete:', repo.count());

// B101-B110: Angular-specific TypeScript patterns
console.log('\n── B101-B110: Angular Patterns ──');

// B101: Service-like class
class ProductoService {
  private productos: Producto[] = [];

  cargar(): Promise<Producto[]> {
    // Simulación de HTTP call
    return new Promise(resolve => {
      setTimeout(() => {
        this.productos = productos;
        resolve(this.productos);
      }, 100);
    });
  }

  filtrar(categoria?: string, precioMax?: number): Producto[] {
    return this.productos.filter(p =>
      (!categoria || p.categoria === categoria) &&
      (!precioMax || p.precio <= precioMax)
    );
  }
}

// B102: Observable-like pattern (simplificado)
type Subscriber<T> = (value: T) => void;

class SimpleObservable<T> {
  private subscribers: Subscriber<T>[] = [];

  subscribe(fn: Subscriber<T>): { unsubscribe: () => void } {
    this.subscribers.push(fn);
    return { unsubscribe: () => { this.subscribers = this.subscribers.filter(s => s !== fn); } };
  }

  next(value: T): void {
    this.subscribers.forEach(fn => fn(value));
  }
}

const obs = new SimpleObservable<string>();
const sub = obs.subscribe(v => console.log('  Recibido:', v));
obs.next('Hola');
obs.next('Mundo');
sub.unsubscribe();
obs.next('No se ve'); // Nadie escucha

console.log('\n═══ FIN ═══');
