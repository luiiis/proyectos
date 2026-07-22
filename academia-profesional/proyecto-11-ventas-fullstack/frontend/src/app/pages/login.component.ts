import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, InputTextModule, ButtonModule, CardModule],
  template: `
    <div style="display:flex;justify-content:center;align-items:center;min-height:100vh;background:linear-gradient(135deg,#1e3a5f,#2d5f8a);">
      <p-card header="Sistema de Ventas" [style]="{width:'380px'}">
        <div style="display:flex;flex-direction:column;gap:12px;">
          <span class="p-float-label">
            <input pInputText id="user" [(ngModel)]="username" style="width:100%">
            <label for="user">Usuario</label>
          </span>
          <span class="p-float-label">
            <input pInputText id="pass" type="password" [(ngModel)]="password" (keyup.enter)="login()" style="width:100%">
            <label for="pass">Contraseña</label>
          </span>
          @if (error()) { <small style="color:red">{{ error() }}</small> }
          <button pButton label="Iniciar Sesión" (click)="login()" [loading]="loading()" style="width:100%"></button>
          <small style="color:#666">Demo: admin / admin123</small>
        </div>
      </p-card>
    </div>
  `
})
export class LoginComponent {
  private auth = inject(AuthService);
  private router = inject(Router);
  username = ''; password = '';
  loading = signal(false);
  error = signal('');

  login() {
    this.loading.set(true); this.error.set('');
    this.auth.login(this.username, this.password).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: () => { this.loading.set(false); this.error.set('Credenciales inválidas'); }
    });
  }
}
