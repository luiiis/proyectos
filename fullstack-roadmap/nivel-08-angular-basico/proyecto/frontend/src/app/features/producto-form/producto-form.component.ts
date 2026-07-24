import { Component, signal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';

interface Producto {
  id: number;
  nombre: string;
  precio: number;
  categoria: string;
}

/**
 * Mini-App 4: Formulario de Productos con Reactive Forms.
 * Aprende: FormGroup, Validators, formularios reactivos, validación.
 */
@Component({
  selector: 'app-producto-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  template: `
    <h2>📦 Productos (Formulario Reactivo)</h2>

    <form [formGroup]="form" (ngSubmit)="guardar()" style="max-width: 400px; margin-bottom: 2rem;">
      <div style="margin-bottom: 1rem;">
        <label>Nombre:</label>
        <input formControlName="nombre" style="width: 100%; padding: 0.5rem;" />
        @if (form.get('nombre')?.invalid && form.get('nombre')?.touched) {
          <small style="color: red;">El nombre es obligatorio (mín. 3 caracteres)</small>
        }
      </div>

      <div style="margin-bottom: 1rem;">
        <label>Precio:</label>
        <input formControlName="precio" type="number" style="width: 100%; padding: 0.5rem;" />
        @if (form.get('precio')?.invalid && form.get('precio')?.touched) {
          <small style="color: red;">El precio debe ser mayor a 0</small>
        }
      </div>

      <div style="margin-bottom: 1rem;">
        <label>Categoría:</label>
        <select formControlName="categoria" style="width: 100%; padding: 0.5rem;">
          <option value="">Seleccionar...</option>
          <option value="Electrónica">Electrónica</option>
          <option value="Periféricos">Periféricos</option>
          <option value="Software">Software</option>
          <option value="Mobiliario">Mobiliario</option>
        </select>
        @if (form.get('categoria')?.invalid && form.get('categoria')?.touched) {
          <small style="color: red;">Selecciona una categoría</small>
        }
      </div>

      <button type="submit" [disabled]="form.invalid"
              style="padding: 0.5rem 1rem; background: #007bff; color: white; border: none; border-radius: 4px;">
        Agregar Producto
      </button>
    </form>

    <h3>Productos agregados ({{ productos().length }})</h3>
    <table style="width: 100%; border-collapse: collapse;">
      <thead>
        <tr style="background: #f5f5f5;">
          <th style="padding: 0.5rem; text-align: left;">Nombre</th>
          <th style="padding: 0.5rem; text-align: right;">Precio</th>
          <th style="padding: 0.5rem; text-align: left;">Categoría</th>
          <th style="padding: 0.5rem;">Acciones</th>
        </tr>
      </thead>
      <tbody>
        @for (prod of productos(); track prod.id) {
          <tr style="border-bottom: 1px solid #eee;">
            <td style="padding: 0.5rem;">{{ prod.nombre }}</td>
            <td style="padding: 0.5rem; text-align: right;">\${{ prod.precio.toFixed(2) }}</td>
            <td style="padding: 0.5rem;">{{ prod.categoria }}</td>
            <td style="padding: 0.5rem; text-align: center;">
              <button (click)="eliminar(prod.id)">🗑️</button>
            </td>
          </tr>
        }
      </tbody>
    </table>
  `
})
export class ProductoFormComponent {
  productos = signal<Producto[]>([]);
  form: FormGroup;
  private nextId = 1;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3)]],
      precio: [null, [Validators.required, Validators.min(0.01)]],
      categoria: ['', [Validators.required]]
    });
  }

  guardar() {
    if (this.form.valid) {
      const nuevo: Producto = {
        id: this.nextId++,
        ...this.form.value
      };
      this.productos.update(list => [...list, nuevo]);
      this.form.reset();
    }
  }

  eliminar(id: number) {
    this.productos.update(list => list.filter(p => p.id !== id));
  }
}
