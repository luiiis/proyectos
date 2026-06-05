import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatMenuModule } from '@angular/material/menu';
import { MatChipsModule } from '@angular/material/chips';
import { ProductService } from '@app/core/services/product.service';
import { AuthService } from '@app/core/services/auth.service';
import { Product } from '@app/core/models/product.model';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatFormFieldModule,
    MatInputModule,
    MatDialogModule,
    MatMenuModule,
    MatChipsModule
  ],
  template: `
    <mat-toolbar color="primary">
      <button mat-icon-button routerLink="/dashboard">
        <mat-icon>arrow_back</mat-icon>
      </button>
      <span>Gestión de Productos</span>
      <span class="spacer"></span>
      <button mat-icon-button [matMenuTriggerFor]="menu">
        <mat-icon>account_circle</mat-icon>
      </button>
      <mat-menu #menu="matMenu">
        <button mat-menu-item (click)="logout()">
          <mat-icon>exit_to_app</mat-icon> Cerrar Sesión
        </button>
      </mat-menu>
    </mat-toolbar>

    <div class="container">
      <div class="header-actions">
        <h2>Productos e Inventario</h2>
        <button mat-raised-button color="primary" (click)="showCreateForm = !showCreateForm">
          <mat-icon>add</mat-icon> Nuevo Producto
        </button>
      </div>

      <!-- Formulario de creación -->
      @if (showCreateForm) {
        <div class="create-form">
          <mat-form-field appearance="outline">
            <mat-label>Nombre</mat-label>
            <input matInput [(ngModel)]="newProduct.name">
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Descripción</mat-label>
            <input matInput [(ngModel)]="newProduct.description">
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Precio</mat-label>
            <input matInput type="number" [(ngModel)]="newProduct.price">
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Stock</mat-label>
            <input matInput type="number" [(ngModel)]="newProduct.stock">
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>Categoría</mat-label>
            <input matInput [(ngModel)]="newProduct.category">
          </mat-form-field>
          <mat-form-field appearance="outline">
            <mat-label>SKU</mat-label>
            <input matInput [(ngModel)]="newProduct.sku">
          </mat-form-field>
          <button mat-raised-button color="accent" (click)="createProduct()">Guardar</button>
          <button mat-button (click)="showCreateForm = false">Cancelar</button>
        </div>
      }

      <!-- Búsqueda -->
      <mat-form-field class="search-field" appearance="outline">
        <mat-label>Buscar producto</mat-label>
        <input matInput [(ngModel)]="searchTerm" (keyup.enter)="searchProducts()">
        <mat-icon matSuffix (click)="searchProducts()" style="cursor:pointer">search</mat-icon>
      </mat-form-field>

      <!-- Tabla de productos -->
      <table mat-table [dataSource]="products" class="full-width">
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
          <td mat-cell *matCellDef="let p">{{ p.price | currency }}</td>
        </ng-container>

        <ng-container matColumnDef="stock">
          <th mat-header-cell *matHeaderCellDef>Stock</th>
          <td mat-cell *matCellDef="let p">
            <mat-chip [color]="p.stock < 10 ? 'warn' : 'primary'">
              {{ p.stock }}
            </mat-chip>
          </td>
        </ng-container>

        <ng-container matColumnDef="sku">
          <th mat-header-cell *matHeaderCellDef>SKU</th>
          <td mat-cell *matCellDef="let p">{{ p.sku }}</td>
        </ng-container>

        <ng-container matColumnDef="actions">
          <th mat-header-cell *matHeaderCellDef>Acciones</th>
          <td mat-cell *matCellDef="let p">
            <button mat-icon-button color="primary" (click)="addStock(p.id)">
              <mat-icon>add_circle</mat-icon>
            </button>
            <button mat-icon-button color="accent" (click)="removeStock(p.id)">
              <mat-icon>remove_circle</mat-icon>
            </button>
            <button mat-icon-button color="warn" (click)="deleteProduct(p.id)">
              <mat-icon>delete</mat-icon>
            </button>
          </td>
        </ng-container>

        <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
        <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
      </table>
    </div>
  `,
  styles: [`
    .spacer { flex: 1 1 auto; }
    .container { padding: 24px; }
    .header-actions { display: flex; justify-content: space-between; align-items: center; }
    .create-form {
      display: flex; flex-wrap: wrap; gap: 12px;
      padding: 16px; background: #f5f5f5; border-radius: 8px; margin-bottom: 16px;
      align-items: center;
    }
    .search-field { width: 300px; margin-bottom: 16px; }
    .full-width { width: 100%; }
  `]
})
export class ProductsComponent implements OnInit {
  products: Product[] = [];
  displayedColumns = ['name', 'category', 'price', 'stock', 'sku', 'actions'];
  showCreateForm = false;
  searchTerm = '';
  newProduct: Partial<Product> = {};

  constructor(
    private productService: ProductService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.productService.getAll().subscribe({
      next: (response) => {
        if (response.success) {
          this.products = response.data;
        }
      }
    });
  }

  searchProducts(): void {
    if (this.searchTerm.trim()) {
      this.productService.search(this.searchTerm).subscribe({
        next: (response) => {
          if (response.success) this.products = response.data;
        }
      });
    } else {
      this.loadProducts();
    }
  }

  createProduct(): void {
    this.productService.create(this.newProduct).subscribe({
      next: () => {
        this.showCreateForm = false;
        this.newProduct = {};
        this.loadProducts();
      }
    });
  }

  addStock(id: number): void {
    const quantity = prompt('Cantidad a agregar:');
    if (quantity) {
      this.productService.addStock(id, parseInt(quantity), 'Entrada manual').subscribe({
        next: () => this.loadProducts()
      });
    }
  }

  removeStock(id: number): void {
    const quantity = prompt('Cantidad a retirar:');
    if (quantity) {
      this.productService.removeStock(id, parseInt(quantity), 'Salida manual').subscribe({
        next: () => this.loadProducts()
      });
    }
  }

  deleteProduct(id: number): void {
    if (confirm('¿Eliminar este producto?')) {
      this.productService.delete(id).subscribe({
        next: () => this.loadProducts()
      });
    }
  }

  logout(): void {
    this.authService.logout();
  }
}
