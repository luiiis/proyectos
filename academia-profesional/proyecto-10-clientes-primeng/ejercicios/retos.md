# Ejercicios y Retos - Proyecto 10: Clientes PrimeNG

## Reto 1: Exportar tabla a Excel
Agrega un botón "Exportar" que descargue los datos de la tabla como archivo .xlsx usando la función exportCSV de p-table o una librería como xlsx.

**Lo que practicas:** File download, exportación de datos, UX

---

## Reto 2: Filtros múltiples
Implementa filtros combinados en la tabla:
- Dropdown por ciudad
- Slider por rango de edad
- Checkbox "Solo activos"
- Los filtros se combinan (AND)

**Lo que practicas:** PrimeNG components, reactive forms, filtrado

---

## Reto 3: Drag & Drop para reordenar
Permite reordenar filas con drag & drop y guardar el nuevo orden en el backend.

**Lo que practicas:** PrimeNG DragDrop, PATCH endpoint, estado

---

## Reto 4: Selección múltiple + acciones masivas
- Checkbox en cada fila para seleccionar
- Botón "Eliminar seleccionados" con confirmación
- Botón "Exportar seleccionados"

**Lo que practicas:** Selection en p-table, acciones batch, UX

---

## Reto 5: Validación en tiempo real del formulario
En el dialog de crear/editar:
- Validar email en tiempo real (formato + no duplicado via API)
- Mostrar fortaleza del password (si aplica)
- Deshabilitar botón Guardar hasta que todo sea válido

**Lo que practicas:** AsyncValidator, debounceTime, reactive forms

---

## Reto 6: Detalle expandible
Al hacer click en una fila, expandir para mostrar información adicional (últimas compras, historial) sin abrir un dialog.

**Lo que practicas:** p-table row expansion, datos anidados, lazy loading de detalle
