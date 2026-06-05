// Angular Data Binding - Reference File
import { Component, model, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-binding-demo',
  standalone: true,
  imports: [FormsModule],
  template: `
    <!-- 1. Interpolation -->
    <h1>{{ title }}</h1>
    <p>Counter: {{ counter() }}</p>

    <!-- 2. Property Binding -->
    <img [src]="imageUrl" [alt]="title">
    <button [disabled]="isDisabled">Click</button>

    <!-- 3. Event Binding -->
    <button (click)="increment()">+1</button>
    <input (keyup.enter)="onEnter($event)">

    <!-- 4. Two-way Binding with ngModel -->
    <input [(ngModel)]="username">
    <p>Hello, {{ username }}</p>

    <!-- 5. Signal-based Two-way with model() -->
    <app-child [(value)]="counter" />
  `
})
export class BindingDemoComponent {
  title = 'Data Binding Demo';
  imageUrl = 'https://angular.io/assets/images/logos/angular/angular.svg';
  isDisabled = false;
  username = '';

  // Signal-based state
  counter = signal(0);
  // model() for two-way binding in child components
  value = model(0);

  increment() { this.counter.update(v => v + 1); }
  onEnter(event: Event) { console.log('Enter pressed', event); }
}

console.log('Reference file: ejemplo-binding.ts - Data Binding patterns for use in an Angular project');
