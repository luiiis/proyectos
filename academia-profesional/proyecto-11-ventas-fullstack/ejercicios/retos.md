# Ejercicios y Retos - Proyecto 11: Ventas Full Stack

## Reto 1: Reporte PDF de venta
Al registrar una venta, generar un ticket/factura en PDF:
- Backend: usar OpenPDF o iText para generar el PDF
- Frontend: botón "Descargar ticket" que descarga el PDF

**Lo que practicas:** Generación de documentos, HTTP response con content-type PDF

---

## Reto 2: Carrito de compras
Implementa un carrito en el frontend:
- Agregar/quitar productos
- Modificar cantidades
- Ver total en tiempo real (Signal)
- Confirmar → registra la venta en el backend

**Lo que practicas:** Estado local con Signals, lógica de negocio en frontend

---

## Reto 3: Dashboard con métricas reales
Conecta el dashboard con datos REALES del backend:
- Ventas del día / semana / mes
- Top 5 productos más vendidos
- Clientes más frecuentes
- Comparativa con período anterior (% cambio)

**Lo que practicas:** Queries SQL con agregación, endpoints de reportes, visualización

---

## Reto 4: Búsqueda con autocomplete
En el registro de venta, al escribir el nombre del producto:
- Mostrar sugerencias en tiempo real (autocomplete)
- Debounce de 300ms para no saturar el backend
- Buscar por nombre o código

**Lo que practicas:** PrimeNG AutoComplete, debounceTime, performance

---

## Reto 5: Manejo de stock en tiempo real
Cuando se registra una venta:
- Backend descuenta stock
- Si stock < 5, mostrar alerta en el dashboard
- Si stock = 0, el producto aparece como "Agotado" y no se puede vender

**Lo que practicas:** Lógica de negocio, validaciones cruzadas, estado

---

## Reto 6: Notificaciones push
Implementa notificaciones toast cuando:
- Venta registrada exitosamente
- Error de stock insuficiente
- Producto con stock bajo (alerta global)

**Lo que practicas:** PrimeNG Toast, MessageService, feedback al usuario

---

## Reto 7 (Avanzado): Testing E2E
Escribe tests end-to-end con Playwright o Cypress:
- Test: login → navegar al dashboard → ver KPIs
- Test: registrar venta → verificar que aparece en historial
- Test: intentar vender más stock del disponible → ver error

**Lo que practicas:** Testing E2E, automatización, calidad
