import { Component, inject, computed } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatMenuModule } from '@angular/material/menu';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterLink, MatToolbarModule, MatButtonModule, MatIconModule, MatCardModule, MatMenuModule],
  template: `
    <mat-toolbar color="primary">
      <span>Sistema 2026</span>
      <span class="spacer"></span>
      <button mat-button routerLink="/products">
        <mat-icon>inventory</mat-icon> Productos
      </button>
      @if (authService.isAdmin()) {
        <button mat-button routerLink="/users">
          <mat-icon>people</mat-icon> Usuarios
        </button>
      }
      <button mat-icon-button [matMenuTriggerFor]="menu">
        <mat-icon>account_circle</mat-icon>
      </button>
      <mat-menu #menu="matMenu">
        <p style="padding: 0 16px; color: #666;">{{ authService.username() }}</p>
        <button mat-menu-item (click)="authService.logout()">
          <mat-icon>exit_to_app</mat-icon> Cerrar Sesión
        </button>
      </mat-menu>
    </mat-toolbar>

    <div class="container">
      <h2>Bienvenido, {{ authService.username() }}</h2>
      <p>Roles: {{ rolesText() }}</p>

      <div class="cards">
        <mat-card class="dash-card" routerLink="/products">
          <mat-icon>inventory_2</mat-icon>
          <h3>Productos</h3>
          <p>Gestión de catálogo e inventario</p>
        </mat-card>

        @if (authService.isAdmin()) {
          <mat-card class="dash-card" routerLink="/users">
            <mat-icon>group</mat-icon>
            <h3>Usuarios</h3>
            <p>Administración de usuarios y roles</p>
          </mat-card>
        }
      </div>
    </div>
  `,
  styles: [`
    .spacer { flex: 1 1 auto; }
    .container { padding: 24px; }
    .cards { display: flex; gap: 16px; margin-top: 24px; }
    .dash-card {
      width: 250px; padding: 24px; cursor: pointer; text-align: center;
      transition: transform 0.2s;
    }
    .dash-card:hover { transform: translateY(-4px); }
    .dash-card mat-icon { font-size: 48px; width: 48px; height: 48px; color: #1a237e; }
  `]
})
export class DashboardComponent {
  authService = inject(AuthService);
  rolesText = computed(() => this.authService.roles().join(', '));
}
