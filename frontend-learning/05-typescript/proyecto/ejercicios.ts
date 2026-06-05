/**
 * MÓDULO 05: TypeScript - Ejercicios Prácticos
 * Ejecutar: ts-node ejercicios.ts
 */

console.log('═══════════════════════════════════════');
console.log('  MÓDULO 05: TypeScript');
console.log('═══════════════════════════════════════\n');

// ═══════ 1. TIPOS BÁSICOS ═══════
console.log('── 1. Tipos Básicos ──');

let nombre: string = 'Carlos';
let edad: number = 30;
let activo: boolean = true;
let datos: any = 'puede ser cualquier cosa'; // EVITAR en lo posible

// Arrays
let numeros: number[] = [1, 2, 3, 4, 5];
let nombres: Array<string> = ['Ana', 'Luis', 'María'];

// Tuple (array con tipos fijos por posición)
let coordenada: [number, number] = [19.4326, -99.1332];

// Union types (puede ser uno u otro)
let id: string | number = 'ABC-123';
id = 42; // También válido

console.log(`${nombre}, ${edad} años, activo: ${activo}`);

// ═══════ 2. INTERFACES (contratos de estructura) ═══════
console.log('\n── 2. Interfaces ──');

interface Producto {
    id: number;
    nombre: string;
    precio: number;
    stock: number;
    categoria?: string;  // ? = opcional
    readonly sku: string; // readonly = no se puede modificar después
}

const laptop: Producto = {
    id: 1,
    nombre: 'Laptop HP',
    precio: 18999.99,
    stock: 25,
    sku: 'HP-001'
};

// laptop.sku = 'OTRO'; // ERROR: Cannot assign to 'sku' because it is a read-only property
console.log('Producto:', laptop);

// Interface para funciones
interface CalculadoraDescuento {
    (precio: number, porcentaje: number): number;
}

const aplicarDescuento: CalculadoraDescuento = (precio, porcentaje) => {
    return precio * (1 - porcentaje / 100);
};

console.log('Con 20% descuento:', aplicarDescuento(1000, 20));

// ═══════ 3. TYPE ALIAS ═══════
console.log('\n── 3. Type Alias ──');

type EstadoVenta = 'PENDIENTE' | 'COMPLETADA' | 'CANCELADA'; // Literal types
type ID = string | number;

interface Venta {
    id: ID;
    estado: EstadoVenta;
    total: number;
}

const venta: Venta = { id: 'V-001', estado: 'COMPLETADA', total: 5000 };
// venta.estado = 'INVALIDO'; // ERROR: no es un valor permitido
console.log('Venta:', venta);

// ═══════ 4. GENERICS (tipos parametrizados) ═══════
console.log('\n── 4. Generics ──');

// Función genérica: funciona con CUALQUIER tipo
function primero<T>(array: T[]): T | undefined {
    return array[0];
}

console.log('Primer número:', primero([10, 20, 30]));     // T = number
console.log('Primer string:', primero(['hola', 'mundo'])); // T = string

// Interface genérica (patrón MUY usado en Angular para respuestas de API)
interface ApiResponse<T> {
    success: boolean;
    message: string;
    data: T;
    timestamp: string;
}

const respuesta: ApiResponse<Producto[]> = {
    success: true,
    message: 'OK',
    data: [laptop],
    timestamp: new Date().toISOString()
};

console.log('API Response:', respuesta.data.length, 'productos');

// ═══════ 5. CLASES con TypeScript ═══════
console.log('\n── 5. Clases ──');

class ServicioProductos {
    // private: solo accesible dentro de la clase
    private productos: Producto[] = [];

    // constructor con parameter properties (shorthand)
    constructor(private readonly apiUrl: string) {}

    agregar(producto: Producto): void {
        this.productos.push(producto);
    }

    buscar(id: number): Producto | undefined {
        return this.productos.find(p => p.id === id);
    }

    listar(): Producto[] {
        return [...this.productos]; // Copia (no exponer el array interno)
    }

    get total(): number {
        return this.productos.length;
    }
}

const servicio = new ServicioProductos('http://localhost:8080/api');
servicio.agregar(laptop);
servicio.agregar({ id: 2, nombre: 'Mouse', precio: 899, stock: 50, sku: 'MS-001' });
console.log('Total productos:', servicio.total);
console.log('Buscar id=1:', servicio.buscar(1)?.nombre);

// ═══════ 6. UTILITY TYPES (los más usados) ═══════
console.log('\n── 6. Utility Types ──');

// Partial<T>: todos los campos opcionales
type ActualizarProducto = Partial<Producto>;
const update: ActualizarProducto = { precio: 19999 }; // Solo precio

// Pick<T, K>: solo ciertos campos
type ProductoResumen = Pick<Producto, 'id' | 'nombre' | 'precio'>;
const resumen: ProductoResumen = { id: 1, nombre: 'Laptop', precio: 18999 };

// Omit<T, K>: todos excepto ciertos campos
type CrearProducto = Omit<Producto, 'id'>; // Sin id (lo genera el backend)

// Record<K, V>: objeto con claves K y valores V
type StockPorSucursal = Record<string, number>;
const stock: StockPorSucursal = { 'CDMX': 25, 'MTY': 15, 'GDL': 10 };

console.log('Partial:', update);
console.log('Pick:', resumen);
console.log('Record:', stock);

// ═══════ 7. ENUMS ═══════
console.log('\n── 7. Enums ──');

enum Rol {
    ADMIN = 'ADMIN',
    GERENTE = 'GERENTE',
    VENDEDOR = 'VENDEDOR',
}

function tienePermiso(rol: Rol, accion: string): boolean {
    if (rol === Rol.ADMIN) return true;
    if (rol === Rol.GERENTE && accion !== 'ELIMINAR_USUARIO') return true;
    return false;
}

console.log('Admin puede todo:', tienePermiso(Rol.ADMIN, 'ELIMINAR_USUARIO'));
console.log('Gerente puede eliminar:', tienePermiso(Rol.GERENTE, 'ELIMINAR_USUARIO'));

console.log('\n═══════════════════════════════════════');
console.log('  FIN - Ejecuta: ts-node ejercicios.ts');
console.log('═══════════════════════════════════════');
