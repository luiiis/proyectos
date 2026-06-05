// Angular HttpClient - Reference File
import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, retry, throwError } from 'rxjs';

// --- Interceptor (functional style, Angular 17+) ---
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token');
  const cloned = req.clone({
    setHeaders: { Authorization: `Bearer ${token}` }
  });
  return next(cloned);
};

export const loggingInterceptor: HttpInterceptorFn = (req, next) => {
  console.log(`[HTTP] ${req.method} ${req.url}`);
  return next(req);
};

// --- Service with CRUD operations ---
@Injectable({ providedIn: 'root' })
export class ApiService {
  private http = inject(HttpClient);
  private baseUrl = '/api/items';

  getAll(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl).pipe(
      retry(2),
      catchError(this.handleError)
    );
  }

  getById(id: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/${id}`).pipe(catchError(this.handleError));
  }

  create(data: any): Observable<any> {
    return this.http.post<any>(this.baseUrl, data).pipe(catchError(this.handleError));
  }

  update(id: number, data: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/${id}`, data).pipe(catchError(this.handleError));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`).pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    const message = error.error?.message || error.statusText;
    console.error(`API Error [${error.status}]: ${message}`);
    return throwError(() => new Error(message));
  }
}

console.log('Reference file: ejemplo-http.ts - HttpClient CRUD & interceptors for use in an Angular project');
