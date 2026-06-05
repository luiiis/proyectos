# Frontend Completo - Parte 2: TypeScript al 100%

---

# 1. QUE ES TYPESCRIPT

TypeScript = JavaScript + TIPOS.
Detecta errores en COMPILACION (antes de ejecutar), no en runtime.

```
JavaScript: let precio = "hola"; precio * 2; → NaN (error silencioso en runtime)
TypeScript: let precio: number = "hola"; → ERROR en compilacion (no llega a ejecutarse)
```

Angular esta ESCRITO en TypeScript. Es OBLIGATORIO aprenderlo.

---

# 2. TIPOS BASICOS

```typescript
// Tipos primitivos
let nombre: string = "Carlos";
let edad: number = 30;
let activo: boolean = true;
let nulo: null = null;
let indefinido: undefined = undefined;

// Arrays
let numeros: number[] = [1, 2, 3, 4, 5];
let nombres: string[] = ["Ana", "Luis"];
let mixto: (string | number)[] = ["hola", 42];

// Tuple (array con tipos fijos por posicion)
let coordenada: [number, number] = [19.43, -99.13];
let registro: [number, string, boolean] = [1, "Carlos", true];

// any: EVITAR (desactiva el type checking)
let datos: any = "puede ser cualquier cosa";

// unknown: mas seguro que any (obliga a verificar tipo antes de usar)
let valor: unknown = obtenerDato();
if (typeof valor === "string") {
    console.log(valor.toUpperCase());  // OK: TypeScript sabe que es string aqui
}

// void: funcion que no retorna nada
function log(mensaje: string): void {
    console.log(mensaje);
}

// never: funcion que NUNCA retorna (lanza error o loop infinito)
function error(msg: string): never {
    throw new Error(msg);
}
```

---

# 3. INTERFACES (contratos de estructura)

```typescript
// Interface: define la FORMA que debe tener un objeto
interface Producto {
    id: number;
    nombre: string;
    precio: number;
    stock: number;
    descripcion?: string;    // ? = OPCIONAL (puede no existir)
    readonly sku: string;    // readonly = no se puede modificar despues
}

// Uso: TypeScript VERIFICA que el objeto cumpla la interface
const laptop: Producto = {
    id: 1,
    nombre: "Laptop HP",
    precio: 18999.99,
    stock: 25,
    sku: "LAP-001"
    // descripcion es opcional, no es obligatorio
};

// laptop.sku = "OTRO";  // ERROR: readonly

// Interface para funciones
interface CalculadoraDescuento {
    (precio: number, porcentaje: number): number;
}
const aplicar: CalculadoraDescuento = (precio, pct) => precio * (1 - pct / 100);

// Interface que extiende otra
interface ProductoConCategoria extends Producto {
    categoria: string;
    proveedor: string;
}

// Interface para respuesta de API
interface ApiResponse<T> {
    success: boolean;
    message: string;
    data: T;
    timestamp: string;
}
// Uso: ApiResponse<Producto[]> o ApiResponse<Producto>
```

---

# 4. TYPE ALIAS

```typescript
// Type alias: nombre para un tipo (similar a interface pero mas flexible)

// Union types (puede ser uno U otro)
type ID = string | number;
type Estado = "ACTIVO" | "INACTIVO" | "PENDIENTE";  // Literal types

// Intersection types (debe ser AMBOS)
type ConTimestamp = Producto & { createdAt: string; updatedAt: string };

// Utility types (transformar tipos existentes)
type ProductoParcial = Partial<Producto>;           // Todos los campos opcionales
type ProductoRequerido = Required<Producto>;        // Todos obligatorios
type ProductoResumen = Pick<Producto, "id" | "nombre" | "precio">;  // Solo estos
type SinId = Omit<Producto, "id">;                 // Todos excepto id
type Catalogo = Record<string, Producto>;          // {[key: string]: Producto}

// Ejemplo real: DTO para crear (sin id, sin readonly)
type CrearProducto = Omit<Producto, "id" | "sku"> & { sku?: string };
```

## Interface vs Type: cuando usar cada uno

```typescript
// INTERFACE: para definir la forma de OBJETOS y CLASES
// - Se puede extender (extends)
// - Se puede implementar (implements)
// - Se puede "mergear" (declaration merging)
interface Animal { nombre: string; }
interface Perro extends Animal { raza: string; }

// TYPE: para CUALQUIER tipo (unions, intersections, primitivos, tuplas)
// - Mas flexible
// - No se puede mergear
type Resultado = "exito" | "error";
type Par = [string, number];
type Callback = (data: Producto) => void;

// REGLA PRACTICA:
// - Objetos/clases → interface
// - Todo lo demas → type
```

---

# 5. GENERICS (tipos parametrizados)

```typescript
// Problema: quieres una funcion que funcione con CUALQUIER tipo
// pero manteniendo type safety

// Sin generics: pierdes el tipo
function primero(array: any[]): any {
    return array[0];  // Retorna "any" (no sabes que tipo es)
}

// Con generics: mantiene el tipo
function primero<T>(array: T[]): T | undefined {
    return array[0];
}
primero([1, 2, 3]);        // TypeScript sabe: retorna number
primero(["a", "b", "c"]); // TypeScript sabe: retorna string

// Clase generica (patron MUY usado en Angular para respuestas de API)
interface ApiResponse<T> {
    success: boolean;
    message: string;
    data: T;
}

// Uso:
const respProductos: ApiResponse<Producto[]> = await fetch("/api/productos").then(r => r.json());
const respProducto: ApiResponse<Producto> = await fetch("/api/productos/1").then(r => r.json());
// TypeScript sabe que respProductos.data es Producto[]
// TypeScript sabe que respProducto.data es Producto

// Generics con constraints (limitar que tipos se aceptan)
function maximo<T extends { precio: number }>(items: T[]): T {
    return items.reduce((max, item) => item.precio > max.precio ? item : max);
}
// Solo acepta objetos que tengan campo "precio"
```

---

# 6. ENUMS

```typescript
// String enum (recomendado)
enum Rol {
    ADMIN = "ADMIN",
    GERENTE = "GERENTE",
    VENDEDOR = "VENDEDOR",
    USER = "USER"
}

// Uso
function tienePermiso(rol: Rol, accion: string): boolean {
    if (rol === Rol.ADMIN) return true;
    if (rol === Rol.GERENTE && accion !== "ELIMINAR_USUARIO") return true;
    return false;
}

// Numeric enum (menos recomendado, puede causar confusion)
enum HttpStatus {
    OK = 200,
    CREATED = 201,
    BAD_REQUEST = 400,
    UNAUTHORIZED = 401,
    NOT_FOUND = 404,
    SERVER_ERROR = 500
}

// ALTERNATIVA moderna (const object + type):
const ROLES = {
    ADMIN: "ADMIN",
    GERENTE: "GERENTE",
    VENDEDOR: "VENDEDOR",
} as const;
type Rol = typeof ROLES[keyof typeof ROLES];  // "ADMIN" | "GERENTE" | "VENDEDOR"
```

---

# 7. CLASES EN TYPESCRIPT

```typescript
class ProductoService {
    // Modificadores de acceso
    private readonly apiUrl: string;  // private: solo dentro de la clase
    protected cache: Map<number, Producto> = new Map();  // protected: clase + hijas

    // Constructor con parameter properties (shorthand)
    constructor(
        private http: HttpClient,  // Crea campo + asigna automaticamente
        private baseUrl: string = "/api/productos"
    ) {
        this.apiUrl = baseUrl;
    }

    // Metodo publico
    async listar(): Promise<Producto[]> {
        const response = await this.http.get<Producto[]>(this.apiUrl);
        return response;
    }

    // Metodo privado (solo uso interno)
    private validar(producto: Producto): boolean {
        return producto.precio > 0 && producto.nombre.length > 0;
    }

    // Getter
    get url(): string {
        return this.apiUrl;
    }
}

// Abstract class
abstract class BaseService<T> {
    abstract getAll(): Promise<T[]>;
    abstract getById(id: number): Promise<T>;
    abstract create(item: T): Promise<T>;
    abstract delete(id: number): Promise<void>;
}
```

---

# 8. DECORATORS (usados en Angular)

```typescript
// Los decorators son FUNCIONES que modifican clases/metodos/propiedades
// Angular los usa EXTENSIVAMENTE

// @Component: marca una clase como componente Angular
@Component({
    selector: 'app-producto',
    template: `<h1>{{nombre}}</h1>`,
    styles: [`h1 { color: blue; }`]
})
class ProductoComponent { }

// @Injectable: marca una clase como servicio inyectable
@Injectable({ providedIn: 'root' })
class ProductoService { }

// @Input/@Output: comunicacion entre componentes
class HijoComponent {
    @Input() datos!: Producto;
    @Output() guardar = new EventEmitter<Producto>();
}

// Crear tu propio decorator (avanzado)
function Log(target: any, key: string, descriptor: PropertyDescriptor) {
    const original = descriptor.value;
    descriptor.value = function(...args: any[]) {
        console.log(`Llamando ${key} con:`, args);
        const result = original.apply(this, args);
        console.log(`${key} retorno:`, result);
        return result;
    };
}

class MiServicio {
    @Log  // Cada vez que se llame, se logea automaticamente
    calcular(a: number, b: number): number {
        return a + b;
    }
}
```

---

# 9. PATRONES COMUNES EN ANGULAR

```typescript
// Modelo/Interface para datos de la API
export interface Producto {
    id: number;
    nombre: string;
    precio: number;
    stock: number;
    categoria: string;
    activo: boolean;
    createdAt: string;
}

// DTO para crear (sin campos que genera el backend)
export type CrearProducto = Omit<Producto, 'id' | 'createdAt' | 'activo'>;

// DTO para actualizar (todos opcionales)
export type ActualizarProducto = Partial<CrearProducto>;

// Respuesta paginada del backend
export interface Page<T> {
    content: T[];
    totalElements: number;
    totalPages: number;
    number: number;
    size: number;
    first: boolean;
    last: boolean;
}

// Servicio tipado
@Injectable({ providedIn: 'root' })
export class ProductoService {
    private http = inject(HttpClient);
    private url = '/api/productos';

    listar(page = 0, size = 20): Observable<Page<Producto>> {
        return this.http.get<Page<Producto>>(`${this.url}?page=${page}&size=${size}`);
    }

    buscar(id: number): Observable<Producto> {
        return this.http.get<Producto>(`${this.url}/${id}`);
    }

    crear(producto: CrearProducto): Observable<Producto> {
        return this.http.post<Producto>(this.url, producto);
    }

    actualizar(id: number, datos: ActualizarProducto): Observable<Producto> {
        return this.http.put<Producto>(`${this.url}/${id}`, datos);
    }

    eliminar(id: number): Observable<void> {
        return this.http.delete<void>(`${this.url}/${id}`);
    }
}
```

---

# 10. CONFIGURACION TYPESCRIPT (tsconfig.json)

```json
{
  "compilerOptions": {
    "target": "ES2022",           // A que version de JS compilar
    "module": "ES2022",           // Sistema de modulos
    "strict": true,               // ACTIVAR SIEMPRE (detecta mas errores)
    "noImplicitAny": true,        // No permitir "any" implicito
    "strictNullChecks": true,     // null/undefined son tipos separados
    "noUnusedLocals": true,       // Error si hay variables sin usar
    "noUnusedParameters": true,   // Error si hay parametros sin usar
    "sourceMap": true,            // Generar .map para debugging
    "declaration": true,          // Generar .d.ts (tipos)
    "outDir": "./dist",           // Donde poner el JS compilado
    "baseUrl": "./",
    "paths": {                    // Aliases para imports
      "@app/*": ["src/app/*"],
      "@env/*": ["src/environments/*"]
    }
  }
}
```

---

# 11. EJERCICIOS

1. Define interfaces para: Usuario, Rol, Permiso con sus relaciones
2. Crea un tipo generico `Result<T, E>` que sea `{ok: true, data: T}` o `{ok: false, error: E}`
3. Implementa una clase `Cache<K, V>` generica con get, set, delete, has, clear
4. Crea un servicio tipado para CRUD de clientes usando los patrones de Angular
5. Usa Utility Types para crear: `CrearUsuario`, `ActualizarUsuario`, `UsuarioResumen`
