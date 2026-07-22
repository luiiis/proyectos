import { Component, OnInit, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';
import { AuthService } from '../services/auth.service';

interface Venta { id: number; numero: string; fecha: string; total: number; cliente: { nombre: string }; }

@Component({
  selector: 'app-ventas',
  standalone: true,
  imports: [CommonModule, TableModule, ButtonModule, ToolbarModule, RouterLink],
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
      <h2>Historial de Ventas</h2>
      <p-table [value]="ventas()" [tableStyle]="{'min-width':'50rem'}">
        <ng-template pTemplate="header">
          <tr>
            <th>Número</th>
            <th>Fecha</th>
            <th>Cliente</th>
            <th>Total</th>
          </tr>
        </ng-template>
        <ng-template pTemplate="body" let-v>
          <tr>
            <td>{{ v.numero }}</td>
            <td>{{ v.fecha | date:'dd/MM/yyyy HH:mm' }}</td>
            <td>{{ v.cliente?.nombre || 'N/A' }}</td>
            <td>{{ v.total | currency:'MXN' }}</td>
          </tr>
        </ng-template>
        <ng-template pTemplate="emptymessage">
          <tr><td colspan="4" style="text-align:center;padding:24px;">No hay ventas registradas aún</td></tr>
        </ng-template>
      </p-table>
    </div>
  `
})
export class VentasComponent implements OnInit {
  auth = inject(AuthService);
  private http = inject(HttpClient);
  ventas = signal<Venta[]>([]);

  ngOnInit() {
    this.http.get<Venta[]>('/api/ventas').subscribe(data => this.ventas.set(data));
  }
}
