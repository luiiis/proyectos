# Ejercicios y Retos - Proyecto 18: Spring AI

## Reto 1: Chat simple
Implementa un endpoint `POST /api/chat` que reciba un mensaje y devuelva la respuesta de GPT:
```java
@PostMapping("/chat")
public String chat(@RequestBody String mensaje) {
    return chatClient.prompt(mensaje).call().content();
}
```

**Lo que practicas:** Spring AI básico, ChatClient, prompts

---

## Reto 2: System Prompt personalizado
Configura un "personalidad" para tu chatbot:
```java
ChatClient.builder(chatModel)
    .defaultSystem("Eres un asistente de ventas de una tienda de tecnología. " +
                   "Responde solo sobre productos que tenemos. " +
                   "Si preguntan sobre otra cosa, redirige a productos.")
    .build();
```

**Lo que practicas:** System prompts, personalización, guardrails

---

## Reto 3: RAG con tus productos
Implementa búsqueda semántica sobre tu catálogo:
1. Al arrancar: generar embeddings de todos los productos → guardar en pgvector
2. Al preguntar: convertir pregunta a embedding → buscar similares → pasar a GPT

```java
// Buscar productos similares a la pregunta
List<Document> similares = vectorStore.similaritySearch(pregunta);
// Pasar como contexto a GPT
String contexto = similares.stream().map(Document::getContent).collect(joining("\n"));
```

**Lo que practicas:** RAG, embeddings, pgvector, búsqueda semántica

---

## Reto 4: Recomendaciones inteligentes
"El cliente compró una laptop. ¿Qué más le recomendarías?"
- Buscar compras del cliente en la BD
- Pasar historial + catálogo a GPT
- GPT sugiere productos complementarios con razón

**Lo que practicas:** Prompt engineering, contexto de negocio, personalización

---

## Reto 5: Análisis de sentimiento en reseñas
Crea endpoint que reciba texto de reseña y devuelva:
```json
{
  "sentimiento": "positivo",
  "confianza": 0.92,
  "aspectos": ["precio", "calidad"],
  "resumen": "Cliente satisfecho con la relación precio-calidad"
}
```

**Lo que practicas:** Output parsing, structured responses, análisis de texto

---

## Reto 6: Function Calling
Permite que GPT llame funciones de tu sistema:
```java
// GPT puede "decidir" buscar en tu BD:
@Bean
Function<BuscarProductoRequest, List<Producto>> buscarProductos() {
    return request -> productoRepo.findByNombreContaining(request.query());
}
```
Cuando el usuario pregunta "¿tienen laptops?", GPT LLAMA a tu función y usa el resultado.

**Lo que practicas:** Function calling, tool use, agentes IA

---

## Reto 7 (Avanzado): Streaming de respuestas
En vez de esperar toda la respuesta de GPT, envíala token por token (como ChatGPT):
```java
@GetMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public Flux<String> chatStream(@RequestParam String mensaje) {
    return chatClient.prompt(mensaje).stream().content();
}
```

**Lo que practicas:** Streaming, SSE, Flux, UX mejorada
