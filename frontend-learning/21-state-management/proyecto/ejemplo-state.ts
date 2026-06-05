// Angular State Management - Reference File
import { Component, signal, computed, effect, Injectable } from '@angular/core';
import { createAction, createReducer, on, props, Store, createSelector, createFeature } from '@ngrx/store';

// ===== 1. Signals (Angular 17+ built-in) =====
@Component({
  selector: 'app-signal-demo',
  standalone: true,
  template: `
    <p>Count: {{ count() }}</p>
    <p>Double: {{ double() }}</p>
    <button (click)="increment()">+1</button>
  `
})
export class SignalDemoComponent {
  count = signal(0);
  double = computed(() => this.count() * 2);

  constructor() {
    effect(() => console.log('Count changed:', this.count()));
  }

  increment() { this.count.update(v => v + 1); }
}

// ===== 2. NgRx Store Pattern =====
// Actions
export const loadItems = createAction('[Items] Load');
export const loadItemsSuccess = createAction('[Items] Load Success', props<{ items: string[] }>());

// Reducer
export interface ItemsState { items: string[]; loading: boolean; }
const initialState: ItemsState = { items: [], loading: false };

export const itemsFeature = createFeature({
  name: 'items',
  reducer: createReducer(
    initialState,
    on(loadItems, state => ({ ...state, loading: true })),
    on(loadItemsSuccess, (state, { items }) => ({ ...state, items, loading: false }))
  )
});

// Selectors
export const { selectItems, selectLoading } = itemsFeature;

// Usage in component
@Component({ selector: 'app-ngrx-demo', standalone: true, template: '' })
export class NgrxDemoComponent {
  private store = new Store(); // inject(Store) in real app
  items$ = this.store.select(selectItems);
  dispatch() { this.store.dispatch(loadItems()); }
}

console.log('Reference file: ejemplo-state.ts - Signals & NgRx state management for use in an Angular project');
