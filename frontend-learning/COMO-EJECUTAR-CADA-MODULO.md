# 📋 Cómo Ejecutar Cada Módulo - frontend-learning

## Módulos Web (01-09): HTML/CSS/JS puros

| Módulo | Comando | Qué demuestra |
|--------|---------|---------------|
| 02 HTML | Abrir `02-html/proyecto/index.html` en navegador | Semántica, formularios, tablas |
| 03 CSS | Abrir `03-css/proyecto/index.html` en navegador | Flexbox, Grid, responsive |
| 04 JS | `node 04-javascript/proyecto/ejercicios.js` | Variables, funciones, arrays, closures |
| 05 TS | `npx ts-node 05-typescript/proyecto/ejercicios.ts` | Tipos, interfaces, generics |
| 06 DOM | Abrir `06-dom/proyecto/index.html` en navegador | querySelector, eventos, delegación |
| 07 Async | `node 07-asincronismo/proyecto/async-demo.js` | Callbacks, Promises, async/await |
| 08 ES6 | `node 08-es6/proyecto/es6-demo.js` | Arrow, spread, destructuring |
| 09 Testing | `node 09-testing-js/proyecto/testing-demo.js` | Tests unitarios simulados |

---

## Módulos Angular (10-30): Requieren proyecto Angular

### Para ejecutar los módulos Angular necesitas un proyecto:

```bash
# Crear proyecto Angular (una sola vez)
ng new mi-app-learning --standalone --style=scss --routing
cd mi-app-learning
ng add @angular/material

# Luego copias los ejemplos de cada módulo dentro del proyecto
```

### Módulos con código de referencia:

| Módulo | Archivo | Concepto |
|--------|---------|----------|
| 10 | `10-angular-fundamentos/proyecto/MANUAL-TECNICO.md` | CLI, arquitectura, standalone |
| 11 | `11-componentes/proyecto/ejemplo-componente.ts` | input(), output(), lifecycle |
| 12 | `12-directivas/proyecto/ejemplo-directivas.ts` | @if, @for, @switch, @defer |
| 13 | `13-data-binding/proyecto/ejemplo-binding.ts` | Interpolation, property, event, model() |
| 14 | `14-servicios/proyecto/ejemplo-servicio.ts` | @Injectable, inject(), state |
| 15 | `15-rxjs/proyecto/MANUAL-TECNICO.md` | Operadores RxJS |
| 16 | `16-http-client/proyecto/ejemplo-http.ts` | GET/POST/PUT/DELETE, interceptors |
| 17 | `17-routing/proyecto/ejemplo-routing.ts` | Lazy loading, guards, resolvers |
| 18 | `18-forms/proyecto/ejemplo-forms.ts` | Reactive Forms, validaciones |
| 19 | `19-angular-material/proyecto/MANUAL-TECNICO.md` | mat-table, dialog, stepper |
| 20 | `20-primeng/proyecto/ejemplo-primeng.ts` | DataTable, Dialog, Confirm |
| 21 | `21-state-management/proyecto/ejemplo-state.ts` | Signals, computed, NgRx |
| 22 | `22-testing-angular/proyecto/ejemplo-testing.ts` | TestBed, ComponentFixture |
| 23 | `23-performance/proyecto/ejemplo-performance.ts` | OnPush, @defer, virtual scroll |
| 24 | `24-accesibilidad/proyecto/ejemplo-a11y.html` | ARIA, keyboard navigation |
| 25 | `25-micro-frontends/proyecto/ejemplo-mfe.ts` | Module Federation |
| 26 | `26-docker/proyecto/Dockerfile` + `nginx.conf` | Dockerizar Angular |
| 27 | `27-cicd/proyecto/github-actions.yml` | CI/CD pipeline |
| 28 | `28-arquitectura/proyecto/MANUAL-TECNICO.md` | Clean Architecture frontend |
| 29 | `29-migracion-extjs/proyecto/MANUAL-TECNICO.md` | ExtJS → Angular |
| 30 | `30-proyecto-final/proyecto/MANUAL-TECNICO.md` | Sistema enterprise completo |

---

## Proyecto Final (Módulo 30): Sistema Completo

```bash
cd frontend-learning/30-proyecto-final/proyecto

# Crear proyecto Angular
ng new sistema-empresarial --standalone --style=scss --routing
cd sistema-empresarial
ng add @angular/material
npm install primeng primeicons

# Generar módulos
ng g c features/auth/login
ng g c features/dashboard
ng g c features/productos/lista
ng g c features/productos/detalle
ng g c features/ventas/nueva
ng g s core/services/auth
ng g s core/services/producto

# Ejecutar
ng serve
# → http://localhost:4200

# Dockerizar
docker build -t sistema-empresarial .
docker run -p 80:80 sistema-empresarial
```

---

## Prerrequisitos

```bash
node -version    # Node.js 20+
npm -version     # npm 10+
ng version       # Angular CLI 17+

# Para TypeScript standalone:
npm install -g ts-node typescript
```

---

## Flujo recomendado

```
1. Módulos 02-09: Ejecutar demos HTML/CSS/JS (fundamentos web)
2. Módulo 10: Crear proyecto Angular con CLI
3. Módulos 11-18: Copiar ejemplos al proyecto y experimentar
4. Módulos 19-21: Agregar Material/PrimeNG y state management
5. Módulos 22-27: Testing, performance, Docker, CI/CD
6. Módulos 28-29: Refactorizar a Clean Architecture
7. Módulo 30: Construir el proyecto final enterprise
```
