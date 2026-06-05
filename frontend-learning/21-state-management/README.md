# Módulo 21: State Management

## 1. Signals (Angular 17+ - Preferir)

```typescript
// signal: estado reactivo mutable
const contador = signal(0);
contador.set(5);           // establecer valor
contador.update(v => v + 1); // actualizar basado en anterior

// computed: derivado, se recalcula automáticamente
const doble = computed(() => contador() * 2);

// effect: side effect cuando cambian signals
effect(() => {
  console.log(`Contador cambió a: ${contador()}`);
  localStorage.setItem('contador', contador().toString());
});
```

## 2. Signals en Servicios (State Service Pattern)

```typescript
@Injectable({ providedIn: 'root' })
export class CarritoStateService {
  // Estado privado
  private items = signal<ItemCarrito[]>([]);

  // Estado público (solo lectura)
  readonly carritoItems = this.items.asReadonly();
  readonly total = computed(() =>
    this.items().reduce((sum, item) => sum + item.precio * item.cantidad, 0)
  );
  readonly cantidadItems = computed(() =>
    this.items().reduce((sum, item) => sum + item.cantidad, 0)
  );
  readonly vacio = computed(() => this.items().length === 0);

  // Acciones
  agregar(producto: Producto) {
    this.items.update(items => {
      const existente = items.find(i => i.productoId === producto.id);
      if (existente) {
        return items.map(i =>
          i.productoId === producto.id ? { ...i, cantidad: i.cantidad + 1 } : i
        );
      }
      return [...items, { productoId: producto.id, nombre: producto.nombre, 
                          precio: producto.precio, cantidad: 1 }];
    });
  }

  eliminar(productoId: number) {
    this.items.update(items => items.filter(i => i.productoId !== productoId));
  }

  vaciar() {
    this.items.set([]);
  }
}
```

## 3. NgRx Basics (Para apps complejas)

```typescript
// Estado
interface AppState {
  usuarios: UsuarioState;
}
interface UsuarioState {
  lista: Usuario[];
  loading: boolean;
  error: string | null;
}

// Actions
export const cargarUsuarios = createAction('[Usuarios] Cargar');
export const cargarUsuariosExito = createAction('[Usuarios] Cargar Éxito', props<{ usuarios: Usuario[] }>());
export const cargarUsuariosError = createAction('[Usuarios] Cargar Error', props<{ error: string }>());

// Reducer
export const usuarioReducer = createReducer(
  initialState,
  on(cargarUsuarios, state => ({ ...state, loading: true })),
  on(cargarUsuariosExito, (state, { usuarios }) => ({ ...state, lista: usuarios, loading: false })),
  on(cargarUsuariosError, (state, { error }) => ({ ...state, error, loading: false })),
);

// Effects
export const cargarUsuarios$ = createEffect((
  actions$ = inject(Actions),
  service = inject(UsuarioService)
) => actions$.pipe(
  ofType(cargarUsuarios),
  switchMap(() => service.getAll().pipe(
    map(usuarios => cargarUsuariosExito({ usuarios })),
    catchError(err => of(cargarUsuariosError({ error: err.message }))),
  ))
), { functional: true });

// Selectors
export const selectUsuarios = (state: AppState) => state.usuarios.lista;
export const selectLoading = (state: AppState) => state.usuarios.loading;
```

## 4. Comparación

```
┌──────────────────┬─────────────────────┬──────────────────────┐
│ Criterio         │ Signals             │ NgRx                 │
├──────────────────┼─────────────────────┼──────────────────────┤
│ Complejidad      │ Baja                │ Alta                 │
│ Boilerplate      │ Mínimo              │ Mucho                │
│ Cuándo usar      │ Mayoría de apps     │ Apps muy complejas   │
│ DevTools         │ No (aún)            │ Sí (Redux DevTools)  │
│ Curva aprendizaje│ Fácil               │ Empinada             │
│ Testing          │ Simple              │ Bien estructurado    │
└──────────────────┴─────────────────────┴──────────────────────┘

Recomendación: Usar Signals + Services para el 90% de los casos.
NgRx solo si necesitas: time-travel debugging, undo/redo, estado muy complejo compartido.
```

## 5. Ejercicios

1. Implementa un `CarritoStateService` con signals (agregar, eliminar, total computed).
2. Crea un `AuthStateService` con signal para usuario actual y computed para isLoggedIn.
3. Implementa un store NgRx básico para un CRUD de productos (actions, reducer, effects).
4. Compara ambos enfoques implementando la misma funcionalidad con Signals y con NgRx.
5. Crea un servicio de estado con signals que persista en localStorage usando effect().

---

## Siguiente Módulo
→ [22-Testing Angular](../22-testing-angular/README.md)
