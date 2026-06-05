import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, HttpClientModule],
  template: `
    <div style="display:flex;justify-content:center;align-items:center;min-height:100vh;background:linear-gradient(135deg,#1a237e,#4a148c);">
      <div style="background:white;padding:40px;border-radius:12px;width:380px;box-shadow:0 8px 32px rgba(0,0,0,0.3);">
        <h2 style="text-align:center;color:#1a237e;">🏢 ERP Empresarial</h2>
        <div style="margin-top:20px;">
          <label>Usuario</label>
          <input [(ngModel)]="username" style="width:100%;padding:10px;margin:8px 0 16px;border:1px solid #ccc;border-radius:6px;">
          <label>Contraseña</label>
          <input [(ngModel)]="password" type="password" style="width:100%;padding:10px;margin:8px 0 16px;border:1px solid #ccc;border-radius:6px;" (keyup.enter)="login()">
          @if (error()) { <p style="color:red;font-size:14px;">{{error()}}</p> }
          <button (click)="login()" style="width:100%;padding:12px;background:#1a237e;color:white;border:none;border-radius:6px;cursor:pointer;font-size:16px;" [disabled]="loading()">
            {{ loading() ? 'Cargando...' : 'Iniciar Sesión' }}
          </button>
        </div>
      </div>
    </div>
  `
})
export class LoginComponent {
  private auth = inject(AuthService);
  private router = inject(Router);
  username = ''; password = '';
  loading = signal(false); error = signal('');

  login() {
    this.loading.set(true); this.error.set('');
    this.auth.login(this.username, this.password).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: () => { this.loading.set(false); this.error.set('Credenciales inválidas'); }
    });
  }
}
