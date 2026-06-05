import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs';

interface AuthResponse { token: string; username: string; rol: string; }

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';
  private _user = signal<AuthResponse | null>(this.getStored());

  user = this._user.asReadonly();
  isAuthenticated = computed(() => !!this._user());
  isAdmin = computed(() => this._user()?.rol === 'ADMIN');

  constructor(private http: HttpClient, private router: Router) {}

  login(username: string, password: string) {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, { username, password })
      .pipe(tap(res => { localStorage.setItem('auth', JSON.stringify(res)); this._user.set(res); }));
  }

  logout() {
    localStorage.removeItem('auth');
    this._user.set(null);
    this.router.navigate(['/login']);
  }

  getToken(): string | null { return this._user()?.token ?? null; }

  private getStored(): AuthResponse | null {
    const s = localStorage.getItem('auth');
    return s ? JSON.parse(s) : null;
  }
}
