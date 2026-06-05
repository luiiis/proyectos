import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Producto {
  id: number; nombre: string; descripcion: string; precio: number;
  costo: number; stock: number; sku: string; categoriaId: number; activo: boolean;
}

@Injectable({ providedIn: 'root' })
export class ProductoService {
  private http = inject(HttpClient);
  private api = '/api/productos';

  listar(page = 0, size = 20): Observable<any> { return this.http.get(`${this.api}?page=${page}&size=${size}`); }
  buscar(q: string): Observable<Producto[]> { return this.http.get<Producto[]>(`${this.api}/buscar?q=${q}`); }
  stockBajo(min = 10): Observable<Producto[]> { return this.http.get<Producto[]>(`${this.api}/stock-bajo?min=${min}`); }
  crear(p: Partial<Producto>): Observable<Producto> { return this.http.post<Producto>(this.api, p); }
  actualizar(id: number, p: Partial<Producto>): Observable<Producto> { return this.http.put<Producto>(`${this.api}/${id}`, p); }
  eliminar(id: number): Observable<void> { return this.http.delete<void>(`${this.api}/${id}`); }
}
