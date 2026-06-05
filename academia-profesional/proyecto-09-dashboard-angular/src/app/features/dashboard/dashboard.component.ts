import { Component, inject } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [MatToolbarModule, MatCardModule, MatButtonModule, MatIconModule],
  template: `
    <mat-toolbar color="primary">
      <mat-icon>dashboard</mat-icon>
      <span style="margin-left:8px">Dashboard</span>
      <span style="flex:1"></span>
      <span>{{ auth.user()?.username }}</span>
      <button mat-icon-button (click)="auth.logout()"><mat-icon>exit_to_app</mat-icon></button>
    </mat-toolbar>

    <div style="padding:24px;display:flex;gap:16px;flex-wrap:wrap;">
      <mat-card style="width:250px;text-align:center;padding:24px;">
        <h1 style="font-size:2.5rem;color:#1a237e;">$1.2M</h1>
        <p>Ventas del Mes</p>
      </mat-card>
      <mat-card style="width:250px;text-align:center;padding:24px;">
        <h1 style="font-size:2.5rem;color:#1a237e;">3,450</h1>
        <p>Pedidos</p>
      </mat-card>
      <mat-card style="width:250px;text-align:center;padding:24px;">
        <h1 style="font-size:2.5rem;color:#4caf50;">89%</h1>
        <p>Satisfacción</p>
      </mat-card>
      <mat-card style="width:250px;text-align:center;padding:24px;">
        <h1 style="font-size:2.5rem;color:#f44336;">12</h1>
        <p>Stock Bajo</p>
      </mat-card>
    </div>
  `
})
export class DashboardComponent {
  auth = inject(AuthService);
}
