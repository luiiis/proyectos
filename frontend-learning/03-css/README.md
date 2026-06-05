# Módulo 03: CSS3 - Estilos y Layout

## 1. Selectores

```css
/* Elemento */
h1 { color: #333; }

/* Clase e ID */
.card { border: 1px solid #ddd; }
#header { background: #1976d2; }

/* Combinadores */
.nav > li { display: inline-block; }       /* hijo directo */
.card .title { font-weight: bold; }        /* descendiente */
.btn + .btn { margin-left: 8px; }          /* hermano adyacente */

/* Pseudo-clases */
.btn:hover { background: #1565c0; }
tr:nth-child(even) { background: #f5f5f5; }
input:focus { border-color: #1976d2; }
input:invalid { border-color: red; }
```

## 2. Box Model

```
┌─────────────────────────────────────┐
│             margin                   │
│  ┌───────────────────────────────┐  │
│  │          border               │  │
│  │  ┌─────────────────────────┐  │  │
│  │  │       padding           │  │  │
│  │  │  ┌───────────────────┐  │  │  │
│  │  │  │    CONTENT        │  │  │  │
│  │  │  └───────────────────┘  │  │  │
│  │  └─────────────────────────┘  │  │
│  └───────────────────────────────┘  │
└─────────────────────────────────────┘

* { box-sizing: border-box; }  /* ← SIEMPRE usar esto */
```

## 3. Flexbox

```css
/* Navbar horizontal */
.navbar {
  display: flex;
  justify-content: space-between;  /* main axis */
  align-items: center;             /* cross axis */
  gap: 16px;
}

/* Centrar elemento */
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
}

/* Cards responsivas */
.card-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}
.card { flex: 1 1 300px; }  /* grow shrink basis */
```

## 4. CSS Grid

```css
/* Layout de página completa */
.layout {
  display: grid;
  grid-template-columns: 250px 1fr;
  grid-template-rows: 60px 1fr 40px;
  grid-template-areas:
    "header header"
    "sidebar main"
    "footer footer";
  min-height: 100vh;
}
.header  { grid-area: header; }
.sidebar { grid-area: sidebar; }
.main    { grid-area: main; }
.footer  { grid-area: footer; }

/* Grid de productos */
.products {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
}
```

## 5. Responsive Design + Media Queries

```css
/* Mobile First */
.container { padding: 16px; }

@media (min-width: 768px) {
  .container { padding: 24px; max-width: 720px; margin: 0 auto; }
}

@media (min-width: 1024px) {
  .container { max-width: 960px; }
  .sidebar { display: block; }  /* oculto en mobile */
}
```

## 6. Ejercicios

1. Crea un navbar responsive con Flexbox (horizontal en desktop, hamburguesa en mobile).
2. Implementa un layout de dashboard con Grid (sidebar + header + content + footer).
3. Crea un grid de cards de productos que se adapte de 1 a 4 columnas según el viewport.
4. Diseña un formulario centrado vertical y horizontalmente con Flexbox.
5. Implementa una tabla responsive que se convierta en cards en mobile.

---

## Siguiente Módulo
→ [04-JavaScript](../04-javascript/README.md)
