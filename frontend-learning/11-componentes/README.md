# Módulo 11: Componentes Angular

## 1. @Component Básico

```typescript
@Component({
  selector: 'app-producto-card',
  standalone: true,
  imports: [CurrencyPipe],
  template: `
    <div class="card">
      <h3>{{ producto().nombre }}</h3>
      <p>{{ producto().precio | currency }}</p>
      <button (click)="onAgregar()">Agregar al carrito</button>
    </div>
  `,
  styles: [`.card { border: 1px solid #ddd; padding: 16px; border-radius: 8px; }`]
})
export class ProductoCardComponent {
  // Angular 17+ signals-based inputs/outputs
  producto = input.required<Producto>();
  agregar = output<Producto>();

  onAgregar() {
    this.agregar.emit(this.producto());
  }
}
```

## 2. Input y Output (Signals API - Angular 17+)

```typescript
// Componente hijo
@Component({ selector: 'app-usuario-form', standalone: true, ... })
export class UsuarioFormComponent {
  // Inputs (reemplazan @Input())
  usuario = input<Usuario>();                    // opcional
  modo = input.required<'crear' | 'editar'>();   // requerido
  titulo = input('Nuevo Usuario');               // con default

  // Outputs (reemplazan @Output())
  guardar = output<Usuario>();
  cancelar = output<void>();

  onSubmit(datos: Usuario) {
    this.guardar.emit(datos);
  }
}

// Componente padre (template)
// <app-usuario-form
//   [usuario]="usuarioSeleccionado()"
//   modo="editar"
//   (guardar)="onGuardar($event)"
//   (cancelar)="onCancelar()"
// />
```

## 3. Lifecycle Hooks

```typescript
export class DashboardComponent implements OnInit, OnDestroy {
  private destroy$ = new Subject<void>();

  // Se ejecuta después de crear el componente
  ngOnInit() {
    this.cargarDatos();
  }

  // Se ejecuta al destruir el componente
  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
```

```
Orden de ejecución:
constructor()        → Inyección de dependencias
ngOnInit()           → Inicialización (llamar APIs aquí)
ngOnChanges()        → Cuando cambian inputs
ngDoCheck()          → Cada ciclo de detección
ngAfterViewInit()    → Después de renderizar la vista
ngOnDestroy()        → Limpieza (unsubscribe, timers)
```

## 4. Content Projection

```typescript
// Componente contenedor reutilizable
@Component({
  selector: 'app-card',
  standalone: true,
  template: `
    <div class="card">
      <div class="card-header">
        <ng-content select="[card-title]" />
      </div>
      <div class="card-body">
        <ng-content />
      </div>
      <div class="card-footer">
        <ng-content select="[card-actions]" />
      </div>
    </div>
  `
})
export class CardComponent {}

// Uso:
// <app-card>
//   <h3 card-title>Título</h3>
//   <p>Contenido principal aquí</p>
//   <button card-actions>Guardar</button>
// </app-card>
```

## 5. Ejercicios

1. Crea un componente `DataTable` que reciba datos y columnas como input y emita eventos de selección.
2. Implementa un componente `Modal` con content projection (header, body, footer).
3. Crea un componente padre-hijo donde el padre pase datos y el hijo emita eventos.
4. Usa lifecycle hooks para cargar datos en `ngOnInit` y limpiar subscripciones en `ngOnDestroy`.
5. Implementa un componente `Tabs` con content projection para cada tab.

---

## Siguiente Módulo
→ [12-Directivas](../12-directivas/README.md)
