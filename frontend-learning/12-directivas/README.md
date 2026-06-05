# Módulo 12: Directivas y Control Flow

## 1. Nuevo Control Flow (Angular 17+)

```html
<!-- @if (reemplaza *ngIf) -->
@if (usuarios().length > 0) {
  <app-tabla [datos]="usuarios()" />
} @else if (cargando()) {
  <app-spinner />
} @else {
  <p>No hay usuarios registrados</p>
}

<!-- @for (reemplaza *ngFor) -->
@for (usuario of usuarios(); track usuario.id) {
  <app-usuario-card [usuario]="usuario" />
} @empty {
  <p>Lista vacía</p>
}

<!-- @switch (reemplaza ngSwitch) -->
@switch (usuario().rol) {
  @case ('admin') { <app-admin-panel /> }
  @case ('user') { <app-user-dashboard /> }
  @default { <app-guest-view /> }
}
```

## 2. Comparación: Nuevo vs Antiguo

```html
<!-- ❌ Antiguo (aún funciona pero no recomendado) -->
<div *ngIf="usuarios.length > 0; else vacio">
  <div *ngFor="let u of usuarios; trackBy: trackById">
    {{ u.nombre }}
  </div>
</div>
<ng-template #vacio><p>Sin datos</p></ng-template>

<!-- ✅ Nuevo (Angular 17+) - más legible -->
@if (usuarios().length > 0) {
  @for (u of usuarios(); track u.id) {
    <div>{{ u.nombre }}</div>
  }
} @else {
  <p>Sin datos</p>
}
```

## 3. Track en @for

```html
<!-- track es OBLIGATORIO en @for -->
<!-- Usa una propiedad única para optimizar el rendering -->

@for (producto of productos(); track producto.id) {
  <app-producto-card [producto]="producto" />
}

<!-- Para arrays simples, usar $index -->
@for (nombre of nombres(); track $index) {
  <li>{{ nombre }}</li>
}

<!-- Variables implícitas disponibles -->
@for (item of items(); track item.id; let i = $index, let first = $first, let last = $last) {
  <div [class.first]="first" [class.last]="last">
    {{ i + 1 }}. {{ item.nombre }}
  </div>
}
```

## 4. Directivas Personalizadas

```typescript
// Directiva de atributo: highlight al hacer hover
@Directive({
  selector: '[appHighlight]',
  standalone: true,
})
export class HighlightDirective {
  color = input('yellow', { alias: 'appHighlight' });

  @HostListener('mouseenter') onEnter() {
    this.el.nativeElement.style.backgroundColor = this.color();
  }

  @HostListener('mouseleave') onLeave() {
    this.el.nativeElement.style.backgroundColor = '';
  }

  constructor(private el: ElementRef) {}
}

// Uso: <tr appHighlight="lightblue">...</tr>
```

```typescript
// Directiva estructural: permisos
@Directive({
  selector: '[appHasRole]',
  standalone: true,
})
export class HasRoleDirective {
  private authService = inject(AuthService);

  @Input() set appHasRole(roles: string[]) {
    const userRol = this.authService.getCurrentRole();
    if (roles.includes(userRol)) {
      this.vcRef.createEmbeddedView(this.templateRef);
    } else {
      this.vcRef.clear();
    }
  }

  constructor(
    private templateRef: TemplateRef<any>,
    private vcRef: ViewContainerRef
  ) {}
}

// Uso: <button *appHasRole="['admin']">Eliminar</button>
```

## 5. Ejercicios

1. Convierte un template con `*ngIf` y `*ngFor` al nuevo control flow (@if, @for).
2. Crea una tabla que use `@for` con track y muestre `@empty` cuando no hay datos.
3. Implementa un `@switch` para renderizar diferentes vistas según el rol del usuario.
4. Crea una directiva `appTooltip` que muestre un tooltip al hacer hover.
5. Implementa una directiva `appHasPermission` que oculte elementos según permisos.

---

## Siguiente Módulo
→ [13-Data Binding](../13-data-binding/README.md)
