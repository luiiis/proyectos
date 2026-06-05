# Bitácora de Construcción - Proyecto 01: Calculadora

## Paso 1: Definir el problema
**¿Qué necesitamos?** Una calculadora que:
- Reciba dos números y una operación
- Calcule el resultado
- Guarde historial
- Maneje errores

**¿Por qué este proyecto?** Porque cubre los fundamentos de Java sin complejidad innecesaria: variables, funciones, control de flujo, entrada/salida, colecciones básicas.

## Paso 2: Diseñar la solución
**Decisión:** Usar una clase `Calculadora` con métodos estáticos para operaciones y un `ArrayList` para historial.

**Alternativas consideradas:**
- Solo un `main` con todo → Descartado: no es mantenible
- Múltiples clases → Descartado: overkill para este nivel
- Una clase con métodos + record para historial → **Elegido**: balance entre simplicidad y buenas prácticas

## Paso 3: Implementar
**Orden de implementación:**
1. Operaciones básicas (sumar, restar, multiplicar, dividir)
2. Menú interactivo con Scanner
3. Validación de entrada
4. Historial con ArrayList
5. Record para representar cada operación

## Paso 4: Decisiones técnicas

| Decisión | Razón | Alternativa |
|----------|-------|-------------|
| Switch expression | Más conciso, retorna valor | if/else (más verbose) |
| Record para historial | Inmutable, toString automático | Clase con getters (más código) |
| ArrayList | Simple, suficiente para este caso | LinkedList (innecesario aquí) |
| Scanner | Estándar para consola | BufferedReader (más complejo) |

## Paso 5: Lecciones aprendidas
- Java es verboso pero explícito (sabes exactamente qué tipo tiene cada variable)
- Los Records simplifican mucho los DTOs/contenedores de datos
- Switch expressions (Java 14+) son mucho más limpios que switch clásico
- Siempre validar entrada del usuario (nunca confiar en el input)
