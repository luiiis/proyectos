import { Component, OnInit, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RouterLink } from '@angular/router';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CardModule, ButtonModule, ToolbarModule, RouterLink],
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
      <h2>Dashboard</h2>
      <div style="display:flex;gap:16px;flex-wrap:wrap;">
        <p-card header="Ventas del Mes" [style]="{width:'250px',textAlign:'center'}">
          <h1 style="color:#1e3a5f;font-size:2rem;">\${{ kpis().ventasMes | number:'1.0-0' }}</h1>
        </p-card>
        <p-card header="Pedidos" [style]="{width:'250px',textAlign:'center'}">
          <h1 style="color:#1e3a5f;font-size:2rem;">{{ kpis().pedidosMes }}</h1>
        </p-card>
        <p-card header="Productos" [style]="{width:'250px',textAlign:'center'}">
          <h1 style="color:#4caf50;font-size:2rem;">{{ kpis().totalProductos }}</h1>
        </p-card>
        <p-card header="Stock Bajo" [style]="{width:'250px',textAlign:'center'}">
          <h1 style="color:#f44336;font-size:2rem;">{{ kpis().productosStockBajo }}</h1>
        </p-card>
      </div>
    </div>
  `
})
export class DashboardComponent implements OnInit {
  auth = inject(AuthService);
  private http = inject(HttpClient);

  kpis = signal({ ventasMes: 0, pedidosMes: 0, totalProductos: 0, productosStockBajo: 0 });

  ngOnInit() {
    this.http.get<any>('/api/ventas/dashboard').subscribe(data => this.kpis.set(data));
  }
}
