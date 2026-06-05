/**
 * MÓDULO 09: Testing JavaScript - Demo (simulación sin Jest)
 * Para tests reales: npm install jest && npx jest
 * Ejecutar demo: node testing-demo.js
 */
console.log('═══ MÓDULO 09: Testing JavaScript ═══\n');

// Función a testear
function calcularDescuento(precio, porcentaje) {
    if (precio <= 0) throw new Error('Precio debe ser positivo');
    if (porcentaje < 0 || porcentaje > 100) throw new Error('Porcentaje inválido');
    return precio * (1 - porcentaje / 100);
}

// Mini framework de testing (simula Jest)
let passed = 0, failed = 0;
function test(descripcion, fn) {
    try { fn(); passed++; console.log(`  ✓ ${descripcion}`); }
    catch (e) { failed++; console.log(`  ✗ ${descripcion}: ${e.message}`); }
}
function expect(valor) {
    return {
        toBe: (esperado) => { if (valor !== esperado) throw new Error(`Expected ${esperado}, got ${valor}`); },
        toThrow: () => { try { valor(); throw new Error('No lanzó excepción'); } catch(e) { if (e.message === 'No lanzó excepción') throw e; } }
    };
}

// Tests
console.log('── calcularDescuento() ──');
test('10% de descuento en $1000 = $900', () => { expect(calcularDescuento(1000, 10)).toBe(900); });
test('20% de descuento en $500 = $400', () => { expect(calcularDescuento(500, 20)).toBe(400); });
test('0% descuento no cambia el precio', () => { expect(calcularDescuento(1000, 0)).toBe(1000); });
test('100% descuento = gratis', () => { expect(calcularDescuento(1000, 100)).toBe(0); });
test('Precio negativo lanza error', () => { expect(() => calcularDescuento(-1, 10)).toThrow(); });
test('Porcentaje > 100 lanza error', () => { expect(() => calcularDescuento(100, 150)).toThrow(); });

console.log(`\n═══ Resultados: ${passed} passed, ${failed} failed ═══`);
console.log('\nPara tests reales con Jest:');
console.log('  npm init -y && npm install --save-dev jest');
console.log('  npx jest --init');
console.log('  npx jest');
