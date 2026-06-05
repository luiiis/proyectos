import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, MatToolbarModule, MatButtonModule, MatIconModule],
  template: `
    <mat-toolbar color="primary">
      <mat-icon>store</mat-icon>
      <span style="margin-left: 8px">Sistema Empresarial</span>
      <span style="flex: 1"></span>
      <button mat-button routerLink="/productos">Productos</button>
      <button mat-button routerLink="/reportes">Reportes</button>
    </mat-toolbar>
    <main style="padding: 24px; max-width: 1200px; margin: 0 auto;">
      <router-outlet />
    </main>
  `
})
export class AppComponent {}
