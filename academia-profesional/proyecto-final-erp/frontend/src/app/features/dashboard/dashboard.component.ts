import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterLink],
  template: `
    <nav style="background:#1a237e;color:white;padding:16px 24px;display:flex;align-items:center;gap:24px;">
      <span style="font-size:1.3rem;font-weight:bold;">🏢 ERP</span>
      <a routerLink="/dashboard" style="color:white;text-decoration:none;">Dashboard</a>
      <a routerLink="/productos" style="color:white;text-decoration:none;">Productos</a>
      <a routerLink="/ventas" style="color:white;text-decoration:none;">Ventas</a>
      <span style="flex:1"></span>
      <span>{{auth.user()?.username}}</span>
      <button (click)="auth.logout()" style="background:none;border:1px solid white;color:white;padding:6px 12px;border-radius:4px;cursor:pointer;">Salir</button>
    </nav>
    <main style="padding:24px;max-width:1200px;margin:0 auto;">
      <h2>Dashboard</h2>
      <div style="display:flex;gap:16px;flex-wrap:wrap;margin-top:16px;">
        <div style="background:white;padding:24px;border-radius:8px;box-shadow:0 2px 8px rgba(0,0,0,0.1);width:250px;text-align:center;">
          <h1 style="color:#1a237e;font-size:2.5rem;">$1.2M</h1><p>Ventas del Mes</p>
        </div>
        <div style="background:white;padding:24px;border-radius:8px;box-shadow:0 2px 8px rgba(0,0,0,0.1);width:250px;text-align:center;">
          <h1 style="color:#1a237e;font-size:2.5rem;">3,450</h1><p>Pedidos</p>
        </div>
        <div style="background:white;padding:24px;border-radius:8px;box-shadow:0 2px 8px rgba(0,0,0,0.1);width:250px;text-align:center;">
          <h1 style="color:#4caf50;font-size:2.5rem;">89%</h1><p>Satisfacción</p>
        </div>
        <div style="background:white;padding:24px;border-radius:8px;box-shadow:0 2px 8px rgba(0,0,0,0.1);width:250px;text-align:center;">
          <h1 style="color:#f44336;font-size:2.5rem;">12</h1><p>Stock Bajo</p>
        </div>
      </div>
    </main>
  `
})
export class DashboardComponent {
  auth = inject(AuthService);
}
