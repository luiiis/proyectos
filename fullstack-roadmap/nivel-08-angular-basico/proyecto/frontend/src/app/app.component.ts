import { Component } from '@angular/core';
import { RouterOutlet, RouterLink } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink],
  template: `
    <nav style="background: #333; padding: 1rem; display: flex; gap: 1rem;">
      <a routerLink="/contador" style="color: white; text-decoration: none;">Contador</a>
      <a routerLink="/tareas" style="color: white; text-decoration: none;">Tareas</a>
      <a routerLink="/calculadora" style="color: white; text-decoration: none;">Calculadora</a>
      <a routerLink="/productos" style="color: white; text-decoration: none;">Productos</a>
    </nav>
    <main style="padding: 2rem;">
      <router-outlet />
    </main>
  `
})
export class AppComponent {
  title = 'Angular Básico';
}
