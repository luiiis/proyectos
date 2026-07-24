# Cómo Ejecutar — Proyecto 8: Angular desde Cero

## Requisitos
- Node.js 22+ (`node -v`)
- Angular CLI 20 (`ng version`)

## Crear proyecto
```bash
cd nivel-08-angular-basico/proyecto
ng new mi-app --standalone --style=scss --routing
cd mi-app
ng serve
# → http://localhost:4200
```

## Mini-apps que construirás:

### 1. Contador (Signals básicos)
```bash
ng generate component features/contador
```
```typescript
// contador.component.ts
import { Component, signal } from '@angular/core';

@Component({
  selector: 'app-contador',
  standalone: true,
  template: `
    <h2>Contador: {{ count() }}</h2>
    <button (click)="incrementar()">+</button>
    <button (click)="decrementar()">-</button>
    <button (click)="reset()">Reset</button>
  `
})
export class ContadorComponent {
  count = signal(0);
  incrementar() { this.count.update(n => n + 1); }
  decrementar() { this.count.update(n => n - 1); }
  reset() { this.count.set(0); }
}
```

### 2. Lista de Tareas (CRUD con Signals)
```bash
ng generate component features/tareas
```
```typescript
// tareas.component.ts
import { Component, signal, computed } from '@angular/core';
import { FormsModule } from '@angular/forms';

interface Tarea { id: number; titulo: string; completada: boolean; }

@Component({
  selector: 'app-tareas',
  standalone: true,
  imports: [FormsModule],
  template: `
    <h2>Tareas ({{ pendientes() }} pendientes)</h2>
    <input [(ngModel)]="nuevaTarea" (keyup.enter)="agregar()" placeholder="Nueva tarea...">
    <button (click)="agregar()">Agregar</button>
    <ul>
      @for (tarea of tareas(); track tarea.id) {
        <li [style.text-decoration]="tarea.completada ? 'line-through' : 'none'">
          <input type="checkbox" [checked]="tarea.completada" (change)="toggle(tarea.id)">
          {{ tarea.titulo }}
          <button (click)="eliminar(tarea.id)">❌</button>
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
    this.tareas.update(list => [...list, { id: this.nextId++, titulo: this.nuevaTarea, completada: false }]);
    this.nuevaTarea = '';
  }

  toggle(id: number) {
    this.tareas.update(list => list.map(t => t.id === id ? { ...t, completada: !t.completada } : t));
  }

  eliminar(id: number) {
    this.tareas.update(list => list.filter(t => t.id !== id));
  }
}
```

### 3. Formulario de Productos (Reactive Forms)
```bash
ng generate component features/producto-form
```
(Ver README del nivel para implementación completa)

## Comandos Angular esenciales
```bash
ng generate component nombre       # Crear componente
ng generate service nombre         # Crear servicio
ng add @angular/material           # Agregar Material Design
ng build                           # Compilar para producción
ng test                            # Ejecutar tests
```
