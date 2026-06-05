import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterLink } from '@angular/router';
import { ProductoService, Producto } from '../../core/services/producto.service';

@Component({
  selector: 'app-productos',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule, RouterLink],
  template: `
    <nav style="background:#1a237e;color:white;padding:16px 24px;display:flex;gap:24px;align-items:center;">
      <span style="font-size:1.3rem;font-weight:bold;">🏢 ERP</span>
      <a routerLink="/dashboard" style="color:white;text-decoration:none;">Dashboard</a>
      <a routerLink="/productos" style="color:white;text-decoration:none;font-weight:bold;">Productos</a>
      <a routerLink="/ventas" style="color:white;text-decoration:none;">Ventas</a>
    </nav>
    <main style="padding:24px;max-width:1200px;margin:0 auto;">
      <div style="display:flex;justify-content:space-between;align-items:center;">
        <h2>Productos</h2>
        <input [(ngModel)]="busqueda" placeholder="Buscar..." (keyup.enter)="buscar()" style="padding:8px;border:1px solid #ccc;border-radius:4px;width:250px;">
      </div>
      <table style="width:100%;border-collapse:collapse;margin-top:16px;">
        <thead>
          <tr style="background:#f5f5f5;">
            <th style="padding:12px;text-align:left;border-bottom:2px solid #ddd;">Nombre</th>
            <th style="padding:12px;text-align:right;">Precio</th>
            <th style="padding:12px;text-align:center;">Stock</th>
            <th style="padding:12px;">SKU</th>
          </tr>
        </thead>
        <tbody>
          @for (p of productos(); track p.id) {
            <tr style="border-bottom:1px solid #eee;">
              <td style="padding:12px;">{{p.nombre}}</td>
              <td style="padding:12px;text-align:right;">{{p.precio | currency:'MXN'}}</td>
              <td style="padding:12px;text-align:center;" [style.color]="p.stock < 10 ? 'red' : 'inherit'">{{p.stock}}</td>
              <td style="padding:12px;color:#666;">{{p.sku}}</td>
            </tr>
          }
        </tbody>
      </table>
    </main>
  `
})
export class ProductosComponent implements OnInit {
  private service = inject(ProductoService);
  productos = signal<Producto[]>([]);
  busqueda = '';

  ngOnInit() { this.cargar(); }

  cargar() {
    this.service.listar().subscribe((res: any) => this.productos.set(res.content || []));
  }

  buscar() {
    if (this.busqueda.trim()) {
      this.service.buscar(this.busqueda).subscribe(r => this.productos.set(r));
    } else { this.cargar(); }
  }
}
