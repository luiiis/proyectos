# Módulo 24: Accesibilidad (a11y)

## 1. WCAG 2.2 - Principios

```
┌─────────────────────────────────────────────────────┐
│  POUR - 4 Principios de Accesibilidad               │
├─────────────────────────────────────────────────────┤
│  P - Perceivable (Perceptible)                      │
│      Texto alternativo, contraste, subtítulos       │
│  O - Operable (Operable)                            │
│      Navegación por teclado, sin trampas de foco    │
│  U - Understandable (Comprensible)                  │
│      Lenguaje claro, errores descriptivos           │
│  R - Robust (Robusto)                               │
│      Compatible con tecnologías asistivas           │
└─────────────────────────────────────────────────────┘

Niveles: A (mínimo) → AA (recomendado) → AAA (ideal)
Objetivo empresarial: cumplir nivel AA
```

## 2. ARIA Roles y Atributos

```html
<!-- Roles semánticos -->
<nav aria-label="Menú principal">...</nav>
<main role="main">...</main>
<aside aria-label="Filtros">...</aside>

<!-- Botones con contexto -->
<button aria-label="Eliminar usuario Juan Pérez" (click)="eliminar(usuario)">
  <mat-icon>delete</mat-icon>
</button>

<!-- Formularios accesibles -->
<mat-form-field>
  <mat-label>Email</mat-label>
  <input matInput formControlName="email" 
         aria-describedby="email-hint"
         [attr.aria-invalid]="form.get('email')?.invalid">
  <mat-hint id="email-hint">Ingrese su email corporativo</mat-hint>
  <mat-error role="alert">Email es requerido</mat-error>
</mat-form-field>

<!-- Live regions (anuncios dinámicos) -->
<div aria-live="polite" aria-atomic="true" class="sr-only">
  {{ mensajeEstado() }}
</div>

<!-- Tablas -->
<table aria-label="Lista de usuarios" role="grid">
  <caption class="sr-only">Usuarios del sistema con sus roles</caption>
  ...
</table>
```

## 3. Navegación por Teclado

```typescript
// Manejo de foco en modales
@Component({ ... })
export class ModalComponent implements AfterViewInit {
  @ViewChild('primerInput') primerInput!: ElementRef;

  ngAfterViewInit() {
    this.primerInput.nativeElement.focus(); // foco al abrir
  }

  @HostListener('keydown.escape')
  cerrar() {
    this.dialogRef.close();
  }
}

// Skip navigation link
// <a class="skip-link" href="#main-content">Saltar al contenido</a>
// <main id="main-content" tabindex="-1">...</main>
```

```css
/* Indicador de foco visible */
:focus-visible {
  outline: 3px solid #1976d2;
  outline-offset: 2px;
}

/* Ocultar visualmente pero accesible para screen readers */
.sr-only {
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  border: 0;
}
```

## 4. Contraste y Texto

```css
/* Contraste mínimo AA: 4.5:1 para texto normal, 3:1 para texto grande */
:root {
  --text-primary: #212121;    /* sobre blanco = 16.1:1 ✅ */
  --text-secondary: #616161;  /* sobre blanco = 5.9:1 ✅ */
  --text-disabled: #9e9e9e;   /* sobre blanco = 2.9:1 ❌ usar con cuidado */
}

/* Tamaño mínimo de texto: 16px para body */
body { font-size: 16px; line-height: 1.5; }

/* No depender solo del color para comunicar información */
.error { color: red; }
.error::before { content: "⚠ "; }  /* ícono además del color */
```

## 5. Testing de Accesibilidad

```typescript
// Usar axe-core para auditoría automática
import { axe, toHaveNoViolations } from 'jest-axe';

expect.extend(toHaveNoViolations);

it('no debe tener violaciones de accesibilidad', async () => {
  const { container } = render(UsuarioFormComponent);
  const results = await axe(container);
  expect(results).toHaveNoViolations();
});
```

## 6. Ejercicios

1. Audita un componente con Chrome DevTools (Lighthouse → Accessibility) y corrige los errores.
2. Implementa navegación completa por teclado en una tabla con acciones.
3. Agrega ARIA labels, roles y live regions a un formulario con validación.
4. Crea un componente modal accesible (trap focus, escape para cerrar, aria-modal).
5. Verifica contraste de colores con herramientas (WebAIM Contrast Checker).

---

Nota: La validación completa de WCAG requiere pruebas manuales con tecnologías asistivas y revisión experta.

## Siguiente Módulo
→ [25-Micro Frontends](../25-micro-frontends/README.md)
