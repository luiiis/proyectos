# Cómo Ejecutar - Proyecto 01: Calculadora

## Requisitos
- Java 21+ instalado (`java -version`)

## Compilar y Ejecutar

```bash
cd academia-profesional/proyecto-01-calculadora/src

# Compilar
javac Calculadora.java

# Ejecutar
java Calculadora
```

## Uso
```
═══ CALCULADORA JAVA ═══
1. Sumar
2. Restar
3. Multiplicar
4. Dividir
5. Potencia
6. Ver historial
7. Salir
Opción: 1
Primer número: 25
Segundo número: 17
Resultado: 25.0 + 17.0 = 42.0
```

## Solución de errores

| Error | Causa | Solución |
|-------|-------|----------|
| `javac: command not found` | Java no instalado | Instalar JDK 21 |
| `InputMismatchException` | Escribiste texto en vez de número | El programa lo maneja con try/catch |
