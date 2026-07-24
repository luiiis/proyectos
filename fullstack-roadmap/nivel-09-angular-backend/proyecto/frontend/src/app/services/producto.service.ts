import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Producto {
  id: number;
  nombre: string;
  descripcion: string;
  precio: number;
  existencia: number;
  categoriaId: number;
  sku: string;
  activo: boolean;
  categoriaNombre: string;
}

export interface ApiResponse<T> {
  success: boolean;
  data: T;
  total?: number;
  message?: string;
}

export interface PaginatedResponse {
  content: Producto[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

/**
 * Servicio HTTP: se comunica con la API de Spring Boot.
 * Usa HttpClient + Observables (RxJS).
 */
@Injectable({ providedIn: 'root' })
export class ProductoService {

  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/api/productos`;

  listarTodos(): Observable<ApiResponse<Producto[]>> {
    return this.http.get<ApiResponse<Producto[]>>(this.baseUrl);
  }

  buscarPorId(id: number): Observable<ApiResponse<Producto>> {
    return this.http.get<ApiResponse<Producto>>(`${this.baseUrl}/${id}`);
  }

  buscarPorNombre(nombre: string): Observable<ApiResponse<Producto[]>> {
    const params = new HttpParams().set('nombre', nombre);
    return this.http.get<ApiResponse<Producto[]>>(`${this.baseUrl}/buscar`, { params });
  }

  buscarPorCategoria(categoriaId: number): Observable<ApiResponse<Producto[]>> {
    return this.http.get<ApiResponse<Producto[]>>(`${this.baseUrl}/categoria/${categoriaId}`);
  }

  listarPaginado(page: number, size: number): Observable<PaginatedResponse> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<PaginatedResponse>(`${this.baseUrl}/paginado`, { params });
  }

  crear(producto: Partial<Producto>): Observable<ApiResponse<Producto>> {
    return this.http.post<ApiResponse<Producto>>(this.baseUrl, producto);
  }

  actualizar(id: number, producto: Partial<Producto>): Observable<ApiResponse<Producto>> {
    return this.http.put<ApiResponse<Producto>>(`${this.baseUrl}/${id}`, producto);
  }

  eliminar(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`);
  }
}
