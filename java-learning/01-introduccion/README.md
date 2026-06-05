# Módulo 01: Introducción a Java

## 1. ¿Qué es Java?

Java es un lenguaje de programación de propósito general, orientado a objetos, diseñado para ser portable ("Write Once, Run Anywhere").

### Historia
- **1995**: Sun Microsystems lanza Java 1.0 (James Gosling)
- **2006**: Java se hace open source
- **2010**: Oracle compra Sun Microsystems
- **2017**: Java adopta ciclo de releases cada 6 meses
- **2021**: Java 17 (LTS)
- **2023**: Java 21 (LTS)
- **2025**: Java 25 (LTS) ← Versión actual recomendada

### ¿Por qué Java en 2026?
- **#1 en enterprise**: Banca, gobierno, telecomunicaciones
- **Demanda laboral**: Millones de ofertas globalmente
- **Ecosistema maduro**: Spring Boot, Hibernate, Kafka, etc.
- **Performance**: Virtual Threads, GraalVM Native
- **Evolución constante**: Records, Pattern Matching, Sealed Classes

---

## 2. JVM, JRE, JDK - ¿Cuál es la diferencia?

```
┌─────────────────────────────────────────────────────────┐
│                        JDK                               │
│  (Java Development Kit)                                  │
│  Todo lo que necesitas para DESARROLLAR                   │
│                                                          │
│  ┌─────────────────────────────────────────────────┐    │
│  │                    JRE                           │    │
│  │  (Java Runtime Environment)                      │    │
│  │  Todo lo que necesitas para EJECUTAR              │    │
│  │                                                  │    │
│  │  ┌─────────────────────────────────────────┐    │    │
│  │  │              JVM                         │    │    │
│  │  │  (Java Virtual Machine)                  │    │    │
│  │  │  La "máquina" que ejecuta bytecode       │    │    │
│  │  │                                          │    │    │
│  │  │  • Class Loader                          │    │    │
│  │  │  • Bytecode Verifier                     │    │    │
│  │  │  • JIT Compiler                          │    │    │
│  │  │  • Garbage Collector                     │    │    │
│  │  │  • Memory Manager                        │    │    │
│  │  └─────────────────────────────────────────┘    │    │
│  │                                                  │    │
│  │  + Librerías estándar (java.lang, java.util...)  │    │
│  └─────────────────────────────────────────────────┘    │
│                                                          │
│  + Compilador (javac)                                    │
│  + Debugger (jdb)                                        │
│  + Herramientas (jar, javadoc, jshell)                   │
└─────────────────────────────────────────────────────────┘
```

| Componente | Qué es | Quién lo necesita |
|-----------|--------|-------------------|
| JVM | Máquina virtual que ejecuta bytecode | Todos (incluido en JRE y JDK) |
| JRE | JVM + librerías estándar | Usuarios que solo ejecutan apps Java |
| JDK | JRE + compilador + herramientas | Desarrolladores (TÚ) |

---

## 3. ¿Cómo funciona Java? (Compilación + Ejecución)

```
TU CÓDIGO                COMPILACIÓN              EJECUCIÓN
─────────               ─────────────            ──────────

HolaMundo.java          javac                    java
┌──────────────┐        ┌──────────────┐        ┌──────────────┐
│ public class │  ───►  │ Bytecode     │  ───►  │    JVM       │
│ HolaMundo {  │        │ (.class)     │        │              │
│   main() {   │        │              │        │ Interpreta   │
│     print()  │        │ CAFEBABE...  │        │ el bytecode  │
│   }          │        │              │        │ y ejecuta    │
│ }            │        └──────────────┘        └──────────────┘
└──────────────┘                                       │
                                                       ▼
                                                 ┌──────────┐
                                                 │ "¡Hola!" │
                                                 └──────────┘

¿Por qué este proceso?
- El bytecode es PORTABLE (funciona en cualquier SO con JVM)
- Windows, Mac, Linux → misma .class funciona en todos
- La JVM optimiza en runtime (JIT Compiler)
```

---

## 4. Tu Primer Programa

```java
// HolaMundo.java
// Todo programa Java necesita al menos una clase con un método main

public class HolaMundo {
    // main: punto de entrada del programa
    // public: accesible desde fuera
    // static: no necesita crear un objeto para ejecutarse
    // void: no devuelve nada
    // String[] args: argumentos de línea de comandos
    public static void main(String[] args) {
        System.out.println("¡Hola, Mundo!");
        // System: clase del sistema
        // out: stream de salida (la consola)
        // println: print line (imprime + salto de línea)
    }
}
```

### Compilar y ejecutar:
```bash
# Compilar (genera HolaMundo.class)
javac HolaMundo.java

# Ejecutar (la JVM lee el .class)
java HolaMundo
# Output: ¡Hola, Mundo!

# O con Java 11+: ejecutar directamente (compila en memoria)
java HolaMundo.java
```

---

## 5. Características de Java

| Característica | Significado |
|---------------|-------------|
| Orientado a Objetos | Todo es un objeto (excepto primitivos) |
| Tipado estático | Declaras el tipo de cada variable en compilación |
| Portable | "Write Once, Run Anywhere" (gracias a la JVM) |
| Garbage Collection | La memoria se libera automáticamente |
| Multithreading | Soporte nativo para concurrencia |
| Seguro | Verificación de bytecode, sandbox, sin punteros |
| Robusto | Manejo de excepciones obligatorio, tipado fuerte |

---

## 6. Java vs Otros Lenguajes

| Aspecto | Java | Python | JavaScript | C# |
|---------|------|--------|------------|-----|
| Tipado | Estático fuerte | Dinámico | Dinámico | Estático fuerte |
| Velocidad | Rápido (JIT) | Lento | Medio (V8) | Rápido (CLR) |
| Uso principal | Backend enterprise | Data/ML/Scripts | Web full-stack | Backend .NET |
| Curva aprendizaje | Media | Baja | Baja-Media | Media |
| Demanda laboral | Muy alta | Alta | Muy alta | Alta |
| Verbosidad | Alta (mejorando) | Baja | Media | Media |

---

## 7. Versiones LTS (Long Term Support)

```
Java 8  (2014) ← Aún en uso en empresas legacy (lambdas, streams)
Java 11 (2018) ← Primer LTS post-Oracle (var, HTTP client)
Java 17 (2021) ← Records, sealed classes, pattern matching preview
Java 21 (2023) ← Virtual threads, sequenced collections
Java 25 (2025) ← Structured concurrency, string templates ← RECOMENDADA
```

**¿Qué versión usar?**
- Proyecto nuevo → Java 25 (o 21 mínimo)
- Proyecto existente → La que ya tenga (migrar gradualmente)
- Este curso → Java 21+ (compatible con todo lo que enseñamos)

---

## 8. Ejercicios

### Ejercicio 1.1
¿Cuál es la diferencia entre JDK, JRE y JVM? Explica con tus palabras.

### Ejercicio 1.2
¿Por qué Java es "Write Once, Run Anywhere"? ¿Qué componente lo hace posible?

### Ejercicio 1.3
Escribe, compila y ejecuta tu primer programa que imprima:
```
Mi nombre es [tu nombre]
Estoy aprendiendo Java en 2026
Java es un lenguaje [tu opinión]
```

### Ejercicio 1.4
¿Qué es bytecode? ¿Por qué Java no compila directamente a código máquina como C?

### Ejercicio 1.5
Investiga: ¿Qué empresas usan Java? Nombra al menos 5 y qué sistemas construyen con él.

---

## Siguiente Módulo
→ [02-Instalación](../02-instalacion/README.md)
