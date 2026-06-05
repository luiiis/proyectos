/**
 * MÓDULO 12: Directivas Angular - Ejemplo de referencia
 * Nuevo control flow (Angular 17+): @if, @for, @switch, @defer
 */

/*
// ═══════ NUEVO CONTROL FLOW (Angular 17+) ═══════

// @if (reemplaza *ngIf)
@if (loading()) {
  <mat-spinner />
} @else if (error()) {
  <p class="error">{{ error() }}</p>
} @else {
  <app-tabla [datos]="productos()" />
}

// @for (reemplaza *ngFor) - REQUIERE track
@for (producto of productos(); track producto.id) {
  <app-producto-card [producto]="producto" />
} @empty {
  <p>No hay productos disponibles</p>
}

// @switch (reemplaza ngSwitch)
@switch (usuario().rol) {
  @case ('ADMIN') { <app-admin-panel /> }
  @case ('VENDEDOR') { <app-ventas-panel /> }
  @default { <app-dashboard-basico /> }
}

// @defer (lazy loading a nivel de template)
@defer (on viewport) {
  <app-grafica-pesada [datos]="datosGrafica()" />
} @loading (minimum 300ms) {
  <mat-spinner diameter="24" />
} @placeholder {
  <div class="placeholder">Gráfica se cargará al hacer scroll</div>
}

// ═══════ CUSTOM DIRECTIVE ═══════
@Directive({
  selector: '[appHighlight]',
  standalone: true
})
export class HighlightDirective {
  color = input('yellow');

  constructor(private el: ElementRef) {}

  @HostListener('mouseenter') onMouseEnter() {
    this.el.nativeElement.style.backgroundColor = this.color();
  }

  @HostListener('mouseleave') onMouseLeave() {
    this.el.nativeElement.style.backgroundColor = '';
  }
}
// Uso: <p appHighlight color="lightblue">Texto con highlight</p>
*/

console.log('Módulo 12: Directivas. Referencia para usar en proyecto Angular.');
