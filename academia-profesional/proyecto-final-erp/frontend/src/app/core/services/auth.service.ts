import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs';

interface AuthResponse { token: string; username: string; rol: string; }

@Injectable({ providedIn: 'root' })
export class AuthService {
  private api = '/api/auth';
  private _user = signal<AuthResponse | null>(JSON.parse(localStorage.getItem('erp_user') || 'null'));

  user = this._user.asReadonly();
  isAuthenticated = computed(() => !!this._user());
  isAdmin = computed(() => this._user()?.rol?.includes('ADMIN') ?? false);

  constructor(private http: HttpClient, private router: Router) {}

  login(username: string, password: string) {
    return this.http.post<AuthResponse>(`${this.api}/login`, { username, password })
      .pipe(tap(r => { localStorage.setItem('erp_user', JSON.stringify(r)); this._user.set(r); }));
  }

  register(data: any) {
    return this.http.post<AuthResponse>(`${this.api}/register`, data)
      .pipe(tap(r => { localStorage.setItem('erp_user', JSON.stringify(r)); this._user.set(r); }));
  }

  logout() { localStorage.removeItem('erp_user'); this._user.set(null); this.router.navigate(['/login']); }
  getToken() { return this._user()?.token ?? null; }
}
