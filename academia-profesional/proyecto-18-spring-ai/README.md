# Proyecto 18: Integración con IA (Spring AI)

## ¿Qué construimos?
Un backend Spring Boot que integra modelos de lenguaje (OpenAI/Claude) para:
- Chatbot que responde preguntas sobre tus productos
- Recomendaciones inteligentes basadas en historial
- Búsqueda semántica (entender la INTENCIÓN, no solo palabras exactas)

## Tecnologías
- Spring Boot 3.3 + Spring AI 1.0
- OpenAI API (GPT-4) o Anthropic (Claude)
- pgvector (PostgreSQL con embeddings)
- RAG (Retrieval Augmented Generation)

## ¿Qué es RAG?
```
Problema: GPT no conoce TUS productos (solo sabe lo que aprendió en entrenamiento)
Solución RAG:
  1. Usuario pregunta: "¿tienen laptops con más de 16GB RAM?"
  2. Tu app BUSCA en tu BD productos relevantes (búsqueda semántica)
  3. Pasa esos productos + la pregunta a GPT
  4. GPT responde usando TUS datos: "Sí, tenemos la Laptop HP ProBook con 32GB..."
```

## Arquitectura
```
Usuario → "¿tienen laptops baratas?"
    │
    ▼
Spring Boot (Spring AI)
    │
    ├── 1. Convertir pregunta a EMBEDDING (vector numérico)
    │      "laptops baratas" → [0.23, -0.45, 0.67, ...]
    │
    ├── 2. Buscar en pgvector productos SIMILARES
    │      SELECT * FROM productos ORDER BY embedding <-> query_embedding LIMIT 5
    │
    ├── 3. Construir PROMPT con contexto
    │      "Basándote en estos productos: [Laptop HP $18999, Dell $12499...]
    │       Responde: ¿tienen laptops baratas?"
    │
    ├── 4. Enviar a OpenAI/Claude
    │
    └── 5. Responder al usuario
           "Sí, tenemos la Dell XPS por $12,499 que es nuestra opción más accesible..."
```
