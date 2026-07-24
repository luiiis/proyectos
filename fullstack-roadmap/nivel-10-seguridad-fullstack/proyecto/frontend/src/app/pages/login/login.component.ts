import { Component, signal, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule],
  template: `
    <div style="max-width: 400px; margin: 4rem auto; padding: 2rem; border: 1px solid #ddd; border-radius: 8px;">
      <h2 style="text-align: center;">🔐 Iniciar Sesión</h2>

      @if (error()) {
        <div style="background: #f8d7da; color: #721c24; padding: 0.75rem; border-radius: 4px; margin-bottom: 1rem;">
          {{ error() }}
        </div>
      }

      <form [formGroup]="form" (ngSubmit)="login()">
        <div style="margin-bottom: 1rem;">
          <label>Usuario:</label>
          <input formControlName="username" style="width: 100%; padding: 0.5rem;" placeholder="admin" />
        </div>
        <div style="margin-bottom: 1rem;">
          <label>Contraseña:</label>
          <input formControlName="password" type="password" style="width: 100%; padding: 0.5rem;" placeholder="Admin123!" />
        </div>
        <button type="submit" [disabled]="form.invalid || cargando()"
                style="width: 100%; padding: 0.75rem; background: #007bff; color: white; border: none; border-radius: 4px; font-size: 1rem; cursor: pointer;">
          @if (cargando()) { Ingresando... } @else { Ingresar }
        </button>
      </form>

      <p style="text-align: center; margin-top: 1rem; color: #666; font-size: 0.9rem;">
        Usuarios de prueba: admin / supervisor / cajero (password: Admin123!)
      </p>
    </div>
  `
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  form = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });

  error = signal('');
  cargando = signal(false);

  login() {
    if (this.form.invalid) return;

    this.cargando.set(true);
    this.error.set('');

    this.authService.login(this.form.value as any).subscribe({
      next: () => {
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.error.set(err.error?.error || 'Credenciales inválidas');
        this.cargando.set(false);
      }
    });
  }
}
