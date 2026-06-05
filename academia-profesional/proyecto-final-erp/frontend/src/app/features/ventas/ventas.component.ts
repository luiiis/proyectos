import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-ventas',
  standalone: true,
  imports: [CommonModule, HttpClientModule, RouterLink],
  template: `
    <nav style="background:#1a237e;color:white;padding:16px 24px;display:flex;gap:24px;align-items:center;">
      <span style="font-size:1.3rem;font-weight:bold;">🏢 ERP</span>
      <a routerLink="/dashboard" style="color:white;text-decoration:none;">Dashboard</a>
      <a routerLink="/productos" style="color:white;text-decoration:none;">Productos</a>
      <a routerLink="/ventas" style="color:white;text-decoration:none;font-weight:bold;">Ventas</a>
    </nav>
    <main style="padding:24px;max-width:1200px;margin:0 auto;">
      <h2>Historial de Ventas</h2>
      <table style="width:100%;border-collapse:collapse;margin-top:16px;">
        <thead>
          <tr style="background:#f5f5f5;">
            <th style="padding:12px;text-align:left;">Número</th>
            <th style="padding:12px;">Fecha</th>
            <th style="padding:12px;text-align:right;">Total</th>
            <th style="padding:12px;">Estado</th>
          </tr>
        </thead>
        <tbody>
          @for (v of ventas(); track v.id) {
            <tr style="border-bottom:1px solid #eee;">
              <td style="padding:12px;">{{v.numero}}</td>
              <td style="padding:12px;">{{v.fecha | date:'dd/MM/yyyy HH:mm'}}</td>
              <td style="padding:12px;text-align:right;">{{v.total | currency:'MXN'}}</td>
              <td style="padding:12px;"><span style="background:#4caf50;color:white;padding:2px 8px;border-radius:12px;font-size:12px;">{{v.estado}}</span></td>
            </tr>
          }
        </tbody>
      </table>
    </main>
  `
})
export class VentasComponent implements OnInit {
  private http = inject(HttpClient);
  ventas = signal<any[]>([]);

  ngOnInit() {
    this.http.get<any[]>('/api/ventas').subscribe(v => this.ventas.set(v));
  }
}
