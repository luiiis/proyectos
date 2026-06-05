/**
 * MÓDULO 11: Componentes Angular - Ejemplo de referencia
 * Este archivo NO se ejecuta solo. Se usa dentro de un proyecto Angular.
 * Para ejecutar: crear proyecto con `ng new` y copiar este código.
 */

// ═══════ Componente con Signals (Angular 17+) ═══════
/*
import { Component, input, output, signal, computed } from '@angular/core';

@Component({
  selector: 'app-producto-card',
  standalone: true,
  template: `
    <div class="card" [class.agotado]="producto().stock === 0">
      <h3>{{ producto().nombre }}</h3>
      <p class="precio">{{ producto().precio | currency:'MXN' }}</p>
      <p>Stock: {{ producto().stock }}</p>

      @if (producto().stock > 0) {
        <button (click)="onComprar()">Comprar</button>
      } @else {
        <span class="badge">Agotado</span>
      }
    </div>
  `
})
export class ProductoCardComponent {
  // Signal inputs (Angular 17+) - reemplazan @Input()
  producto = input.required<Producto>();
  mostrarAcciones = input(true);  // Con valor default

  // Signal outputs - reemplazan @Output() + EventEmitter
  comprar = output<number>();

  // Computed signal (se recalcula cuando producto cambia)
  precioConIva = computed(() => this.producto().precio * 1.16);

  onComprar() {
    this.comprar.emit(this.producto().id);
  }
}

// ═══════ Lifecycle Hooks ═══════
// ngOnInit()      → Después de crear el componente (cargar datos)
// ngOnChanges()   → Cuando un @Input cambia
// ngOnDestroy()   → Antes de destruir (limpiar subscriptions)
// ngAfterViewInit() → Después de renderizar el template

interface Producto {
  id: number;
  nombre: string;
  precio: number;
  stock: number;
}
*/

console.log('Este archivo es referencia. Úsalo dentro de un proyecto Angular.');
console.log('Crear proyecto: ng new mi-app --standalone');
console.log('Crear componente: ng generate component features/producto-card');
