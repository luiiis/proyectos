import { Component, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ProductService, Product } from '../../core/services/product.service';
import { rxResource } from '@angular/core/rxjs-interop';

/**
 * Componente de Productos - Angular 21 con Signals.
 *
 * ═══════ DIFERENCIAS CON TU PROYECTO ANTERIOR (Angular 17) ═══════
 *
 * ANTES (Angular 17):
 *   products: Product[] = [];
 *   loading = false;
 *   ngOnInit() {
 *     this.loading = true;
 *     this.productService.getAll().subscribe(res => {
 *       this.products = res.data;
 *       this.loading = false;
 *     });
 *   }
 *
 * AHORA (Angular 21):
 *   productsResource = rxResource({ loader: () => this.productService.getAll() });
 *   products = computed(() => this.productsResource.value()?.data ?? []);
 *   loading = this.productsResource.isLoading;
 *
 * ¿Qué ganamos?
 * 1. Menos código (no más subscribe/unsubscribe manual)
 * 2. Reactividad granular (solo se re-renderiza lo que cambió)
 * 3. Loading/error states automáticos
 * 4. Sin memory leaks (no hay subscriptions que olvidar limpiar)
 */
@Component({
  selector: 'app-products',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatChipsModule,
    MatProgressSpinnerModule,
  ],
  template: `
    <div class="products-container">
      <header class="products-header">
        <h2>Productos e Inventario</h2>
        <button mat-raised-button color="primary" (click)="showForm.set(!showForm())">
          <mat-icon>add</mat-icon> Nuevo Producto
        </button>
      </header>

      <!-- Búsqueda -->
      <mat-form-field appearance="outline" class="search-field">
        <mat-label>Buscar producto</mat-label>
        <input matInput [ngModel]="searchTerm()" (ngModelChange)="searchTerm.set($event)">
        <mat-icon matSuffix>search</mat-icon>
      </mat-form-field>

      <!-- Loading state -->
      @if (productsResource.isLoading()) {
        <mat-spinner diameter="40"></mat-spinner>
      }

      <!-- Error state -->
      @if (productsResource.error()) {
        <p class="error">Error cargando productos. Intenta de nuevo.</p>
      }

      <!-- Tabla de productos -->
      @if (filteredProducts().length > 0) {
        <table mat-table [dataSource]="filteredProducts()" class="full-width">
          <ng-container matColumnDef="name">
            <th mat-header-cell *matHeaderCellDef>Nombre</th>
            <td mat-cell *matCellDef="let p">{{ p.name }}</td>
          </ng-container>

          <ng-container matColumnDef="category">
            <th mat-header-cell *matHeaderCellDef>Categoría</th>
            <td mat-cell *matCellDef="let p">{{ p.category }}</td>
          </ng-container>

          <ng-container matColumnDef="price">
            <th mat-header-cell *matHeaderCellDef>Precio</th>
            <td mat-cell *matCellDef="let p">{{ p.price | currency:'MXN' }}</td>
          </ng-container>

          <ng-container matColumnDef="stock">
            <th mat-header-cell *matHeaderCellDef>Stock</th>
            <td mat-cell *matCellDef="let p">
              <mat-chip [color]="p.stock < 10 ? 'warn' : 'primary'">
                {{ p.stock }}
              </mat-chip>
            </td>
          </ng-container>

          <ng-container matColumnDef="actions">
            <th mat-header-cell *matHeaderCellDef>Acciones</th>
            <td mat-cell *matCellDef="let p">
              <button mat-icon-button color="primary" (click)="addStock(p)">
                <mat-icon>add_circle</mat-icon>
              </button>
              <button mat-icon-button color="warn" (click)="removeStock(p)">
                <mat-icon>remove_circle</mat-icon>
              </button>
            </td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="columns"></tr>
          <tr mat-row *matRowDef="let row; columns: columns;"></tr>
        </table>
      } @empty {
        <p>No hay productos disponibles.</p>
      }

      <!-- Resumen con computed signals -->
      <footer class="products-footer">
        <span>Total: {{ totalProducts() }} productos</span>
        <span>Valor inventario: {{ inventoryValue() | currency:'MXN' }}</span>
      </footer>
    </div>
  `,
  styles: [`
    .products-container { padding: 24px; }
    .products-header { display: flex; justify-content: space-between; align-items: center; }
    .search-field { width: 300px; margin: 16px 0; }
    .full-width { width: 100%; }
    .products-footer { margin-top: 16px; display: flex; gap: 24px; color: #666; }
    .error { color: #f44336; }
  `]
})
export class ProductsComponent {
  private productService = inject(ProductService);

  // ═══════ SIGNALS (estado reactivo) ═══════
  searchTerm = signal('');
  showForm = signal(false);
  columns = ['name', 'category', 'price', 'stock', 'actions'];

  // rxResource: carga datos automáticamente y expone loading/error/value como signals
  productsResource = rxResource({
    loader: () => this.productService.getAll()
  });

  // computed: se recalcula SOLO cuando sus dependencias (signals) cambian
  products = computed(() => this.productsResource.value()?.data ?? []);

  filteredProducts = computed(() => {
    const term = this.searchTerm().toLowerCase();
    if (!term) return this.products();
    return this.products().filter(p =>
      p.name.toLowerCase().includes(term) ||
      p.category?.toLowerCase().includes(term)
    );
  });

  totalProducts = computed(() => this.filteredProducts().length);

  inventoryValue = computed(() =>
    this.products().reduce((sum, p) => sum + (p.price * p.stock), 0)
  );

  // ═══════ ACCIONES ═══════
  addStock(product: Product): void {
    const qty = prompt('Cantidad a agregar:');
    if (qty) {
      this.productService.addStock(product.id, parseInt(qty), 'Entrada manual').subscribe({
        next: () => this.productsResource.reload()  // Recargar datos
      });
    }
  }

  removeStock(product: Product): void {
    const qty = prompt('Cantidad a retirar:');
    if (qty) {
      this.productService.removeStock(product.id, parseInt(qty), 'Salida manual').subscribe({
        next: () => this.productsResource.reload()
      });
    }
  }
}
