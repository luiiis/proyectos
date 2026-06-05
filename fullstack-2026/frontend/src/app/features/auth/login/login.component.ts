import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../../../core/services/auth.service';

/**
 * Componente de Login - Angular 21 con Keycloak.
 *
 * DIFERENCIA FUNDAMENTAL con tu proyecto anterior:
 * - ANTES: Formulario con username/password que envía POST al backend
 * - AHORA: Solo un botón que redirige a Keycloak
 *
 * ¿Por qué?
 * - El formulario de login está EN Keycloak (no en tu app)
 * - Keycloak maneja: validación, MFA, brute force protection, social login
 * - Tu app solo necesita un botón "Iniciar Sesión"
 */
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [MatButtonModule, MatCardModule, MatIconModule],
  template: `
    <div class="login-container">
      <mat-card class="login-card">
        <mat-card-header>
          <mat-card-title>Sistema de Gestión 2026</mat-card-title>
          <mat-card-subtitle>Inicia sesión para continuar</mat-card-subtitle>
        </mat-card-header>
        <mat-card-content>
          <p>La autenticación es gestionada por Keycloak (OAuth2/OIDC).</p>
          <p>Serás redirigido al servidor de autenticación.</p>
        </mat-card-content>
        <mat-card-actions>
          <button mat-raised-button color="primary" (click)="login()">
            <mat-icon>login</mat-icon>
            Iniciar Sesión con Keycloak
          </button>
        </mat-card-actions>
      </mat-card>
    </div>
  `,
  styles: [`
    .login-container {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      background: linear-gradient(135deg, #1a237e 0%, #4a148c 100%);
    }
    .login-card {
      width: 420px;
      padding: 32px;
      text-align: center;
    }
    mat-card-actions { justify-content: center; padding: 16px; }
  `]
})
export class LoginComponent {
  private authService = inject(AuthService);

  login(): void {
    this.authService.login(); // Redirige a Keycloak
  }
}
