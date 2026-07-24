import { Injectable, inject, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
  username: string;
  roles: string[];
}

export interface UsuarioActual {
  id: number;
  username: string;
  email: string;
  nombre: string;
  roles: { nombre: string }[];
}

/**
 * AuthService: maneja login, logout, tokens y estado de sesión.
 *
 * - Guarda tokens en localStorage
 * - Expone signals reactivos para el estado de autenticación
 * - Maneja renovación de token con refresh token
 */
@Injectable({ providedIn: 'root' })
export class AuthService {

  private http = inject(HttpClient);
  private router = inject(Router);
  private baseUrl = `${environment.apiUrl}/api/auth`;

  // Estado reactivo
  private _isAuthenticated = signal(this.hasToken());
  private _username = signal(this.getStoredUsername());
  private _roles = signal<string[]>(this.getStoredRoles());

  // Signals públicos (solo lectura)
  isAuthenticated = this._isAuthenticated.asReadonly();
  username = this._username.asReadonly();
  roles = this._roles.asReadonly();
  isAdmin = computed(() => this._roles().includes('ROLE_ADMIN'));

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/login`, request).pipe(
      tap(response => {
        localStorage.setItem('accessToken', response.accessToken);
        localStorage.setItem('refreshToken', response.refreshToken);
        localStorage.setItem('username', response.username);
        localStorage.setItem('roles', JSON.stringify(response.roles));
        this._isAuthenticated.set(true);
        this._username.set(response.username);
        this._roles.set(response.roles);
      })
    );
  }

  logout(): void {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('username');
    localStorage.removeItem('roles');
    this._isAuthenticated.set(false);
    this._username.set('');
    this._roles.set([]);
    this.router.navigate(['/login']);
  }

  refresh(): Observable<LoginResponse> {
    const refreshToken = localStorage.getItem('refreshToken');
    return this.http.post<LoginResponse>(`${this.baseUrl}/refresh`, { refreshToken }).pipe(
      tap(response => {
        localStorage.setItem('accessToken', response.accessToken);
        this._isAuthenticated.set(true);
      })
    );
  }

  getToken(): string | null {
    return localStorage.getItem('accessToken');
  }

  hasRole(role: string): boolean {
    return this._roles().includes(role);
  }

  private hasToken(): boolean {
    return !!localStorage.getItem('accessToken');
  }

  private getStoredUsername(): string {
    return localStorage.getItem('username') || '';
  }

  private getStoredRoles(): string[] {
    const roles = localStorage.getItem('roles');
    return roles ? JSON.parse(roles) : [];
  }
}
