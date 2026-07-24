import { Component, signal } from '@angular/core';

/**
 * Mini-App 1: Contador con Signals.
 * Aprende: signal(), .set(), .update(), template bindings.
 */
@Component({
  selector: 'app-contador',
  standalone: true,
  template: `
    <h2>🔢 Contador</h2>
    <p style="font-size: 3rem; font-weight: bold;">{{ count() }}</p>
    <div style="display: flex; gap: 0.5rem;">
      <button (click)="decrementar()">➖ Restar</button>
      <button (click)="reset()">🔄 Reset</button>
      <button (click)="incrementar()">➕ Sumar</button>
    </div>
    <p style="margin-top: 1rem; color: #666;">
      Tecnología: Angular Signals (signal, update, set)
    </p>
  `
})
export class ContadorComponent {
  count = signal(0);

  incrementar() {
    this.count.update(n => n + 1);
  }

  decrementar() {
    this.count.update(n => n - 1);
  }

  reset() {
    this.count.set(0);
  }
}
