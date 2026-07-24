import { Component, signal, computed } from '@angular/core';
import { FormsModule } from '@angular/forms';

interface Tarea {
  id: number;
  titulo: string;
  completada: boolean;
}

/**
 * Mini-App 2: Lista de Tareas con CRUD.
 * Aprende: signal<T[]>(), computed(), @for, @if, ngModel, eventos.
 */
@Component({
  selector: 'app-tareas',
  standalone: true,
  imports: [FormsModule],
  template: `
    <h2>✅ Lista de Tareas</h2>
    <p>{{ pendientes() }} pendientes de {{ tareas().length }} total</p>

    <div style="display: flex; gap: 0.5rem; margin-bottom: 1rem;">
      <input
        [(ngModel)]="nuevaTarea"
        (keyup.enter)="agregar()"
        placeholder="Nueva tarea..."
        style="flex: 1; padding: 0.5rem;"
      />
      <button (click)="agregar()">Agregar</button>
    </div>

    @if (tareas().length === 0) {
      <p style="color: #999;">No hay tareas. ¡Agrega una!</p>
    }

    <ul style="list-style: none; padding: 0;">
      @for (tarea of tareas(); track tarea.id) {
        <li style="padding: 0.5rem; border-bottom: 1px solid #eee; display: flex; align-items: center; gap: 0.5rem;">
          <input
            type="checkbox"
            [checked]="tarea.completada"
            (change)="toggle(tarea.id)"
          />
          <span [style.text-decoration]="tarea.completada ? 'line-through' : 'none'"
                [style.color]="tarea.completada ? '#999' : '#333'">
            {{ tarea.titulo }}
          </span>
          <button (click)="eliminar(tarea.id)" style="margin-left: auto;">❌</button>
        </li>
      }
    </ul>
  `
})
export class TareasComponent {
  tareas = signal<Tarea[]>([]);
  nuevaTarea = '';
  private nextId = 1;

  pendientes = computed(() => this.tareas().filter(t => !t.completada).length);

  agregar() {
    if (!this.nuevaTarea.trim()) return;
    this.tareas.update(list => [
      ...list,
      { id: this.nextId++, titulo: this.nuevaTarea.trim(), completada: false }
    ]);
    this.nuevaTarea = '';
  }

  toggle(id: number) {
    this.tareas.update(list =>
      list.map(t => t.id === id ? { ...t, completada: !t.completada } : t)
    );
  }

  eliminar(id: number) {
    this.tareas.update(list => list.filter(t => t.id !== id));
  }
}
