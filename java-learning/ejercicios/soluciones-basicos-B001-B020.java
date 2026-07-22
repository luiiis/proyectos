/**
 * SOLUCIONES - Ejercicios Básicos B001 a B020
 * Compilar: javac soluciones-basicos-B001-B020.java
 * Ejecutar: java SolucionesBasicos
 */
public class SolucionesBasicos {

    public static void main(String[] args) {
        System.out.println("═══ SOLUCIONES EJERCICIOS BÁSICOS ═══\n");

        // B001: Suma de dígitos
        System.out.println("B001 - Suma de dígitos de 1234: " + sumaDigitos(1234));  // 10
        System.out.println("B001 - Suma de dígitos de 9999: " + sumaDigitos(9999));  // 36

        // B002: Palíndromo
        System.out.println("\nB002 - 'anilina' es palíndromo: " + esPalindromo("anilina"));  // true
        System.out.println("B002 - 'java' es palíndromo: " + esPalindromo("java"));          // false

        // B003: Fibonacci
        System.out.print("\nB003 - Fibonacci(7): ");
        fibonacci(7);  // 0 1 1 2 3 5 8

        // B004: Máximo en array
        int[] nums = {3, 7, 2, 9, 1, 5, 8};
        System.out.println("\n\nB004 - Máximo en [3,7,2,9,1,5,8]: " + maximo(nums));  // 9

        // B005: Contar vocales
        System.out.println("\nB005 - Vocales en 'Hola Mundo': " + contarVocales("Hola Mundo"));  // 4

        // B006: Invertir string
        System.out.println("\nB006 - Invertir 'Java': " + invertir("Java"));  // avaJ

        // B007: Número primo
        System.out.println("\nB007 - 17 es primo: " + esPrimo(17));  // true
        System.out.println("B007 - 15 es primo: " + esPrimo(15));  // false

        // B008: Factorial
        System.out.println("\nB008 - Factorial de 5: " + factorial(5));  // 120

        // B009: Número a palabras (unidades)
        System.out.println("\nB009 - 7 en palabras: " + numeroAPalabra(7));  // siete

        // B010: FizzBuzz (primeros 20)
        System.out.println("\nB010 - FizzBuzz:");
        fizzBuzz(20);

        // B011: Segundo mayor
        System.out.println("\n\nB011 - Segundo mayor en [3,7,2,9,1]: " + segundoMayor(new int[]{3,7,2,9,1}));  // 7

        // B012: Array sin duplicados
        System.out.print("\nB012 - Sin duplicados [1,2,2,3,3,4]: ");
        sinDuplicados(new int[]{1, 2, 2, 3, 3, 4});

        // B013: Contar palabras
        System.out.println("\n\nB013 - Palabras en 'Hola mundo cruel': " + contarPalabras("Hola mundo cruel"));  // 3

        // B014: Potencia sin Math.pow
        System.out.println("\nB014 - 2^10: " + potencia(2, 10));  // 1024

        // B015: MCD (Máximo Común Divisor)
        System.out.println("\nB015 - MCD(48, 18): " + mcd(48, 18));  // 6

        // B016: Decimal a binario
        System.out.println("\nB016 - 13 en binario: " + decimalABinario(13));  // 1101

        // B017: Ordenar array (Bubble Sort)
        int[] desordenado = {5, 2, 8, 1, 9, 3};
        bubbleSort(desordenado);
        System.out.print("\nB017 - Ordenado: ");
        for (int n : desordenado) System.out.print(n + " ");

        // B018: Búsqueda binaria
        int[] ordenado = {1, 3, 5, 7, 9, 11, 13, 15};
        System.out.println("\n\nB018 - Buscar 7 en array ordenado: índice " + busquedaBinaria(ordenado, 7));  // 3

        // B019: Suma de array
        System.out.println("\nB019 - Suma de [1,2,3,4,5]: " + sumaArray(new int[]{1,2,3,4,5}));  // 15

        // B020: Rotar array
        int[] arr = {1, 2, 3, 4, 5};
        rotarDerecha(arr, 2);
        System.out.print("\nB020 - [1,2,3,4,5] rotado 2 posiciones: ");
        for (int n : arr) System.out.print(n + " ");  // 4 5 1 2 3
        System.out.println();
    }

    // ═══ IMPLEMENTACIONES ═══

    // B001: Suma de dígitos
    static int sumaDigitos(int n) {
        int suma = 0;
        n = Math.abs(n);
        while (n > 0) {
            suma += n % 10;    // Último dígito
            n /= 10;          // Quitar último dígito
        }
        return suma;
    }

    // B002: Palíndromo
    static boolean esPalindromo(String s) {
        s = s.toLowerCase().replaceAll("[^a-záéíóú]", "");
        int izq = 0, der = s.length() - 1;
        while (izq < der) {
            if (s.charAt(izq) != s.charAt(der)) return false;
            izq++;
            der--;
        }
        return true;
    }

    // B003: Fibonacci
    static void fibonacci(int n) {
        int a = 0, b = 1;
        for (int i = 0; i < n; i++) {
            System.out.print(a + " ");
            int temp = a + b;
            a = b;
            b = temp;
        }
    }

    // B004: Máximo en array
    static int maximo(int[] arr) {
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) max = arr[i];
        }
        return max;
    }

    // B005: Contar vocales
    static int contarVocales(String s) {
        int count = 0;
        for (char c : s.toLowerCase().toCharArray()) {
            if ("aeiouáéíóú".indexOf(c) != -1) count++;
        }
        return count;
    }

    // B006: Invertir string
    static String invertir(String s) {
        char[] chars = s.toCharArray();
        int izq = 0, der = chars.length - 1;
        while (izq < der) {
            char temp = chars[izq];
            chars[izq] = chars[der];
            chars[der] = temp;
            izq++;
            der--;
        }
        return new String(chars);
    }

    // B007: Número primo
    static boolean esPrimo(int n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }

    // B008: Factorial
    static long factorial(int n) {
        long resultado = 1;
        for (int i = 2; i <= n; i++) {
            resultado *= i;
        }
        return resultado;
    }

    // B009: Número a palabra (0-9)
    static String numeroAPalabra(int n) {
        String[] palabras = {"cero","uno","dos","tres","cuatro","cinco","seis","siete","ocho","nueve"};
        return (n >= 0 && n <= 9) ? palabras[n] : "fuera de rango";
    }

    // B010: FizzBuzz
    static void fizzBuzz(int n) {
        for (int i = 1; i <= n; i++) {
            if (i % 15 == 0) System.out.print("FizzBuzz ");
            else if (i % 3 == 0) System.out.print("Fizz ");
            else if (i % 5 == 0) System.out.print("Buzz ");
            else System.out.print(i + " ");
        }
    }

    // B011: Segundo mayor
    static int segundoMayor(int[] arr) {
        int max = Integer.MIN_VALUE, segundo = Integer.MIN_VALUE;
        for (int n : arr) {
            if (n > max) { segundo = max; max = n; }
            else if (n > segundo && n != max) segundo = n;
        }
        return segundo;
    }

    // B012: Sin duplicados
    static void sinDuplicados(int[] arr) {
        java.util.Set<Integer> vistos = new java.util.LinkedHashSet<>();
        for (int n : arr) vistos.add(n);
        vistos.forEach(n -> System.out.print(n + " "));
    }

    // B013: Contar palabras
    static int contarPalabras(String s) {
        return s.trim().isEmpty() ? 0 : s.trim().split("\\s+").length;
    }

    // B014: Potencia sin Math.pow
    static long potencia(int base, int exp) {
        long resultado = 1;
        for (int i = 0; i < exp; i++) resultado *= base;
        return resultado;
    }

    // B015: MCD (Euclides)
    static int mcd(int a, int b) {
        while (b != 0) { int temp = b; b = a % b; a = temp; }
        return a;
    }

    // B016: Decimal a binario
    static String decimalABinario(int n) {
        if (n == 0) return "0";
        StringBuilder sb = new StringBuilder();
        while (n > 0) { sb.insert(0, n % 2); n /= 2; }
        return sb.toString();
    }

    // B017: Bubble Sort
    static void bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j]; arr[j] = arr[j + 1]; arr[j + 1] = temp;
                }
            }
        }
    }

    // B018: Búsqueda binaria
    static int busquedaBinaria(int[] arr, int target) {
        int izq = 0, der = arr.length - 1;
        while (izq <= der) {
            int mid = (izq + der) / 2;
            if (arr[mid] == target) return mid;
            if (arr[mid] < target) izq = mid + 1;
            else der = mid - 1;
        }
        return -1;
    }

    // B019: Suma de array
    static int sumaArray(int[] arr) {
        int suma = 0;
        for (int n : arr) suma += n;
        return suma;
    }

    // B020: Rotar array a la derecha
    static void rotarDerecha(int[] arr, int k) {
        k = k % arr.length;
        invertirParcial(arr, 0, arr.length - 1);
        invertirParcial(arr, 0, k - 1);
        invertirParcial(arr, k, arr.length - 1);
    }

    private static void invertirParcial(int[] arr, int izq, int der) {
        while (izq < der) {
            int temp = arr[izq]; arr[izq] = arr[der]; arr[der] = temp;
            izq++; der--;
        }
    }
}
