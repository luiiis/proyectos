# Cómo Ejecutar - Proyecto 18: Spring AI

## Prerrequisitos
- Java 21+
- Docker (para PostgreSQL + pgvector)
- API Key de OpenAI (https://platform.openai.com/api-keys) o Anthropic

## 1. Levantar infraestructura
```bash
cd academia-profesional/proyecto-18-spring-ai
docker compose up -d
```

## 2. Configurar API Key
```bash
# Crear archivo .env o exportar variable:
export OPENAI_API_KEY="sk-tu-api-key-aqui"
# O en application.yml: spring.ai.openai.api-key: sk-...
```

## 3. Ejecutar
```bash
mvn spring-boot:run
```

## 4. Probar
```bash
# Chat simple
curl -X POST http://localhost:8080/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{"mensaje": "¿Qué laptops tienen disponibles?"}'

# Recomendación basada en historial
curl -X POST http://localhost:8080/api/ai/recomendar \
  -H "Content-Type: application/json" \
  -d '{"clienteId": 1, "presupuesto": 20000}'

# Búsqueda semántica
curl "http://localhost:8080/api/ai/buscar?q=algo+para+trabajar+desde+casa"
```
