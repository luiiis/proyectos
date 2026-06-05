# Módulo 16: HttpClient - Comunicación con APIs

## 1. Configuración

```typescript
// app.config.ts
export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(
      withInterceptors([authInterceptor, errorInterceptor])
    ),
  ],
};
```

## 2. Métodos HTTP (CRUD)

```typescript
@Injectable({ providedIn: 'root' })
export class ProductoService {
  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl + '/productos';

  // GET - Listar
  getAll(params?: { page: number; size: number }): Observable<PaginatedResponse<Producto>> {
    return this.http.get<PaginatedResponse<Producto>>(this.apiUrl, {
      params: { page: params?.page ?? 0, size: params?.size ?? 10 }
    });
  }

  // GET - Obtener uno
  getById(id: number): Observable<Producto> {
    return this.http.get<Producto>(`${this.apiUrl}/${id}`);
  }

  // POST - Crear
  crear(producto: ProductoCrear): Observable<Producto> {
    return this.http.post<Producto>(this.apiUrl, producto);
  }

  // PUT - Actualizar
  actualizar(id: number, producto: Partial<Producto>): Observable<Producto> {
    return this.http.put<Producto>(`${this.apiUrl}/${id}`, producto);
  }

  // DELETE - Eliminar
  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
```

## 3. Interceptors (Angular 17+ funcional)

```typescript
// auth.interceptor.ts
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  if (token) {
    req = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
  }

  return next(req);
};

// error.interceptor.ts
export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const snackBar = inject(MatSnackBar);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      switch (error.status) {
        case 401:
          router.navigate(['/login']);
          break;
        case 403:
          snackBar.open('Sin permisos', 'Cerrar', { duration: 3000 });
          break;
        case 500:
          snackBar.open('Error del servidor', 'Cerrar', { duration: 5000 });
          break;
      }
      return throwError(() => error);
    })
  );
};
```

## 4. Manejo de Errores

```typescript
// En el componente
guardarProducto(producto: ProductoCrear) {
  this.loading.set(true);
  this.productoService.crear(producto).pipe(
    finalize(() => this.loading.set(false))
  ).subscribe({
    next: (creado) => {
      this.snackBar.open('Producto creado', 'OK', { duration: 3000 });
      this.router.navigate(['/productos', creado.id]);
    },
    error: (err: HttpErrorResponse) => {
      if (err.status === 400) {
        this.erroresValidacion.set(err.error.errors);
      }
    }
  });
}
```

## 5. Ejercicios

1. Crea un servicio CRUD completo para una entidad con tipado genérico `BaseCrudService<T>`.
2. Implementa un interceptor de autenticación que agregue el token JWT.
3. Crea un interceptor de errores que maneje 401, 403, 404 y 500.
4. Implementa paginación con HttpParams (page, size, sort).
5. Crea un servicio que use `retry(3)` y `catchError` para manejar fallos de red.

---

## Siguiente Módulo
→ [17-Routing](../17-routing/README.md)
