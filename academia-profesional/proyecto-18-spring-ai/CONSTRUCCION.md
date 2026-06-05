# Cómo se Construyó - Proyecto 18: Spring AI

## Paso 1: Entender qué es Spring AI
Spring AI es el framework OFICIAL de Spring para integrar modelos de lenguaje.
Abstrae la complejidad de llamar a OpenAI, Anthropic, Google, etc.

## Paso 2: Elegir el modelo
- GPT-4o-mini: barato ($0.15/1M tokens), rápido, suficiente para la mayoría
- GPT-4o: más inteligente pero más caro ($5/1M tokens)
- Claude 3.5 Sonnet: excelente para código y análisis

## Paso 3: Implementar RAG
```
RAG = Retrieval Augmented Generation

Sin RAG: "¿Tienen laptops?" → GPT inventa productos (alucinación)
Con RAG: "¿Tienen laptops?" → buscas en TU BD → pasas resultados a GPT → responde con datos REALES

Flujo:
1. Usuario pregunta
2. Buscar en BD productos relevantes (por ahora: query SQL simple)
3. Construir prompt: "Contexto: [productos encontrados]. Pregunta: [lo que preguntó]"
4. GPT responde BASÁNDOSE en tus datos (no inventa)
```

## Paso 4: pgvector (búsqueda semántica)
```
Búsqueda tradicional: WHERE nombre LIKE '%laptop%' (solo encuentra si dice "laptop")
Búsqueda semántica: "algo para trabajar desde casa" → encuentra laptops, monitores, sillas
  (entiende la INTENCIÓN, no solo palabras exactas)

Cómo funciona:
1. Cada producto se convierte en un VECTOR de 1536 números (embedding)
   "Laptop HP 32GB" → [0.23, -0.45, 0.67, 0.12, ...]
2. La pregunta también se convierte en vector
   "algo para programar" → [0.21, -0.43, 0.65, 0.14, ...]
3. Se buscan los vectores MÁS CERCANOS (cosine similarity)
   Laptop HP está "cerca" de "algo para programar" → se incluye en resultados
```

## Paso 5: Probar
```bash
# Chat simple
curl -X POST localhost:8080/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{"mensaje":"¿Qué me recomiendas para home office?"}'

# RAG (usa datos de TU BD)
curl -X POST localhost:8080/api/ai/consultar \
  -H "Content-Type: application/json" \
  -d '{"pregunta":"necesito algo para videollamadas con buena calidad"}'

# Recomendación con presupuesto
curl -X POST localhost:8080/api/ai/recomendar \
  -H "Content-Type: application/json" \
  -d '{"necesidad":"laptop para programar Java","presupuesto":25000}'
```

## Conceptos clave aprendidos
- **ChatClient**: interfaz para hablar con LLMs (OpenAI, Claude, etc.)
- **System prompt**: instrucciones que definen el "personaje" de la IA
- **User prompt**: lo que el usuario pregunta + contexto
- **Temperature**: 0 = determinista, 1 = creativo
- **Embedding**: representación numérica del significado de un texto
- **pgvector**: extensión de PostgreSQL para almacenar y buscar embeddings
- **RAG**: patrón que combina búsqueda en BD + generación con IA
