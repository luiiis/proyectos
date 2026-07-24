import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: '/contador', pathMatch: 'full' },
  {
    path: 'contador',
    loadComponent: () => import('./features/contador/contador.component').then(m => m.ContadorComponent)
  },
  {
    path: 'tareas',
    loadComponent: () => import('./features/tareas/tareas.component').then(m => m.TareasComponent)
  },
  {
    path: 'calculadora',
    loadComponent: () => import('./features/calculadora/calculadora.component').then(m => m.CalculadoraComponent)
  },
  {
    path: 'productos',
    loadComponent: () => import('./features/producto-form/producto-form.component').then(m => m.ProductoFormComponent)
  }
];
