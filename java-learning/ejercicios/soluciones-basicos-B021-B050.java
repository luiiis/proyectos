import java.util.*;
import java.util.stream.*;

/**
 * SOLUCIONES - Ejercicios Básicos B021 a B050 (POO + Colecciones)
 * Compilar: javac soluciones-basicos-B021-B050.java
 * Ejecutar: java SolucionesPOO
 */
public class SolucionesPOO {

    // ═══ B021-B030: POO ═══

    // B021: Clase Cuenta Bancaria con operaciones
    static class CuentaBancaria {
        private final String titular;
        private double saldo;
        private final List<String> movimientos = new ArrayList<>();

        CuentaBancaria(String titular, double saldoInicial) {
            this.titular = titular;
            this.saldo = saldoInicial;
            movimientos.add("Apertura: +$" + saldoInicial);
        }

        void depositar(double monto) {
            if (monto <= 0) throw new IllegalArgumentException("Monto debe ser positivo");
            saldo += monto;
            movimientos.add("Depósito: +$" + monto);
        }

        void retirar(double monto) {
            if (monto <= 0) throw new IllegalArgumentException("Monto debe ser positivo");
            if (monto > saldo) throw new IllegalStateException("Fondos insuficientes");
            saldo -= monto;
            movimientos.add("Retiro: -$" + monto);
        }

        double getSaldo() { return saldo; }
        List<String> getMovimientos() { return Collections.unmodifiableList(movimientos); }
        @Override public String toString() { return titular + " | Saldo: $" + saldo; }
    }

    // B022: Interface Calculable con múltiples implementaciones
    interface Calculable {
        double calcular(double a, double b);
        String getOperacion();
    }

    record Suma() implements Calculable {
        public double calcular(double a, double b) { return a + b; }
        public String getOperacion() { return "+"; }
    }
    record Resta() implements Calculable {
        public double calcular(double a, double b) { return a - b; }
        public String getOperacion() { return "-"; }
    }
    record Multiplicacion() implements Calculable {
        public double calcular(double a, double b) { return a * b; }
        public String getOperacion() { return "×"; }
    }

    // B023: Herencia - Sistema de figuras geométricas
    static abstract class Figura {
        abstract double area();
        abstract double perimetro();
        abstract String tipo();
        @Override public String toString() {
            return String.format("%s → Área: %.2f, Perímetro: %.2f", tipo(), area(), perimetro());
        }
    }

    static class Circulo extends Figura {
        private final double radio;
        Circulo(double radio) { this.radio = radio; }
        double area() { return Math.PI * radio * radio; }
        double perimetro() { return 2 * Math.PI * radio; }
        String tipo() { return "Círculo(r=" + radio + ")"; }
    }

    static class Rectangulo extends Figura {
        private final double ancho, alto;
        Rectangulo(double ancho, double alto) { this.ancho = ancho; this.alto = alto; }
        double area() { return ancho * alto; }
        double perimetro() { return 2 * (ancho + alto); }
        String tipo() { return "Rectángulo(" + ancho + "x" + alto + ")"; }
    }

    // ═══ B031-B040: Colecciones ═══

    // B031: Frecuencia de caracteres con HashMap
    static Map<Character, Integer> frecuenciaCaracteres(String texto) {
        var freq = new LinkedHashMap<Character, Integer>();
        for (char c : texto.toLowerCase().toCharArray()) {
            if (Character.isLetter(c)) {
                freq.merge(c, 1, Integer::sum);
            }
        }
        return freq;
    }

    // B032: Eliminar duplicados manteniendo orden
    static <T> List<T> eliminarDuplicados(List<T> lista) {
        return new ArrayList<>(new LinkedHashSet<>(lista));
    }

    // B033: Intersección de dos listas
    static <T> List<T> interseccion(List<T> a, List<T> b) {
        Set<T> setB = new HashSet<>(b);
        return a.stream().filter(setB::contains).distinct().toList();
    }

    // B034: Agrupar palabras por longitud
    static Map<Integer, List<String>> agruparPorLongitud(List<String> palabras) {
        return palabras.stream().collect(Collectors.groupingBy(String::length));
    }

    // B035: Implementar Stack con ArrayList
    static class MiStack<T> {
        private final List<T> elementos = new ArrayList<>();
        void push(T item) { elementos.add(item); }
        T pop() {
            if (elementos.isEmpty()) throw new NoSuchElementException("Stack vacío");
            return elementos.remove(elementos.size() - 1);
        }
        T peek() {
            if (elementos.isEmpty()) throw new NoSuchElementException("Stack vacío");
            return elementos.get(elementos.size() - 1);
        }
        boolean isEmpty() { return elementos.isEmpty(); }
        int size() { return elementos.size(); }
    }

    // B036: Validar paréntesis balanceados con Stack
    static boolean parentesisBalanceados(String expr) {
        var stack = new MiStack<Character>();
        Map<Character, Character> pares = Map.of(')', '(', ']', '[', '}', '{');
        for (char c : expr.toCharArray()) {
            if ("([{".indexOf(c) != -1) {
                stack.push(c);
            } else if (")]}".indexOf(c) != -1) {
                if (stack.isEmpty() || stack.pop() != pares.get(c)) return false;
            }
        }
        return stack.isEmpty();
    }

    // B037: Top N elementos más frecuentes
    static List<String> topFrecuentes(List<String> lista, int n) {
        return lista.stream()
            .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(n)
            .map(Map.Entry::getKey)
            .toList();
    }

    // B038: Merge de dos listas ordenadas
    static List<Integer> mergeSorted(List<Integer> a, List<Integer> b) {
        List<Integer> result = new ArrayList<>();
        int i = 0, j = 0;
        while (i < a.size() && j < b.size()) {
            if (a.get(i) <= b.get(j)) result.add(a.get(i++));
            else result.add(b.get(j++));
        }
        while (i < a.size()) result.add(a.get(i++));
        while (j < b.size()) result.add(b.get(j++));
        return result;
    }

    // B039: LRU Cache simple con LinkedHashMap
    static class LRUCache<K, V> extends LinkedHashMap<K, V> {
        private final int maxSize;
        LRUCache(int maxSize) {
            super(maxSize, 0.75f, true); // accessOrder = true
            this.maxSize = maxSize;
        }
        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > maxSize;
        }
    }

    // B040: Cifrado César
    static String cifrarCesar(String texto, int desplazamiento) {
        return texto.chars().mapToObj(c -> {
            if (Character.isUpperCase(c)) return String.valueOf((char) ('A' + (c - 'A' + desplazamiento) % 26));
            if (Character.isLowerCase(c)) return String.valueOf((char) ('a' + (c - 'a' + desplazamiento) % 26));
            return String.valueOf((char) c);
        }).collect(Collectors.joining());
    }

    // ═══ B041-B050: Streams avanzados ═══

    record Empleado(String nombre, String departamento, double salario) {}

    public static void main(String[] args) {
        System.out.println("═══ SOLUCIONES B021-B050 ═══\n");

        // B021: Cuenta Bancaria
        System.out.println("── B021: Cuenta Bancaria ──");
        var cuenta = new CuentaBancaria("Carlos García", 10000);
        cuenta.depositar(5000);
        cuenta.retirar(3000);
        System.out.println(cuenta);
        cuenta.getMovimientos().forEach(m -> System.out.println("  " + m));

        // B022: Interface Calculable
        System.out.println("\n── B022: Interface Calculable ──");
        List<Calculable> ops = List.of(new Suma(), new Resta(), new Multiplicacion());
        for (var op : ops) {
            System.out.printf("  10 %s 3 = %.0f%n", op.getOperacion(), op.calcular(10, 3));
        }

        // B023: Figuras
        System.out.println("\n── B023: Herencia Figuras ──");
        List<Figura> figuras = List.of(new Circulo(5), new Rectangulo(4, 6));
        figuras.forEach(f -> System.out.println("  " + f));

        // B031: Frecuencia
        System.out.println("\n── B031: Frecuencia caracteres ──");
        System.out.println("  'programacion': " + frecuenciaCaracteres("programacion"));

        // B032: Duplicados
        System.out.println("\n── B032: Eliminar duplicados ──");
        System.out.println("  " + eliminarDuplicados(List.of(1,2,2,3,3,3,4)));

        // B033: Intersección
        System.out.println("\n── B033: Intersección ──");
        System.out.println("  " + interseccion(List.of(1,2,3,4,5), List.of(3,4,5,6,7)));

        // B034: Agrupar por longitud
        System.out.println("\n── B034: Agrupar por longitud ──");
        var grouped = agruparPorLongitud(List.of("hola", "mundo", "java", "spring", "ok"));
        grouped.forEach((k, v) -> System.out.println("  " + k + " letras: " + v));

        // B035-B036: Stack + Paréntesis
        System.out.println("\n── B036: Paréntesis balanceados ──");
        System.out.println("  '([{}])': " + parentesisBalanceados("([{}])"));
        System.out.println("  '([)]': " + parentesisBalanceados("([)]"));
        System.out.println("  '{[()]}': " + parentesisBalanceados("{[()]}"));

        // B037: Top frecuentes
        System.out.println("\n── B037: Top 3 más frecuentes ──");
        var palabras = List.of("java","spring","java","angular","java","spring","docker","spring");
        System.out.println("  " + topFrecuentes(palabras, 3));

        // B038: Merge sorted
        System.out.println("\n── B038: Merge sorted ──");
        System.out.println("  " + mergeSorted(List.of(1,3,5,7), List.of(2,4,6,8)));

        // B039: LRU Cache
        System.out.println("\n── B039: LRU Cache (max 3) ──");
        var cache = new LRUCache<String, Integer>(3);
        cache.put("a", 1); cache.put("b", 2); cache.put("c", 3);
        cache.get("a");  // acceder a "a" → se mueve al final
        cache.put("d", 4);  // "b" se elimina (LRU)
        System.out.println("  Cache: " + cache);  // {c=3, a=1, d=4}

        // B040: Cifrado César
        System.out.println("\n── B040: Cifrado César ──");
        String original = "Hola Mundo";
        String cifrado = cifrarCesar(original, 3);
        String descifrado = cifrarCesar(cifrado, -3);
        System.out.println("  Original: " + original);
        System.out.println("  Cifrado(+3): " + cifrado);
        System.out.println("  Descifrado: " + descifrado);

        // B041-B050: Streams con Empleados
        System.out.println("\n── B041-B050: Streams avanzados ──");
        var empleados = List.of(
            new Empleado("Carlos", "Tecnología", 55000),
            new Empleado("María", "Ventas", 42000),
            new Empleado("Juan", "Tecnología", 48000),
            new Empleado("Ana", "RRHH", 38000),
            new Empleado("Luis", "Ventas", 45000),
            new Empleado("Pedro", "Tecnología", 62000)
        );

        // B041: Salario promedio por departamento
        System.out.println("  Salario promedio por depto:");
        empleados.stream()
            .collect(Collectors.groupingBy(Empleado::departamento, Collectors.averagingDouble(Empleado::salario)))
            .forEach((d, avg) -> System.out.printf("    %s: $%,.0f%n", d, avg));

        // B042: Empleado mejor pagado por departamento
        System.out.println("  Mejor pagado por depto:");
        empleados.stream()
            .collect(Collectors.groupingBy(Empleado::departamento,
                Collectors.maxBy(Comparator.comparingDouble(Empleado::salario))))
            .forEach((d, emp) -> System.out.printf("    %s: %s ($%,.0f)%n", d, emp.get().nombre(), emp.get().salario()));

        // B043: Partitioning (salario > 45000 vs <=45000)
        System.out.println("  Partición salario > $45000:");
        var particion = empleados.stream()
            .collect(Collectors.partitioningBy(e -> e.salario() > 45000));
        System.out.println("    > $45K: " + particion.get(true).stream().map(Empleado::nombre).toList());
        System.out.println("    ≤ $45K: " + particion.get(false).stream().map(Empleado::nombre).toList());

        System.out.println("\n═══ FIN ═══");
    }
}
