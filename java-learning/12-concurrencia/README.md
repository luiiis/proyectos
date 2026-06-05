# Módulo 12: Concurrencia y Multithreading

## Virtual Threads (Java 21+) — El cambio más importante
```java
// ANTES: Thread pool limitado (200 threads = 200 tareas simultáneas)
ExecutorService executor = Executors.newFixedThreadPool(200);

// AHORA: Virtual Threads (1,000,000+ tareas simultáneas)
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    IntStream.range(0, 100_000).forEach(i ->
        executor.submit(() -> {
            Thread.sleep(Duration.ofSeconds(1));
            return procesarPedido(i);
        })
    );
}
```

## CompletableFuture (Async sin bloquear)
```java
CompletableFuture<Producto> futuro = CompletableFuture
    .supplyAsync(() -> buscarProducto(id))           // Async
    .thenApply(p -> calcularDescuento(p))            // Transformar
    .thenApply(p -> aplicarImpuestos(p))             // Encadenar
    .exceptionally(ex -> productoDefault());          // Manejar error

// Ejecutar múltiples en paralelo
CompletableFuture.allOf(futuro1, futuro2, futuro3).join();
```

## Structured Concurrency (Java 25)
```java
try (var scope = StructuredTaskScope.open()) {
    var usuario = scope.fork(() -> buscarUsuario(id));
    var pedidos = scope.fork(() -> buscarPedidos(id));
    scope.join();  // Esperar ambos
    return new Perfil(usuario.get(), pedidos.get());
}
// Si uno falla → el otro se cancela automáticamente
```

## Ejercicios
1. Descarga 10 URLs en paralelo con CompletableFuture
2. Implementa un procesador de pedidos con Virtual Threads
3. Simula un sistema de transferencias bancarias thread-safe
