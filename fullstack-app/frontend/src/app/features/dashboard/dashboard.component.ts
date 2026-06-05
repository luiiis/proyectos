import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatMenuModule } from '@angular/material/menu';
import { AuthService } from '@app/core/services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatMenuModule
  ],
  template: `
    <mat-toolbar color="primary">
      <span>Sistema de Gestión</span>
      <span class="spacer"></span>
      <button mat-button routerLink="/dashboard">
        <mat-icon>dashboard</mat-icon> Dashboard
      </button>
      <button mat-button routerLink="/users">
        <mat-icon>people</mat-icon> Usuarios
      </button>
      <button mat-button routerLink="/products">
        <mat-icon>inventory</mat-icon> Productos
      </button>
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
      <h2>Bienvenido, {{ currentUser?.username }}</h2>

      <div class="card-container">
        <mat-card class="dashboard-card" routerLink="/users">
          <mat-card-header>
            <mat-icon mat-card-avatar>people</mat-icon>
            <mat-card-title>Usuarios</mat-card-title>
            <mat-card-subtitle>Gestión de usuarios y roles</mat-card-subtitle>
          </mat-card-header>
        </mat-card>

        <mat-card class="dashboard-card" routerLink="/products">
          <mat-card-header>
            <mat-icon mat-card-avatar>inventory</mat-icon>
            <mat-card-title>Productos</mat-card-title>
            <mat-card-subtitle>Catálogo e inventario</mat-card-subtitle>
          </mat-card-header>
        </mat-card>
      </div>
    </div>
  `,
  styles: [`
    .spacer { flex: 1 1 auto; }
    .container { padding: 24px; }
    .dashboard-card {
      width: 280px;
      cursor: pointer;
      transition: transform 0.2s;
    }
    .dashboard-card:hover { transform: translateY(-4px); }
    .card-container { display: flex; gap: 16px; flex-wrap: wrap; margin-top: 24px; }
  `]
})
export class DashboardComponent {
  currentUser: any;

  constructor(private authService: AuthService, private router: Router) {
    this.authService.currentUser$.subscribe(user => this.currentUser = user);
  }

  logout(): void {
    this.authService.logout();
  }
}
