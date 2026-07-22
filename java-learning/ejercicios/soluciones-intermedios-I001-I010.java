import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

/**
 * SOLUCIONES - Ejercicios Intermedios I001 a I010 (Estructuras + Patrones)
 * Compilar: javac soluciones-intermedios-I001-I010.java
 * Ejecutar: java SolucionesIntermedios
 */
public class SolucionesIntermedios {

    // ═══ I001: Implementar Stack con Array (sin ArrayList) ═══
    static class ArrayStack<T> {
        private Object[] data;
        private int top = -1;

        ArrayStack(int capacity) { data = new Object[capacity]; }

        void push(T item) {
            if (top == data.length - 1) resize();
            data[++top] = item;
        }

        @SuppressWarnings("unchecked")
        T pop() {
            if (isEmpty()) throw new NoSuchElementException("Stack vacío");
            T item = (T) data[top];
            data[top--] = null;
            return item;
        }

        @SuppressWarnings("unchecked")
        T peek() {
            if (isEmpty()) throw new NoSuchElementException("Stack vacío");
            return (T) data[top];
        }

        boolean isEmpty() { return top == -1; }
        int size() { return top + 1; }

        private void resize() {
            data = Arrays.copyOf(data, data.length * 2);
        }
    }

    // ═══ I002: Implementar Queue con Linked Nodes ═══
    static class LinkedQueue<T> {
        private record Node<T>(T value, Node<T>[] next) {
            // No podemos usar record para nodos mutables, usamos clase
        }

        private static class QNode<T> {
            T value;
            QNode<T> next;
            QNode(T value) { this.value = value; }
        }

        private QNode<T> head, tail;
        private int size;

        void enqueue(T item) {
            var node = new QNode<>(item);
            if (tail != null) tail.next = node;
            tail = node;
            if (head == null) head = node;
            size++;
        }

        T dequeue() {
            if (isEmpty()) throw new NoSuchElementException("Queue vacía");
            T value = head.value;
            head = head.next;
            if (head == null) tail = null;
            size--;
            return value;
        }

        T peek() {
            if (isEmpty()) throw new NoSuchElementException("Queue vacía");
            return head.value;
        }

        boolean isEmpty() { return size == 0; }
        int size() { return size; }
    }

    // ═══ I003: Binary Search Tree ═══
    static class BST {
        private static class Node {
            int value;
            Node left, right;
            Node(int value) { this.value = value; }
        }

        private Node root;

        void insert(int value) { root = insert(root, value); }
        private Node insert(Node node, int value) {
            if (node == null) return new Node(value);
            if (value < node.value) node.left = insert(node.left, value);
            else if (value > node.value) node.right = insert(node.right, value);
            return node;
        }

        boolean contains(int value) { return contains(root, value); }
        private boolean contains(Node node, int value) {
            if (node == null) return false;
            if (value == node.value) return true;
            return value < node.value ? contains(node.left, value) : contains(node.right, value);
        }

        List<Integer> inOrder() {
            var result = new ArrayList<Integer>();
            inOrder(root, result);
            return result;
        }
        private void inOrder(Node node, List<Integer> result) {
            if (node == null) return;
            inOrder(node.left, result);
            result.add(node.value);
            inOrder(node.right, result);
        }
    }

    // ═══ I004: Patrón Observer ═══
    interface Observador {
        void notificar(String evento, Object datos);
    }

    static class EventBus {
        private final Map<String, List<Observador>> suscriptores = new HashMap<>();

        void suscribir(String evento, Observador observador) {
            suscriptores.computeIfAbsent(evento, k -> new ArrayList<>()).add(observador);
        }

        void publicar(String evento, Object datos) {
            suscriptores.getOrDefault(evento, List.of())
                .forEach(obs -> obs.notificar(evento, datos));
        }
    }

    // ═══ I005: Patrón Builder ═══
    static class Pedido {
        private final String numero;
        private final String cliente;
        private final List<String> items;
        private final double total;
        private final String envio;
        private final String notas;

        private Pedido(Builder b) {
            this.numero = b.numero;
            this.cliente = b.cliente;
            this.items = List.copyOf(b.items);
            this.total = b.total;
            this.envio = b.envio;
            this.notas = b.notas;
        }

        static class Builder {
            private String numero, cliente, envio = "Estándar", notas = "";
            private List<String> items = new ArrayList<>();
            private double total;

            Builder numero(String n) { this.numero = n; return this; }
            Builder cliente(String c) { this.cliente = c; return this; }
            Builder item(String item, double precio) {
                items.add(item);
                total += precio;
                return this;
            }
            Builder envio(String e) { this.envio = e; return this; }
            Builder notas(String n) { this.notas = n; return this; }
            Pedido build() {
                if (numero == null || cliente == null) throw new IllegalStateException("Faltan campos obligatorios");
                return new Pedido(this);
            }
        }

        @Override public String toString() {
            return String.format("Pedido[%s] cliente=%s, items=%s, total=$%.2f, envío=%s", numero, cliente, items, total, envio);
        }
    }

    // ═══ I006: Patrón Strategy ═══
    interface PrecioStrategy {
        double calcular(double precioBase);
        String nombre();
    }

    record PrecioNormal() implements PrecioStrategy {
        public double calcular(double p) { return p; }
        public String nombre() { return "Normal"; }
    }
    record PrecioVIP() implements PrecioStrategy {
        public double calcular(double p) { return p * 0.85; }  // 15% descuento
        public String nombre() { return "VIP (-15%)"; }
    }
    record PrecioMayorista() implements PrecioStrategy {
        public double calcular(double p) { return p * 0.70; }  // 30% descuento
        public String nombre() { return "Mayorista (-30%)"; }
    }

    // ═══ I007: Thread-safe Counter con Virtual Threads ═══
    static class ContadorSeguro {
        private final java.util.concurrent.atomic.AtomicInteger valor = new java.util.concurrent.atomic.AtomicInteger(0);
        void incrementar() { valor.incrementAndGet(); }
        void decrementar() { valor.decrementAndGet(); }
        int getValor() { return valor.get(); }
    }

    // ═══ MAIN ═══
    public static void main(String[] args) throws Exception {
        System.out.println("═══ SOLUCIONES INTERMEDIOS I001-I010 ═══\n");

        // I001: Stack
        System.out.println("── I001: Stack con Array ──");
        var stack = new ArrayStack<String>(4);
        stack.push("Java"); stack.push("Spring"); stack.push("Docker");
        System.out.println("  Peek: " + stack.peek());
        System.out.println("  Pop: " + stack.pop());
        System.out.println("  Size: " + stack.size());

        // I002: Queue
        System.out.println("\n── I002: Queue con LinkedList ──");
        var queue = new LinkedQueue<Integer>();
        queue.enqueue(10); queue.enqueue(20); queue.enqueue(30);
        System.out.println("  Dequeue: " + queue.dequeue());  // 10 (FIFO)
        System.out.println("  Peek: " + queue.peek());        // 20
        System.out.println("  Size: " + queue.size());        // 2

        // I003: BST
        System.out.println("\n── I003: Binary Search Tree ──");
        var bst = new BST();
        for (int v : new int[]{5, 3, 7, 1, 4, 6, 8}) bst.insert(v);
        System.out.println("  InOrder: " + bst.inOrder());       // [1,2,3,4,5,6,7,8]
        System.out.println("  Contains 4: " + bst.contains(4));  // true
        System.out.println("  Contains 9: " + bst.contains(9));  // false

        // I004: Observer
        System.out.println("\n── I004: Patrón Observer ──");
        var bus = new EventBus();
        bus.suscribir("venta", (evento, datos) -> System.out.println("  📧 Email: Nueva " + evento + " - " + datos));
        bus.suscribir("venta", (evento, datos) -> System.out.println("  📱 Push: " + datos));
        bus.suscribir("stock_bajo", (evento, datos) -> System.out.println("  ⚠️ Alerta: " + datos));
        bus.publicar("venta", "Laptop HP $18,999");
        bus.publicar("stock_bajo", "Mouse MX: solo 2 unidades");

        // I005: Builder
        System.out.println("\n── I005: Patrón Builder ──");
        var pedido = new Pedido.Builder()
            .numero("P-001")
            .cliente("Carlos García")
            .item("Laptop HP", 18999)
            .item("Mouse MX", 1899)
            .envio("Express")
            .notas("Fragil")
            .build();
        System.out.println("  " + pedido);

        // I006: Strategy
        System.out.println("\n── I006: Patrón Strategy ──");
        double precioBase = 10000;
        List<PrecioStrategy> estrategias = List.of(new PrecioNormal(), new PrecioVIP(), new PrecioMayorista());
        for (var s : estrategias) {
            System.out.printf("  %s: $%,.2f → $%,.2f%n", s.nombre(), precioBase, s.calcular(precioBase));
        }

        // I007: Thread-safe con Virtual Threads
        System.out.println("\n── I007: Contador Thread-Safe (Virtual Threads) ──");
        var contador = new ContadorSeguro();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 10_000; i++) {
                executor.submit(contador::incrementar);
            }
        }
        System.out.println("  10,000 incrementos concurrentes → Resultado: " + contador.getValor());
        System.out.println("  ¿Correcto (10000)? " + (contador.getValor() == 10000 ? "✓ SÍ" : "✗ NO"));

        System.out.println("\n═══ FIN ═══");
    }
}
