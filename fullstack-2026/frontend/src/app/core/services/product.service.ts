import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

/**
 * Servicio de productos - Angular 21.
 *
 * DIFERENCIA con tu proyecto anterior:
 * - Mismo patrón de servicio HTTP, pero ahora los componentes
 *   usan Signals + resource() en lugar de subscribe() manual.
 * - El servicio sigue siendo el mismo (HttpClient + Observable).
 * - Lo que cambia es CÓMO los componentes consumen los datos.
 */

// ═══════ Interfaces (contratos de datos) ═══════
export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  stock: number;
  category: string;
  sku: string;
  imageUrl: string;
  active: boolean;
  createdAt: string;
}

export interface ApiResult<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
}

export interface CreateProduct {
  name: string;
  description?: string;
  price: number;
  stock: number;
  category?: string;
  sku?: string;
  imageUrl?: string;
}

@Injectable({ providedIn: 'root' })
export class ProductService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/products`;

  getAll(): Observable<ApiResult<Product[]>> {
    return this.http.get<ApiResult<Product[]>>(this.apiUrl);
  }

  getById(id: number): Observable<ApiResult<Product>> {
    return this.http.get<ApiResult<Product>>(`${this.apiUrl}/${id}`);
  }

  search(query: string): Observable<ApiResult<Product[]>> {
    return this.http.get<ApiResult<Product[]>>(`${this.apiUrl}/search?q=${query}`);
  }

  getCategories(): Observable<ApiResult<string[]>> {
    return this.http.get<ApiResult<string[]>>(`${this.apiUrl}/categories`);
  }

  create(product: CreateProduct): Observable<ApiResult<Product>> {
    return this.http.post<ApiResult<Product>>(this.apiUrl, product);
  }

  update(id: number, product: Partial<Product>): Observable<ApiResult<Product>> {
    return this.http.put<ApiResult<Product>>(`${this.apiUrl}/${id}`, product);
  }

  delete(id: number): Observable<ApiResult<void>> {
    return this.http.delete<ApiResult<void>>(`${this.apiUrl}/${id}`);
  }

  addStock(id: number, quantity: number, reason: string): Observable<ApiResult<Product>> {
    return this.http.post<ApiResult<Product>>(`${this.apiUrl}/${id}/stock/add`, { quantity, reason });
  }

  removeStock(id: number, quantity: number, reason: string): Observable<ApiResult<Product>> {
    return this.http.post<ApiResult<Product>>(`${this.apiUrl}/${id}/stock/remove`, { quantity, reason });
  }
}
