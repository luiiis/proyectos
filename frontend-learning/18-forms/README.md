# Módulo 18: Formularios en Angular

## 1. Reactive Forms (Preferir siempre)

```typescript
@Component({
  selector: 'app-usuario-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './usuario-form.component.html',
})
export class UsuarioFormComponent implements OnInit {
  private fb = inject(FormBuilder);

  form = this.fb.group({
    nombre: ['', [Validators.required, Validators.minLength(3)]],
    email: ['', [Validators.required, Validators.email]],
    edad: [null as number | null, [Validators.required, Validators.min(18)]],
    rol: ['user', Validators.required],
    direccion: this.fb.group({
      calle: [''],
      ciudad: ['', Validators.required],
      pais: ['', Validators.required],
    }),
  });

  onSubmit() {
    if (this.form.valid) {
      console.log(this.form.getRawValue());
    } else {
      this.form.markAllAsTouched();
    }
  }
}
```

## 2. Template del Formulario

```html
<form [formGroup]="form" (ngSubmit)="onSubmit()">
  <mat-form-field>
    <mat-label>Nombre</mat-label>
    <input matInput formControlName="nombre">
    @if (form.get('nombre')?.hasError('required') && form.get('nombre')?.touched) {
      <mat-error>Nombre es requerido</mat-error>
    }
    @if (form.get('nombre')?.hasError('minlength')) {
      <mat-error>Mínimo 3 caracteres</mat-error>
    }
  </mat-form-field>

  <mat-form-field>
    <mat-label>Email</mat-label>
    <input matInput formControlName="email" type="email">
    @if (form.get('email')?.hasError('email')) {
      <mat-error>Email inválido</mat-error>
    }
  </mat-form-field>

  <!-- Grupo anidado -->
  <div formGroupName="direccion">
    <input matInput formControlName="ciudad" placeholder="Ciudad">
  </div>

  <button mat-raised-button type="submit" [disabled]="form.invalid">
    Guardar
  </button>
</form>
```

## 3. Validadores Personalizados

```typescript
// Validador síncrono
function noEspacios(control: AbstractControl): ValidationErrors | null {
  if (control.value?.includes(' ')) {
    return { noEspacios: true };
  }
  return null;
}

// Validador asíncrono (verifica en servidor)
function emailUnico(usuarioService: UsuarioService): AsyncValidatorFn {
  return (control: AbstractControl): Observable<ValidationErrors | null> => {
    return usuarioService.verificarEmail(control.value).pipe(
      map(existe => existe ? { emailDuplicado: true } : null),
      catchError(() => of(null)),
    );
  };
}

// Uso
email: ['', [Validators.required, Validators.email], [emailUnico(this.usuarioService)]],
```

## 4. FormArray (Campos Dinámicos)

```typescript
form = this.fb.group({
  nombre: ['', Validators.required],
  telefonos: this.fb.array([this.crearTelefono()]),
});

get telefonos() {
  return this.form.get('telefonos') as FormArray;
}

crearTelefono(): FormGroup {
  return this.fb.group({
    tipo: ['celular'],
    numero: ['', [Validators.required, Validators.pattern(/^\d{10}$/)]],
  });
}

agregarTelefono() { this.telefonos.push(this.crearTelefono()); }
eliminarTelefono(i: number) { this.telefonos.removeAt(i); }
```

## 5. Template-Driven (Simple)

```html
<!-- Para formularios simples (login, búsqueda) -->
<form #loginForm="ngForm" (ngSubmit)="onLogin(loginForm.value)">
  <input name="email" ngModel required email>
  <input name="password" ngModel required minlength="8" type="password">
  <button [disabled]="loginForm.invalid">Ingresar</button>
</form>
```

## 6. Ejercicios

1. Crea un formulario reactivo de registro con validaciones (required, email, minLength).
2. Implementa un validador asíncrono que verifique si un email ya existe.
3. Crea un formulario con FormArray para agregar/eliminar teléfonos dinámicamente.
4. Implementa un formulario con grupos anidados (datos personales + dirección).
5. Crea un componente de formulario reutilizable que funcione en modo crear/editar.

---

## Siguiente Módulo
→ [19-Angular Material](../19-angular-material/README.md)
