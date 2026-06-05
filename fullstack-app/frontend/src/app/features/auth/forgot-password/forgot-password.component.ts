import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MailService } from '@app/core/services/mail.service';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  template: `
    <div class="forgot-container">
      <mat-card class="forgot-card">
        <mat-card-header>
          <mat-card-title>Recuperar Contraseña</mat-card-title>
        </mat-card-header>
        <mat-card-content>
          @if (!emailSent) {
            <p>Ingresa tu correo electrónico y te enviaremos un enlace para restablecer tu contraseña.</p>
            <form [formGroup]="form" (ngSubmit)="onSubmit()">
              <mat-form-field class="full-width" appearance="outline">
                <mat-label>Email</mat-label>
                <input matInput formControlName="email" type="email">
              </mat-form-field>

              @if (errorMessage) {
                <p class="error-message">{{ errorMessage }}</p>
              }

              <button mat-raised-button color="primary" class="full-width" type="submit"
                      [disabled]="form.invalid || loading">
                Enviar enlace de recuperación
              </button>
            </form>
          } @else {
            <p class="success-message">
              Se ha enviado un correo de recuperación a tu email. Revisa tu bandeja de entrada.
            </p>
          }

          <div class="links">
            <a routerLink="/login">Volver al inicio de sesión</a>
          </div>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .forgot-container {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }
    .forgot-card { width: 400px; padding: 24px; }
    .full-width { width: 100%; margin-bottom: 12px; }
    .links { text-align: center; margin-top: 16px; }
    .links a { color: #667eea; text-decoration: none; }
  `]
})
export class ForgotPasswordComponent {
  form: FormGroup;
  loading = false;
  emailSent = false;
  errorMessage = '';

  constructor(private fb: FormBuilder, private mailService: MailService) {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;

    this.loading = true;
    this.mailService.requestPasswordReset(this.form.value.email).subscribe({
      next: () => {
        this.emailSent = true;
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.error?.message || 'Error al enviar correo';
      }
    });
  }
}
