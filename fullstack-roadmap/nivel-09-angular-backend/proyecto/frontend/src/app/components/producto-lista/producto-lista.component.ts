import { Component, OnInit, signal, inject } from '@angular/core';
import { ProductoService, Producto } from '../../services/producto.service';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

/**
 * Componente: Lista de Productos.
 * Se comunica con el backend a través del ProductoService.
 */
@Component({
  selector: 'app-producto-lista',
  standalone: true,
  imports: [FormsModule, RouterLink],
  template: `
    <h2>📋 Productos</h2>

    <!-- Buscador -->
    <div style="margin-bottom: 1rem; display: flex; gap: 0.5rem;">
      <input
        [(ngModel)]="busqueda"
        (keyup.enter)="buscar()"
        placeholder="Buscar producto..."
        style="flex: 1; padding: 0.5rem;"
      />
      <button (click)="buscar()">🔍 Buscar</button>
      <button (click)="cargar()">🔄 Todos</button>
      <a routerLink="/productos/nuevo" style="padding: 0.5rem 1rem; background: #28a745; color: white; text-decoration: none; border-radius: 4px;">
        ➕ Nuevo
      </a>
    </div>

    <!-- Tabla -->
    @if (cargando()) {
      <p>Cargando...</p>
    } @else {
      <table style="width: 100%; border-collapse: collapse;">
        <thead>
          <tr style="background: #f5f5f5;">
            <th style="padding: 0.5rem; text-align: left;">Nombre</th>
            <th style="padding: 0.5rem; text-align: left;">Categoría</th>
            <th style="padding: 0.5rem; text-align: right;">Precio</th>
            <th style="padding: 0.5rem; text-align: right;">Existencia</th>
            <th style="padding: 0.5rem;">Acciones</th>
          </tr>
        </thead>
        <tbody>
          @for (producto of productos(); track producto.id) {
            <tr style="border-bottom: 1px solid #eee;">
              <td style="padding: 0.5rem;">{{ producto.nombre }}</td>
              <td style="padding: 0.5rem;">{{ producto.categoriaNombre }}</td>
              <td style="padding: 0.5rem; text-align: right;">\${{ producto.precio.toFixed(2) }}</td>
              <td style="padding: 0.5rem; text-align: right;"
                  [style.color]="producto.existencia < 10 ? 'red' : 'inherit'">
                {{ producto.existencia }}
              </td>
              <td style="padding: 0.5rem; text-align: center;">
                <a [routerLink]="['/productos/editar', producto.id]">✏️</a>
                <button (click)="eliminar(producto.id)">🗑️</button>
              </td>
            </tr>
          } @empty {
            <tr><td colspan="5" style="text-align: center; padding: 2rem;">No hay productos</td></tr>
          }
        </tbody>
      </table>
    }

    <!-- Mensaje -->
    @if (mensaje()) {
      <div style="margin-top: 1rem; padding: 0.5rem; background: #d4edda; border-radius: 4px;">
        {{ mensaje() }}
      </div>
    }
  `
})
export class ProductoListaComponent implements OnInit {
  private productoService = inject(ProductoService);

  productos = signal<Producto[]>([]);
  cargando = signal(false);
  mensaje = signal('');
  busqueda = '';

  ngOnInit() {
    this.cargar();
  }

  cargar() {
    this.cargando.set(true);
    this.productoService.listarTodos().subscribe({
      next: (res) => {
        this.productos.set(res.data);
        this.cargando.set(false);
      },
      error: () => this.cargando.set(false)
    });
  }

  buscar() {
    if (!this.busqueda.trim()) {
      this.cargar();
      return;
    }
    this.cargando.set(true);
    this.productoService.buscarPorNombre(this.busqueda).subscribe({
      next: (res) => {
        this.productos.set(res.data);
        this.cargando.set(false);
      },
      error: () => this.cargando.set(false)
    });
  }

  eliminar(id: number) {
    if (confirm('¿Eliminar este producto?')) {
      this.productoService.eliminar(id).subscribe({
        next: () => {
          this.mensaje.set('Producto eliminado');
          this.cargar();
          setTimeout(() => this.mensaje.set(''), 3000);
        }
      });
    }
  }
}
