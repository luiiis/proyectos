import { Component, OnInit, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RouterLink } from '@angular/router';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { TagModule } from 'primeng/tag';
import { AuthService } from '../services/auth.service';

interface Producto { id: number; nombre: string; precio: number; stock: number; sku: string; }

@Component({
  selector: 'app-productos',
  standalone: true,
  imports: [TableModule, ButtonModule, ToolbarModule, TagModule, RouterLink],
  template: `
    <p-toolbar>
      <div class="p-toolbar-group-start">
        <button pButton label="Dashboard" [routerLink]="'/dashboard'" class="p-button-text"></button>
        <button pButton label="Productos" [routerLink]="'/productos'" class="p-button-text"></button>
        <button pButton label="Ventas" [routerLink]="'/ventas'" class="p-button-text"></button>
      </div>
      <div class="p-toolbar-group-end">
        <span style="margin-right:12px">{{ auth.user()?.nombre }}</span>
        <button pButton icon="pi pi-sign-out" class="p-button-danger p-button-text" (click)="auth.logout()"></button>
      </div>
    </p-toolbar>

    <div style="padding:24px;">
      <h2>Productos</h2>
      <p-table [value]="productos()" [paginator]="true" [rows]="10" [tableStyle]="{'min-width':'50rem'}">
        <ng-template pTemplate="header">
          <tr>
            <th pSortableColumn="sku">SKU</th>
            <th pSortableColumn="nombre">Nombre</th>
            <th pSortableColumn="precio">Precio</th>
            <th pSortableColumn="stock">Stock</th>
          </tr>
        </ng-template>
        <ng-template pTemplate="body" let-p>
          <tr>
            <td>{{ p.sku }}</td>
            <td>{{ p.nombre }}</td>
            <td>{{ p.precio | currency:'MXN' }}</td>
            <td>
              @if (p.stock < 5) { <p-tag severity="danger" [value]="p.stock + ' ⚠️'" /> }
              @else { {{ p.stock }} }
            </td>
          </tr>
        </ng-template>
      </p-table>
    </div>
  `
})
export class ProductosComponent implements OnInit {
  auth = inject(AuthService);
  private http = inject(HttpClient);
  productos = signal<Producto[]>([]);

  ngOnInit() {
    this.http.get<Producto[]>('/api/productos').subscribe(data => this.productos.set(data));
  }
}
