# Manual de Usuario — Fullstack Roadmap

## ¿Qué es este proyecto?
Una ruta de aprendizaje que te lleva desde CERO hasta construir sistemas profesionales Full Stack con Java + Angular.

---

## ¿Cómo usarlo?

### Paso 1: Preparar tu computadora (Nivel 0)
- Abrir `nivel-00-preparacion/README.md`
- Seguir CADA paso de instalación
- Al terminar: ejecutar el checklist para verificar que todo funciona

### Paso 2: Seguir los niveles EN ORDEN
```
Nivel 0 → 1 → 2 → 3 → 4 → 5 → 6 → 7 → 8 → 9 → 10 → 11 → 12 → 13 → 14
```
**NO saltar niveles.** Cada uno construye sobre el anterior.

### Paso 3: En cada nivel
1. Leer el `README.md` (entender los conceptos)
2. Leer el `COMO_EJECUTAR.md` (cómo levantarlo)
3. Ejecutar el proyecto y probarlo
4. Modificarlo para experimentar
5. Cuando entiendas TODO → avanzar al siguiente

---

## ¿Qué hay en cada carpeta?

| Archivo | Para qué |
|---------|----------|
| `README.md` | Explicación del nivel: qué aprenderás, conceptos, versiones |
| `COMO_EJECUTAR.md` | Paso a paso para levantar y probar el proyecto |
| `proyecto/backend/` | Código Java (Spring Boot) |
| `proyecto/frontend/` | Código Angular (cuando aplique) |
| `proyecto/backend/src/main/resources/db/migration/` | Scripts SQL |

---

## Niveles y qué construyes en cada uno

| Nivel | Proyecto | Lo que VES funcionando |
|-------|----------|----------------------|
| 1 | API Saludos | Abres el navegador → ves "Hola Carlos" en JSON |
| 2 | CRUD Tareas | En Postman creas/editas/borras tareas |
| 3 | Productos + BD | Lo mismo pero datos se guardan en MySQL (persisten) |
| 4 | Tests | Ejecutas `mvn test` → 14 pruebas pasan en verde |
| 5 | Login + JWT | Login en Postman → recibes token → accedes a endpoints protegidos |
| 8 | Angular | Abres localhost:4200 → ves tu app con botones y listas |
| 9 | Angular + API | Ves productos reales de la BD en una tabla bonita |
| 10 | Login completo | Pantalla de login → dashboard → todo protegido |
| 11 | Inventario | Sistema completo de inventario (como si fuera una app real) |
| 12 | ERP | Punto de venta + inventario + reportes (producto profesional) |
| 14 | Docker | Un solo comando `docker compose up` levanta TODO |

---

## Tiempo estimado

| Si dedicas... | Terminas en... |
|---------------|----------------|
| 1 hora/día | ~12 meses |
| 2 horas/día | ~6 meses |
| 4 horas/día | ~3 meses |
| 8 horas/día (tiempo completo) | ~6-8 semanas |

---

## Si algo no funciona

1. **Lee el error completo** (no solo la primera línea)
2. **Busca en el COMO_EJECUTAR** → sección "Errores frecuentes"
3. **Verifica prerrequisitos** (`java -version`, `mvn -version`, etc.)
4. **Reinicia** (cierra terminal, vuelve a abrir, ejecuta de nuevo)
5. **Google el error** (copia y pega el mensaje exacto)
6. **Pide ayuda** con el error COMPLETO + qué comando ejecutaste

---

## Glosario rápido

| Término | Qué es |
|---------|--------|
| API | Programa que recibe peticiones y devuelve datos (JSON) |
| Endpoint | Una URL que hace algo específico (/api/productos) |
| Backend | El programa que corre en el servidor (Java) |
| Frontend | Lo que ve el usuario en el navegador (Angular) |
| JWT | "Pasaporte digital" que demuestra quién eres |
| CRUD | Crear, Leer, Actualizar, Eliminar (las 4 operaciones básicas) |
| Docker | Empaqueta tu app para que funcione en cualquier computadora |
| Git | Sistema para guardar versiones de tu código |
| Maven | Herramienta que descarga librerías y compila tu proyecto Java |
| MyBatis | Librería que conecta Java con la base de datos |
| Flyway | Ejecuta scripts SQL automáticamente al arrancar la app |
