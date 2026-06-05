# Módulo 23: Performance en Angular

## 1. Change Detection

```
┌─────────────────────────────────────────────────────┐
│  Change Detection: Default vs OnPush                │
├─────────────────────────────────────────────────────┤
│                                                     │
│  Default: Revisa TODOS los componentes en cada      │
│  evento (click, timer, HTTP response)               │
│                                                     │
│  OnPush: Solo revisa si:                            │
│  1. Un @Input() cambió su referencia                │
│  2. Un evento del template se disparó               │
│  3. Un signal cambió                                │
│  4. Se llamó markForCheck() manualmente             │
│                                                     │
└─────────────────────────────────────────────────────┘
```

```typescript
// ✅ OnPush - usar SIEMPRE en componentes nuevos
@Component({
  selector: 'app-producto-card',
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: true,
  template: `<div>{{ producto().nombre }}</div>`
})
export class ProductoCardComponent {
  producto = input.required<Producto>();
}
```

## 2. Lazy Loading

```typescript
// Lazy loading de rutas (carga solo cuando se navega)
export const routes: Routes = [
  {
    path: 'reportes',
    loadComponent: () => import('./features/reportes/reporte-list.component')
      .then(m => m.ReporteListComponent),
  },
  {
    path: 'admin',
    loadChildren: () => import('./features/admin/admin.routes')
      .then(m => m.ADMIN_ROUTES),
    canActivate: [authGuard, roleGuard],
  },
];
```

## 3. @defer (Angular 17+)

```html
<!-- Carga el componente solo cuando es visible en viewport -->
@defer (on viewport) {
  <app-comentarios [productoId]="producto().id" />
} @placeholder {
  <div class="skeleton">Cargando comentarios...</div>
} @loading (minimum 500ms) {
  <app-spinner />
} @error {
  <p>Error al cargar comentarios</p>
}

<!-- Otras condiciones de @defer -->
@defer (on idle) { ... }           <!-- cuando el browser está idle -->
@defer (on interaction) { ... }    <!-- al interactuar (click, focus) -->
@defer (on timer(3s)) { ... }      <!-- después de 3 segundos -->
@defer (when condicion()) { ... }  <!-- cuando una expresión es true -->
```

## 4. Virtual Scroll

```typescript
@Component({
  standalone: true,
  imports: [ScrollingModule],
  template: `
    <!-- Solo renderiza los items visibles (~20) de una lista de 10,000 -->
    <cdk-virtual-scroll-viewport itemSize="48" class="lista-viewport">
      <div *cdkVirtualFor="let usuario of usuarios(); trackBy: trackById" class="item">
        {{ usuario.nombre }} - {{ usuario.email }}
      </div>
    </cdk-virtual-scroll-viewport>
  `,
  styles: [`.lista-viewport { height: 400px; }`]
})
export class UsuarioListComponent {
  usuarios = signal<Usuario[]>([]);  // puede tener 10,000+ items
  trackById = (index: number, item: Usuario) => item.id;
}
```

## 5. trackBy en @for

```html
<!-- ✅ track evita re-renderizar elementos que no cambiaron -->
@for (producto of productos(); track producto.id) {
  <app-producto-card [producto]="producto" />
}

<!-- ❌ Sin track: Angular destruye y recrea TODOS los elementos al cambiar la lista -->
```

## 6. Ejercicios

1. Convierte 5 componentes a `ChangeDetectionStrategy.OnPush` y verifica que siguen funcionando.
2. Implementa `@defer (on viewport)` para cargar componentes pesados solo cuando son visibles.
3. Usa `cdk-virtual-scroll-viewport` para renderizar una lista de 10,000 elementos.
4. Configura lazy loading para todas las rutas de features.
5. Usa Chrome DevTools (Performance tab) para medir el impacto de OnPush vs Default.

---

## Siguiente Módulo
→ [24-Accesibilidad](../24-accesibilidad/README.md)
