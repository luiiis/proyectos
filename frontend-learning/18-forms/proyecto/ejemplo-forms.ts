// Angular Reactive Forms - Reference File
import { Component, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';

// --- Custom Validator ---
function noWhitespace(control: AbstractControl): ValidationErrors | null {
  if (control.value && control.value.trim().length === 0) {
    return { whitespace: true };
  }
  return null;
}

@Component({
  selector: 'app-form-demo',
  standalone: true,
  imports: [ReactiveFormsModule],
  template: `
    <form [formGroup]="form" (ngSubmit)="onSubmit()">
      <input formControlName="name" placeholder="Name">
      <div *ngIf="form.get('name')?.errors?.['required']">Name is required</div>
      <div *ngIf="form.get('name')?.errors?.['whitespace']">No empty spaces</div>

      <input formControlName="email" placeholder="Email" type="email">
      <div *ngIf="form.get('email')?.errors?.['email']">Invalid email</div>

      <input formControlName="age" placeholder="Age" type="number">
      <div *ngIf="form.get('age')?.errors?.['min']">Must be 18+</div>

      <button type="submit" [disabled]="form.invalid">Submit</button>
    </form>
  `
})
export class FormDemoComponent {
  private fb = inject(FormBuilder);

  form: FormGroup = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(3), noWhitespace]],
    email: ['', [Validators.required, Validators.email]],
    age: [null, [Validators.required, Validators.min(18), Validators.max(120)]]
  });

  onSubmit() {
    if (this.form.valid) {
      console.log('Form value:', this.form.value);
    }
  }
}

console.log('Reference file: ejemplo-forms.ts - Reactive Forms & validators for use in an Angular project');
