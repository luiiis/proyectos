# Módulo 09: Testing en JavaScript

## 1. Jest - Estructura Básica

```javascript
// producto.service.spec.ts
describe('ProductoService', () => {
  let service;

  beforeEach(() => {
    service = new ProductoService();
  });

  describe('calcularTotal', () => {
    it('debe calcular el total correctamente', () => {
      const items = [
        { precio: 100, cantidad: 2 },
        { precio: 50, cantidad: 1 },
      ];
      expect(service.calcularTotal(items)).toBe(250);
    });

    it('debe retornar 0 para array vacío', () => {
      expect(service.calcularTotal([])).toBe(0);
    });

    it('debe lanzar error si items es null', () => {
      expect(() => service.calcularTotal(null)).toThrow();
    });
  });
});
```

## 2. Matchers Comunes

```javascript
// Igualdad
expect(valor).toBe(5);              // igualdad estricta (===)
expect(obj).toEqual({ id: 1 });     // igualdad profunda
expect(valor).toBeTruthy();
expect(valor).toBeFalsy();
expect(valor).toBeNull();
expect(valor).toBeDefined();

// Números
expect(precio).toBeGreaterThan(0);
expect(descuento).toBeLessThanOrEqual(100);
expect(0.1 + 0.2).toBeCloseTo(0.3);

// Strings y Arrays
expect(mensaje).toContain('error');
expect(lista).toHaveLength(3);
expect(roles).toContain('admin');

// Objetos
expect(usuario).toHaveProperty('email');
expect(response).toMatchObject({ status: 200 });
```

## 3. Mocking

```javascript
// Mock de función
const mockFn = jest.fn();
mockFn.mockReturnValue(42);
mockFn.mockResolvedValue({ data: [] });  // para async

// Mock de módulo
jest.mock('./api.service');
import { ApiService } from './api.service';

// Mock de implementación
ApiService.getUsuarios = jest.fn().mockResolvedValue([
  { id: 1, nombre: 'Ana' },
]);

// Verificar llamadas
expect(mockFn).toHaveBeenCalled();
expect(mockFn).toHaveBeenCalledWith(1, 'admin');
expect(mockFn).toHaveBeenCalledTimes(2);

// Spy
const spy = jest.spyOn(service, 'guardar');
await service.procesarPedido(pedido);
expect(spy).toHaveBeenCalledWith(expect.objectContaining({ id: 1 }));
```

## 4. Testing Library (DOM)

```javascript
import { render, screen, fireEvent } from '@testing-library/dom';

// Renderizar y buscar
render(`<button class="btn">Guardar</button>`);
const boton = screen.getByRole('button', { name: /guardar/i });

// Interactuar
fireEvent.click(boton);

// Verificar
expect(boton).toBeDisabled();
expect(screen.getByText('Guardado')).toBeInTheDocument();
expect(screen.queryByText('Error')).not.toBeInTheDocument();
```

## 5. Patrón AAA (Arrange, Act, Assert)

```javascript
it('debe aplicar descuento a usuarios premium', () => {
  // Arrange (preparar)
  const usuario = { tipo: 'premium', descuento: 0.2 };
  const producto = { precio: 100 };

  // Act (ejecutar)
  const total = calcularPrecioFinal(producto, usuario);

  // Assert (verificar)
  expect(total).toBe(80);
});
```

## 6. Ejercicios

1. Escribe tests para una función `validarEmail(email)` con casos válidos e inválidos.
2. Crea un mock de `fetch` y testea una función que llama a una API.
3. Testea un carrito de compras: agregar, eliminar, calcular total con descuentos.
4. Usa Testing Library para testear un formulario (llenar inputs, submit, validación).
5. Implementa tests para una función async que maneja errores (try/catch).

---

## Siguiente Módulo
→ [10-Angular Fundamentos](../10-angular-fundamentos/README.md)
