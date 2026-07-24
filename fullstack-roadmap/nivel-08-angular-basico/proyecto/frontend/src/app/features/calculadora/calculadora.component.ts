import { Component, signal } from '@angular/core';

/**
 * Mini-App 3: Calculadora básica.
 * Aprende: eventos, lógica, signal, @switch.
 */
@Component({
  selector: 'app-calculadora',
  standalone: true,
  template: `
    <h2>🧮 Calculadora</h2>
    <div style="max-width: 300px; background: #222; padding: 1rem; border-radius: 8px;">
      <div style="background: #333; color: #0f0; font-size: 2rem; text-align: right; padding: 1rem; border-radius: 4px; margin-bottom: 1rem; font-family: monospace;">
        {{ display() }}
      </div>
      <div style="display: grid; grid-template-columns: repeat(4, 1fr); gap: 0.5rem;">
        @for (btn of botones; track btn) {
          <button
            (click)="presionar(btn)"
            [style.background]="esOperador(btn) ? '#f90' : '#555'"
            style="padding: 1rem; font-size: 1.2rem; border: none; color: white; border-radius: 4px; cursor: pointer;">
            {{ btn }}
          </button>
        }
      </div>
    </div>
  `
})
export class CalculadoraComponent {
  display = signal('0');
  private operando1 = 0;
  private operador = '';
  private esperandoOperando = false;

  botones = [
    '7', '8', '9', '÷',
    '4', '5', '6', '×',
    '1', '2', '3', '-',
    '0', '.', '=', '+'
  ];

  esOperador(btn: string): boolean {
    return ['÷', '×', '-', '+', '='].includes(btn);
  }

  presionar(btn: string) {
    if (btn >= '0' && btn <= '9' || btn === '.') {
      this.ingresarDigito(btn);
    } else if (btn === '=') {
      this.calcular();
    } else {
      this.setOperador(btn);
    }
  }

  private ingresarDigito(digito: string) {
    if (this.esperandoOperando) {
      this.display.set(digito);
      this.esperandoOperando = false;
    } else {
      this.display.update(d => d === '0' ? digito : d + digito);
    }
  }

  private setOperador(op: string) {
    this.operando1 = parseFloat(this.display());
    this.operador = op;
    this.esperandoOperando = true;
  }

  private calcular() {
    const operando2 = parseFloat(this.display());
    let resultado = 0;

    switch (this.operador) {
      case '+': resultado = this.operando1 + operando2; break;
      case '-': resultado = this.operando1 - operando2; break;
      case '×': resultado = this.operando1 * operando2; break;
      case '÷': resultado = operando2 !== 0 ? this.operando1 / operando2 : 0; break;
    }

    this.display.set(String(Math.round(resultado * 100) / 100));
    this.esperandoOperando = true;
  }
}
