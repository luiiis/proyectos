import { bootstrapApplication } from '@angular/platform-browser';
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { Routes } from '@angular/router';
import { authInterceptor } from './app/interceptors/auth.interceptor';
import { authGuard } from './app/guards/auth.guard';

const routes: Routes = [
  { path: 'login', loadComponent: () => import('./app/pages/login.component').then(m => m.LoginComponent) },
  {
    path: '',
    canActivate: [authGuard],
    children: [
      { path: 'dashboard', loadComponent: () => import('./app/pages/dashboard.component').then(m => m.DashboardComponent) },
      { path: 'productos', loadComponent: () => import('./app/pages/productos.component').then(m => m.ProductosComponent) },
      { path: 'ventas', loadComponent: () => import('./app/pages/ventas.component').then(m => m.VentasComponent) },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  { path: '**', redirectTo: 'login' }
];

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  template: `<router-outlet />`
})
export class AppComponent {}

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([authInterceptor])),
    provideAnimationsAsync()
  ]
}).catch(err => console.error(err));
