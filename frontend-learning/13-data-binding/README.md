# Módulo 13: Data Binding en Angular

## 1. Tipos de Binding

```
┌─────────────────────────────────────────────────────┐
│              DATA BINDING                            │
├─────────────────────────────────────────────────────┤
│                                                     │
│  Component ──────────────────────────> Template      │
│             Interpolation: {{ valor }}               │
│             Property: [propiedad]="valor"            │
│                                                     │
│  Component <────────────────────────── Template      │
│             Event: (evento)="handler($event)"       │
│                                                     │
│  Component <─────────────────────────> Template      │
│             Two-way: [(ngModel)]="valor"            │
│             Two-way: [(signal)]="valor" (Angular 18)│
│                                                     │
└─────────────────────────────────────────────────────┘
```

## 2. Interpolation y Property Binding

```html
<!-- Interpolation {{ }} → muestra texto -->
<h1>{{ titulo() }}</h1>
<p>Total: {{ productos().length }} productos</p>
<span>{{ precio() | currency:'USD' }}</span>

<!-- Property Binding [ ] → pasa valores a propiedades/inputs -->
<img [src]="producto().imagen" [alt]="producto().nombre">
<button [disabled]="!formularioValido()">Guardar</button>
<app-tabla [datos]="usuarios()" [columnas]="columnas" />
<div [class.activo]="estaActivo()" [style.color]="color()"></div>
```

## 3. Event Binding

```html
<!-- Event Binding ( ) → escucha eventos del template -->
<button (click)="guardar()">Guardar</button>
<input (input)="buscar($event)" (keyup.enter)="ejecutarBusqueda()">
<form (ngSubmit)="onSubmit()">...</form>
<tr (click)="seleccionar(usuario)">...</tr>

<!-- $event contiene el evento nativo -->
<input (input)="onInput($event)">
```

```typescript
onInput(event: Event) {
  const valor = (event.target as HTMLInputElement).value;
  this.filtro.set(valor);
}
```

## 4. Two-Way Binding con Signals (Angular 17+)

```typescript
@Component({
  selector: 'app-busqueda',
  standalone: true,
  imports: [FormsModule],
  template: `
    <!-- Two-way binding clásico con ngModel -->
    <input [(ngModel)]="termino" placeholder="Buscar...">
    <p>Buscando: {{ termino }}</p>

    <!-- Two-way binding con model signals (Angular 18+) -->
    <app-filtro [(valor)]="filtroActual" />
  `
})
export class BusquedaComponent {
  termino = '';
  filtroActual = signal('');
}

// Componente hijo con model()
@Component({
  selector: 'app-filtro',
  standalone: true,
  template: `<input [value]="valor()" (input)="onInput($event)">`
})
export class FiltroComponent {
  valor = model<string>('');  // two-way binding signal

  onInput(event: Event) {
    this.valor.set((event.target as HTMLInputElement).value);
  }
}
```

## 5. Signals para Estado Reactivo

```typescript
@Component({ ... })
export class ProductoListComponent {
  // Estado reactivo con signals
  productos = signal<Producto[]>([]);
  filtro = signal('');
  
  // Computed: se recalcula automáticamente
  productosFiltrados = computed(() => {
    const term = this.filtro().toLowerCase();
    return this.productos().filter(p => 
      p.nombre.toLowerCase().includes(term)
    );
  });

  total = computed(() => 
    this.productosFiltrados().reduce((sum, p) => sum + p.precio, 0)
  );

  // Effect: ejecuta side effects cuando cambian signals
  constructor() {
    effect(() => {
      console.log(`Filtro cambió a: ${this.filtro()}`);
    });
  }
}
```

## 6. Ejercicios

1. Crea un componente con interpolation, property binding y event binding.
2. Implementa un buscador con two-way binding que filtre una lista en tiempo real.
3. Usa signals (signal, computed) para manejar el estado de un carrito de compras.
4. Crea un componente hijo con `model()` para two-way binding con el padre.
5. Implementa un formulario donde los botones se habiliten/deshabiliten según el estado.

---

## Siguiente Módulo
→ [14-Servicios](../14-servicios/README.md)
