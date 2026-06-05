import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MailService } from '@app/core/services/mail.service';

@Component({
  selector: 'app-reset-password',
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
    <div class="reset-container">
      <mat-card class="reset-card">
        <mat-card-header>
          <mat-card-title>Nueva Contraseña</mat-card-title>
        </mat-card-header>
        <mat-card-content>
          @if (!resetSuccess) {
            <form [formGroup]="form" (ngSubmit)="onSubmit()">
              <mat-form-field class="full-width" appearance="outline">
                <mat-label>Nueva contraseña</mat-label>
                <input matInput type="password" formControlName="password">
              </mat-form-field>

              <mat-form-field class="full-width" appearance="outline">
                <mat-label>Confirmar contraseña</mat-label>
                <input matInput type="password" formControlName="confirmPassword">
              </mat-form-field>

              @if (errorMessage) {
                <p class="error-message">{{ errorMessage }}</p>
              }

              <button mat-raised-button color="primary" class="full-width" type="submit"
                      [disabled]="form.invalid || loading">
                Restablecer Contraseña
              </button>
            </form>
          } @else {
            <p class="success-message">Contraseña restablecida exitosamente.</p>
            <a routerLink="/login" mat-raised-button color="primary">Ir al Login</a>
          }
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .reset-container {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }
    .reset-card { width: 400px; padding: 24px; }
    .full-width { width: 100%; margin-bottom: 12px; }
  `]
})
export class ResetPasswordComponent implements OnInit {
  form: FormGroup;
  token = '';
  loading = false;
  resetSuccess = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private mailService: MailService
  ) {
    this.form = this.fb.group({
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParams['token'] || '';
    if (!this.token) {
      this.router.navigate(['/login']);
    }
  }

  onSubmit(): void {
    if (this.form.value.password !== this.form.value.confirmPassword) {
      this.errorMessage = 'Las contraseñas no coinciden';
      return;
    }

    this.loading = true;
    this.mailService.confirmPasswordReset(this.token, this.form.value.password).subscribe({
      next: () => {
        this.resetSuccess = true;
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.error?.message || 'Error al restablecer contraseña';
      }
    });
  }
}
