# Preguntas de Entrevista - Tema: Java Básico

## Junior
1. ¿Cuál es la diferencia entre `int` y `Integer`?
2. ¿Qué es un Record en Java? ¿Cuándo usarlo?
3. ¿Qué pasa si divides por cero en Java?
4. ¿Cuál es la diferencia entre `==` y `.equals()`?
5. ¿Qué es un `switch expression` vs `switch statement`?

## Respuestas
1. `int` es primitivo (no puede ser null, vive en stack). `Integer` es objeto wrapper (puede ser null, vive en heap). Usar `Integer` cuando necesitas null o colecciones genéricas.
2. Record es una clase inmutable que genera automáticamente constructor, getters, equals, hashCode y toString. Usarlo para DTOs y contenedores de datos.
3. Con `int`: lanza `ArithmeticException`. Con `double`: devuelve `Infinity` o `NaN`.
4. `==` compara referencias (misma dirección de memoria). `.equals()` compara contenido/valor.
5. Expression retorna un valor y no necesita break. Statement es el clásico con break y fall-through.
