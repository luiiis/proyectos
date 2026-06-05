// Angular Routing - Reference File
import { Routes, CanActivateFn, ResolveFn } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';
import { DataService } from './data.service';

// --- Guard (functional style) ---
export const authGuard: CanActivateFn = (route, state) => {
  const auth = inject(AuthService);
  if (auth.isAuthenticated()) return true;
  return inject(import('@angular/router').then(m => m.Router)).createUrlTree(['/login']);
};

// --- Resolver ---
export const userResolver: ResolveFn<any> = (route) => {
  const dataService = inject(DataService);
  return dataService.getUserById(route.paramMap.get('id')!);
};

// --- Routes with lazy loading, guards, and resolvers ---
export const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  {
    path: 'home',
    loadComponent: () => import('./home.component').then(m => m.HomeComponent)
  },
  {
    path: 'dashboard',
    canActivate: [authGuard],
    loadComponent: () => import('./dashboard.component').then(m => m.DashboardComponent),
    children: [
      { path: 'profile', loadComponent: () => import('./profile.component').then(m => m.ProfileComponent) }
    ]
  },
  {
    path: 'user/:id',
    resolve: { user: userResolver },
    loadComponent: () => import('./user-detail.component').then(m => m.UserDetailComponent)
  },
  { path: '**', loadComponent: () => import('./not-found.component').then(m => m.NotFoundComponent) }
];

console.log('Reference file: ejemplo-routing.ts - Routing, guards & resolvers for use in an Angular project');
