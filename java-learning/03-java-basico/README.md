# Módulo 03: Java Básico - Variables, Operadores, Control de Flujo

## 1. Variables y Tipos de Datos

### Tipos Primitivos (8 tipos)

```java
// Enteros
byte edad = 25;           // 8 bits  (-128 a 127)
short año = 2026;         // 16 bits (-32,768 a 32,767)
int poblacion = 130000000; // 32 bits (-2^31 a 2^31-1) ← MÁS USADO
long distancia = 9_460_730_472_580L; // 64 bits (agregar L al final)

// Decimales
float pi = 3.14f;         // 32 bits (agregar f al final) - poca precisión
double precio = 18999.99; // 64 bits ← MÁS USADO para decimales

// Carácter
char letra = 'A';         // 16 bits (un solo carácter Unicode)

// Booleano
boolean activo = true;    // true o false
```

### Tipos de Referencia (Objetos)
```java
String nombre = "Carlos";          // Cadena de texto (NO es primitivo)
Integer numero = 42;               // Wrapper de int (puede ser null)
int[] arreglo = {1, 2, 3, 4, 5};  // Array
```

### var (Java 10+) - Inferencia de tipo
```java
var mensaje = "Hola";      // Java infiere que es String
var precio = 99.99;        // Java infiere que es double
var lista = new ArrayList<String>(); // Java infiere el tipo

// var solo funciona en variables locales (no en campos de clase)
// El tipo se determina en COMPILACIÓN (sigue siendo tipado estático)
```

### Constantes
```java
final double IVA = 0.16;           // No se puede cambiar después
final String EMPRESA = "TechCorp"; // Convención: MAYÚSCULAS_CON_GUION
static final int MAX_INTENTOS = 3; // Constante de clase
```

---

## 2. Operadores

```java
// Aritméticos
int suma = 10 + 3;      // 13
int resta = 10 - 3;     // 7
int mult = 10 * 3;      // 30
int div = 10 / 3;       // 3 (división entera, trunca decimales)
int mod = 10 % 3;       // 1 (residuo/módulo)
double divReal = 10.0 / 3; // 3.333...

// Asignación compuesta
int x = 10;
x += 5;   // x = x + 5 → 15
x -= 3;   // x = x - 3 → 12
x *= 2;   // x = x * 2 → 24
x /= 4;   // x = x / 4 → 6

// Incremento/Decremento
int i = 5;
i++;       // i = 6 (post-incremento)
++i;       // i = 7 (pre-incremento)
i--;       // i = 6

// Comparación (devuelven boolean)
10 == 10   // true (igual)
10 != 5    // true (diferente)
10 > 5     // true
10 >= 10   // true
10 < 20    // true

// Lógicos
true && true   // true (AND: ambos deben ser true)
true || false  // true (OR: al menos uno true)
!true          // false (NOT: invierte)

// Ternario (if en una línea)
String resultado = (edad >= 18) ? "Mayor" : "Menor";
```

---

## 3. Control de Flujo

### if / else if / else
```java
int nota = 85;

if (nota >= 90) {
    System.out.println("Excelente");
} else if (nota >= 80) {
    System.out.println("Bueno");
} else if (nota >= 70) {
    System.out.println("Regular");
} else {
    System.out.println("Reprobado");
}
```

### switch (clásico y moderno)
```java
// Switch clásico
String dia = "LUNES";
switch (dia) {
    case "LUNES":
    case "MARTES":
        System.out.println("Inicio de semana");
        break;
    case "VIERNES":
        System.out.println("¡Por fin viernes!");
        break;
    default:
        System.out.println("Otro día");
}

// Switch moderno (Java 14+) - sin break, con arrow
String tipo = switch (dia) {
    case "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES" -> "Laboral";
    case "SABADO", "DOMINGO" -> "Fin de semana";
    default -> "Desconocido";
};
```

### Ciclos
```java
// for clásico
for (int i = 0; i < 10; i++) {
    System.out.println("Iteración: " + i);
}

// for-each (para colecciones y arrays)
String[] frutas = {"Manzana", "Banana", "Naranja"};
for (String fruta : frutas) {
    System.out.println(fruta);
}

// while
int contador = 0;
while (contador < 5) {
    System.out.println("Contador: " + contador);
    contador++;
}

// do-while (se ejecuta al menos 1 vez)
int intentos = 0;
do {
    System.out.println("Intento: " + intentos);
    intentos++;
} while (intentos < 3);
```

### break y continue
```java
// break: salir del ciclo
for (int i = 0; i < 100; i++) {
    if (i == 10) break;  // Sale cuando i llega a 10
    System.out.println(i);
}

// continue: saltar a la siguiente iteración
for (int i = 0; i < 10; i++) {
    if (i % 2 == 0) continue;  // Salta los pares
    System.out.println(i);  // Solo imprime impares: 1, 3, 5, 7, 9
}
```

---

## 4. Entrada de Datos (Scanner)

```java
import java.util.Scanner;

public class EntradaDatos {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("¿Cómo te llamas? ");
        String nombre = scanner.nextLine();

        System.out.print("¿Cuántos años tienes? ");
        int edad = scanner.nextInt();

        System.out.print("¿Cuál es tu estatura? ");
        double estatura = scanner.nextDouble();

        System.out.printf("Hola %s, tienes %d años y mides %.2f m%n", nombre, edad, estatura);

        scanner.close();
    }
}
```

---

## 5. Strings (Cadenas de texto)

```java
String nombre = "Carlos";
String apellido = "García";

// Concatenación
String completo = nombre + " " + apellido;  // "Carlos García"

// Métodos útiles
nombre.length();              // 6
nombre.toUpperCase();         // "CARLOS"
nombre.toLowerCase();         // "carlos"
nombre.charAt(0);             // 'C'
nombre.substring(0, 3);       // "Car"
nombre.contains("ar");        // true
nombre.startsWith("Ca");      // true
nombre.replace("a", "@");     // "C@rlos"
nombre.trim();                // Quita espacios al inicio/final
nombre.isEmpty();             // false
nombre.equals("Carlos");      // true (comparar contenido)
nombre == "Carlos";           // ⚠️ Compara REFERENCIA, no contenido

// String formatting
String msg = String.format("Hola %s, tienes %d años", nombre, 25);
// O con text blocks (Java 15+):
String html = """
        <html>
            <body>
                <h1>Hola %s</h1>
            </body>
        </html>
        """.formatted(nombre);
```

---

## 6. Arrays

```java
// Declarar y crear
int[] numeros = new int[5];        // Array de 5 enteros (inicializados en 0)
String[] nombres = {"Ana", "Luis", "María"};  // Con valores iniciales

// Acceder y modificar
numeros[0] = 10;
numeros[1] = 20;
System.out.println(numeros[0]);    // 10
System.out.println(nombres.length); // 3

// Recorrer
for (int i = 0; i < numeros.length; i++) {
    System.out.println(numeros[i]);
}

// Arrays multidimensionales (matrices)
int[][] matriz = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
};
System.out.println(matriz[1][2]); // 6 (fila 1, columna 2)
```

---

## 7. Ejercicios (100 ejercicios progresivos)

### Nivel 1: Variables y Operadores (1-25)
1. Declara variables para almacenar: nombre, edad, estatura, es_estudiante
2. Calcula el área de un círculo dado el radio
3. Convierte grados Celsius a Fahrenheit
4. Calcula el IMC (peso / estatura²)
5. Determina si un número es par o impar
6. Intercambia el valor de dos variables sin usar una tercera
7. Calcula el precio final con IVA (16%)
8. Determina el mayor de 3 números
9. Calcula la hipotenusa de un triángulo rectángulo
10. Convierte segundos a horas:minutos:segundos

### Nivel 2: Control de Flujo (26-50)
11. Determina si un año es bisiesto
12. Calculadora básica (+, -, *, /) con switch
13. Determina la calificación en letra (A, B, C, D, F) dada una nota numérica
14. Imprime los números del 1 al 100 que sean divisibles por 3 y 5
15. FizzBuzz: imprime Fizz (múltiplo de 3), Buzz (múltiplo de 5), FizzBuzz (ambos)
16. Tabla de multiplicar de un número dado
17. Suma de los primeros N números naturales
18. Factorial de un número
19. Determina si un número es primo
20. Serie Fibonacci hasta N términos

### Nivel 3: Strings y Arrays (51-75)
21. Cuenta vocales y consonantes en una cadena
22. Invierte un string sin usar StringBuilder.reverse()
23. Determina si un string es palíndromo
24. Cuenta palabras en una oración
25. Encuentra el elemento mayor y menor de un array
26. Ordena un array de enteros (implementa bubble sort)
27. Busca un elemento en un array (búsqueda lineal y binaria)
28. Elimina duplicados de un array
29. Rota un array N posiciones a la derecha
30. Encuentra el segundo número más grande

### Nivel 4: Programas Completos (76-100)
31. Sistema de cajero: depositar, retirar, consultar saldo
32. Juego de adivinanza (número aleatorio, el usuario intenta adivinar)
33. Generador de contraseñas aleatorias
34. Conversor de unidades (km↔mi, kg↔lb, °C↔°F)
35. Validador de CURP/RFC (formato básico)
36. Cifrado César (encriptar/desencriptar texto)
37. Calculadora de propinas (15%, 18%, 20%)
38. Sistema de calificaciones (promedio, máximo, mínimo, aprobados)
39. Generador de triángulo de Pascal
40. Juego de piedra, papel o tijera vs la computadora

---

## Siguiente Módulo
→ [04-POO](../04-poo/README.md)
