/**
 * MÓDULO 07: Asincronismo - Demo ejecutable
 * Ejecutar: node async-demo.js
 */
console.log('═══ MÓDULO 07: Asincronismo ═══\n');

// ═══════ 1. CALLBACKS (el pasado) ═══════
console.log('── 1. Callbacks ──');
function buscarProducto(id, callback) {
    setTimeout(() => {
        callback(null, { id, nombre: 'Laptop', precio: 18999 });
    }, 100);
}
buscarProducto(1, (err, producto) => {
    console.log('  Callback:', producto.nombre);
});

// ═══════ 2. PROMISES ═══════
console.log('── 2. Promises ──');
function buscarConPromise(id) {
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            if (id > 0) resolve({ id, nombre: 'Monitor', precio: 12499 });
            else reject(new Error('ID inválido'));
        }, 100);
    });
}

buscarConPromise(2)
    .then(p => console.log('  Promise:', p.nombre))
    .catch(err => console.error('  Error:', err.message));

// ═══════ 3. ASYNC/AWAIT (el presente) ═══════
async function demo() {
    console.log('\n── 3. Async/Await ──');

    try {
        const producto = await buscarConPromise(3);
        console.log('  Await:', producto.nombre);
    } catch (err) {
        console.error('  Error:', err.message);
    }

    // Paralelo con Promise.all
    console.log('\n── 4. Promise.all (paralelo) ──');
    const inicio = Date.now();
    const [p1, p2, p3] = await Promise.all([
        buscarConPromise(1),
        buscarConPromise(2),
        buscarConPromise(3),
    ]);
    console.log(`  3 requests en ${Date.now() - inicio}ms (paralelo, no secuencial)`);
    console.log(`  Resultados: ${p1.nombre}, ${p2.nombre}, ${p3.nombre}`);

    // Fetch API (simulado)
    console.log('\n── 5. Fetch API (patrón real) ──');
    console.log('  // En un navegador o con node-fetch:');
    console.log('  // const res = await fetch("http://localhost:8080/api/productos");');
    console.log('  // const data = await res.json();');
}

demo().then(() => console.log('\n✓ Demo async completada'));
