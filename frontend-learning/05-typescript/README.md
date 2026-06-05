# Módulo 05: TypeScript - JavaScript con Tipos

## 1. Tipos Básicos

```typescript
// Primitivos
const nombre: string = 'Juan';
const edad: number = 30;
const activo: boolean = true;

// Arrays
const numeros: number[] = [1, 2, 3];
const nombres: Array<string> = ['Ana', 'Luis'];

// Union types
let id: string | number = 'abc-123';
id = 456;  // ✅ también válido

// Literal types
type Rol = 'admin' | 'user' | 'viewer';
const miRol: Rol = 'admin';
```

## 2. Interfaces vs Type Aliases

```typescript
// Interface → para objetos y clases (extensible)
interface Usuario {
  id: number;
  nombre: string;
  email: string;
  rol: Rol;
  activo?: boolean;  // opcional
}

interface Admin extends Usuario {
  permisos: string[];
}

// Type → para uniones, intersecciones, utilidades
type Respuesta<T> = {
  data: T;
  mensaje: string;
  exitoso: boolean;
};

type UsuarioCrear = Omit<Usuario, 'id'>;
type UsuarioActualizar = Partial<Pick<Usuario, 'nombre' | 'email'>>;
```

## 3. Enums y Clases

```typescript
enum EstadoPedido {
  PENDIENTE = 'PENDIENTE',
  PROCESANDO = 'PROCESANDO',
  ENVIADO = 'ENVIADO',
  ENTREGADO = 'ENTREGADO',
}

class ProductoService {
  private productos: Producto[] = [];

  constructor(private readonly apiUrl: string) {}

  agregar(producto: Producto): void {
    this.productos.push(producto);
  }

  buscar(id: number): Producto | undefined {
    return this.productos.find(p => p.id === id);
  }
}
```

## 4. Generics

```typescript
// Función genérica
function primero<T>(array: T[]): T | undefined {
  return array[0];
}

// Interface genérica (patrón API Response)
interface ApiResponse<T> {
  data: T;
  total: number;
  page: number;
  pageSize: number;
}

// Constraint
function getProperty<T, K extends keyof T>(obj: T, key: K): T[K] {
  return obj[key];
}

const usuario: Usuario = { id: 1, nombre: 'Ana', email: 'ana@mail.com', rol: 'admin' };
const nombre = getProperty(usuario, 'nombre'); // tipo: string
```

## 5. Comparación JS vs TS

```javascript
// JavaScript - sin seguridad de tipos
function calcularTotal(productos) {
  return productos.reduce((sum, p) => sum + p.precio * p.cantidad, 0);
}
// ¿Qué pasa si p.precio es undefined? → NaN silencioso
```

```typescript
// TypeScript - errores en compilación
interface LineaPedido {
  productoId: number;
  precio: number;
  cantidad: number;
}

function calcularTotal(productos: LineaPedido[]): number {
  return productos.reduce((sum, p) => sum + p.precio * p.cantidad, 0);
}
// Si falta precio → Error en compilación, no en producción
```

## 6. Ejercicios

1. Define interfaces para un sistema de e-commerce: Producto, Carrito, Pedido, Usuario.
2. Crea un tipo genérico `PaginatedResponse<T>` y úsalo con diferentes entidades.
3. Implementa una clase `Repository<T>` con métodos CRUD tipados.
4. Usa utility types (Partial, Pick, Omit, Record) para crear variantes de una interface.
5. Crea un enum para estados de un pedido y una función que valide transiciones válidas.

---

## Siguiente Módulo
→ [06-DOM](../06-dom/README.md)
