import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatChipsModule } from '@angular/material/chips';

interface Producto {
  id: number;
  nombre: string;
  precio: number;
  stock: number;
  sku: string;
  categoriaId: number;
  activo: boolean;
}

@Component({
  selector: 'app-productos',
  standalone: true,
  imports: [
    CommonModule, FormsModule, HttpClientModule,
    MatTableModule, MatButtonModule, MatIconModule,
    MatFormFieldModule, MatInputModule, MatChipsModule
  ],
  template: `
    <h2>Gestión de Productos</h2>

    <mat-form-field appearance="outline" style="width: 300px; margin-bottom: 16px;">
      <mat-label>Buscar producto</mat-label>
      <input matInput [(ngModel)]="busqueda" (keyup.enter)="buscar()">
      <mat-icon matSuffix (click)="buscar()" style="cursor:pointer">search</mat-icon>
    </mat-form-field>

    <button mat-raised-button color="primary" (click)="cargar()" style="margin-left: 16px;">
      <mat-icon>refresh</mat-icon> Recargar
    </button>

    <table mat-table [dataSource]="productos" style="width: 100%;">
      <ng-container matColumnDef="nombre">
        <th mat-header-cell *matHeaderCellDef>Nombre</th>
        <td mat-cell *matCellDef="let p">{{p.nombre}}</td>
      </ng-container>

      <ng-container matColumnDef="precio">
        <th mat-header-cell *matHeaderCellDef>Precio</th>
        <td mat-cell *matCellDef="let p">{{p.precio | currency:'MXN'}}</td>
      </ng-container>

      <ng-container matColumnDef="stock">
        <th mat-header-cell *matHeaderCellDef>Stock</th>
        <td mat-cell *matCellDef="let p">
          <mat-chip [color]="p.stock < 10 ? 'warn' : 'primary'" selected>
            {{p.stock}}
          </mat-chip>
        </td>
      </ng-container>

      <ng-container matColumnDef="sku">
        <th mat-header-cell *matHeaderCellDef>SKU</th>
        <td mat-cell *matCellDef="let p">{{p.sku}}</td>
      </ng-container>

      <ng-container matColumnDef="acciones">
        <th mat-header-cell *matHeaderCellDef>Acciones</th>
        <td mat-cell *matCellDef="let p">
          <button mat-icon-button color="warn" (click)="eliminar(p.id)">
            <mat-icon>delete</mat-icon>
          </button>
        </td>
      </ng-container>

      <tr mat-header-row *matHeaderRowDef="columnas"></tr>
      <tr mat-row *matRowDef="let row; columns: columnas;"></tr>
    </table>

    <p *ngIf="productos.length === 0" style="text-align: center; color: #666; margin-top: 24px;">
      No hay productos. Verifica que el backend está corriendo en http://localhost:8080
    </p>
  `
})
export class ProductosComponent implements OnInit {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/productos';

  productos: Producto[] = [];
  busqueda = '';
  columnas = ['nombre', 'precio', 'stock', 'sku', 'acciones'];

  ngOnInit() {
    this.cargar();
  }

  cargar() {
    this.http.get<any>(`${this.apiUrl}?size=50`).subscribe({
      next: (res) => this.productos = res.content || res,
      error: (err) => console.error('Error cargando productos:', err)
    });
  }

  buscar() {
    if (this.busqueda.trim()) {
      this.http.get<Producto[]>(`${this.apiUrl}/buscar?q=${this.busqueda}`).subscribe({
        next: (res) => this.productos = res,
        error: (err) => console.error('Error buscando:', err)
      });
    } else {
      this.cargar();
    }
  }

  eliminar(id: number) {
    if (confirm('¿Eliminar este producto?')) {
      this.http.delete(`${this.apiUrl}/${id}`).subscribe({
        next: () => this.cargar(),
        error: (err) => console.error('Error eliminando:', err)
      });
    }
  }
}
