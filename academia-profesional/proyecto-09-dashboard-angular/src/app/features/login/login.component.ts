import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, MatCardModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  template: `
    <div style="display:flex;justify-content:center;align-items:center;min-height:100vh;background:#1a237e;">
      <mat-card style="width:380px;padding:24px;">
        <mat-card-title>Iniciar Sesión</mat-card-title>
        <mat-card-content>
          <mat-form-field class="full" appearance="outline">
            <mat-label>Usuario</mat-label>
            <input matInput [(ngModel)]="username">
          </mat-form-field>
          <mat-form-field class="full" appearance="outline">
            <mat-label>Contraseña</mat-label>
            <input matInput type="password" [(ngModel)]="password" (keyup.enter)="login()">
          </mat-form-field>
          @if (error()) { <p style="color:red">{{ error() }}</p> }
        </mat-card-content>
        <mat-card-actions>
          <button mat-raised-button color="primary" class="full" (click)="login()" [disabled]="loading()">
            {{ loading() ? 'Cargando...' : 'Entrar' }}
          </button>
        </mat-card-actions>
      </mat-card>
    </div>
  `,
  styles: [`.full { width: 100%; margin-bottom: 8px; }`]
})
export class LoginComponent {
  private auth = inject(AuthService);
  private router = inject(Router);

  username = '';
  password = '';
  loading = signal(false);
  error = signal('');

  login() {
    this.loading.set(true);
    this.error.set('');
    this.auth.login(this.username, this.password).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: () => { this.loading.set(false); this.error.set('Credenciales inválidas'); }
    });
  }
}
