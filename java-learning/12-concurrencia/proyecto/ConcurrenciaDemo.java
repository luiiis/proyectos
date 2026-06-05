import java.util.concurrent.*;
import java.util.List;
import java.util.stream.IntStream;

/**
 * MÓDULO 12: Concurrencia - Demo ejecutable
 * Ejecutar: javac ConcurrenciaDemo.java && java ConcurrenciaDemo
 */
public class ConcurrenciaDemo {
    public static void main(String[] args) throws Exception {
        System.out.println("═══ MÓDULO 12: Concurrencia ═══\n");

        // Virtual Threads (Java 21+)
        System.out.println("── Virtual Threads ──");
        long inicio = System.currentTimeMillis();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 1000).forEach(i ->
                executor.submit(() -> {
                    Thread.sleep(java.time.Duration.ofMillis(100));
                    return i;
                })
            );
        }
        System.out.printf("1000 tareas en %dms (Virtual Threads)%n", System.currentTimeMillis() - inicio);

        // CompletableFuture
        System.out.println("\n── CompletableFuture ──");
        CompletableFuture<String> futuro = CompletableFuture
            .supplyAsync(() -> { sleep(100); return "Datos del servidor"; })
            .thenApply(data -> data.toUpperCase())
            .thenApply(data -> "Procesado: " + data);

        System.out.println(futuro.get());

        // Múltiples en paralelo
        System.out.println("\n── Paralelo con allOf ──");
        var f1 = CompletableFuture.supplyAsync(() -> { sleep(100); return "Usuario"; });
        var f2 = CompletableFuture.supplyAsync(() -> { sleep(150); return "Pedidos"; });
        var f3 = CompletableFuture.supplyAsync(() -> { sleep(80); return "Inventario"; });

        CompletableFuture.allOf(f1, f2, f3).join();
        System.out.printf("Resultados: %s, %s, %s%n", f1.get(), f2.get(), f3.get());
    }

    static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
