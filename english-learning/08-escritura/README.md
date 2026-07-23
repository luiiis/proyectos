# Módulo 08: Escritura — Emails, Commits, PRs, Code Reviews

## 1. Emails Profesionales

### Estructura de un email:
```
Subject: [tema claro y específico]

Hi [nombre],

[1-2 oraciones del punto principal]

[Detalles si son necesarios]

[Acción esperada / siguiente paso]

Best regards,
[Tu nombre]
```

### Ejemplo: Pedir ayuda
```
Subject: Need help with authentication issue

Hi Carlos,

I'm having trouble with the JWT token validation in the product service.
The token is generated correctly but the resource server rejects it
with a 401 error.

Could you take a look at the SecurityConfig.java when you have a chance?
I've pushed my changes to the branch feature/auth-fix.

Thanks in advance,
[Tu nombre]
```

### Ejemplo: Avisar de un problema
```
Subject: Production issue - API response time degradation

Hi team,

I noticed that API response times increased from ~200ms to ~2s
starting around 3 PM today.

I've checked the logs and it seems related to a slow database query
in the product search endpoint. I'm investigating and will update
within the hour.

No action needed from your side for now.

Best,
[Tu nombre]
```

### Frases útiles para emails:

| Para... | Usa |
|---------|-----|
| Empezar | "I hope this finds you well" (formal) / "Hey [nombre]," (informal) |
| Pedir algo | "Could you..." / "Would you mind..." / "I'd appreciate if..." |
| Informar | "I wanted to let you know..." / "Just a heads up..." |
| Agradecer | "Thank you for your help" / "I appreciate your time" |
| Disculpar | "Sorry for the delay" / "Apologies for the inconvenience" |
| Cerrar | "Let me know if you need anything else" / "Feel free to reach out" |
| Urgente | "This is urgent" / "Could you prioritize this?" |

---

## 2. Pull Requests (PRs)

### Título:
```
feat: add product search endpoint with pagination
fix: resolve memory leak in WebSocket handler
refactor: extract validation logic to separate service
docs: update API reference for auth endpoints
```

### Descripción (template):
```markdown
## What does this PR do?
Adds a search endpoint that allows filtering products by name, 
category, and price range with server-side pagination.

## Why?
Users reported that finding products in a catalog of 10K+ items 
was too slow. This implements server-side search to improve UX.

## How to test
1. Start the backend: `mvn spring-boot:run`
2. GET http://localhost:8080/api/products/search?q=laptop&page=0&size=10
3. Verify pagination metadata in response

## Checklist
- [x] Tests pass
- [x] No new warnings
- [x] Documentation updated
- [ ] Reviewed by at least 1 person
```

---

## 3. Code Review Comments

### Aprobar:
```
"LGTM! Clean implementation."
"Looks good. Nice use of the Builder pattern here."
"Approved. Small nit below but not blocking."
```

### Sugerir cambio:
```
"Suggestion: consider using Optional here to avoid the null check"
"Nit: this variable name could be more descriptive (e.g., 'activeProducts' instead of 'list')"
"Could we extract this to a separate method? It would improve readability."
```

### Pedir explicación:
```
"Question: why did you choose ConcurrentHashMap over HashMap here?"
"I'm not sure I understand the logic in lines 45-52. Could you add a comment?"
"Is there a reason we're not using the existing ProductMapper?"
```

### Reportar problema:
```
"Bug: this will throw NPE if 'category' is null"
"This could be a performance issue with large datasets - consider adding pagination"
"Security concern: user input is not validated before passing to the query"
```

---

## 4. Commit Messages

### Formato (Conventional Commits):
```
type(scope): short description

Body: explain WHAT and WHY (not HOW)

Footer: references
```

### Tipos:
| Tipo | Cuándo | Ejemplo |
|------|--------|---------|
| feat | Nueva funcionalidad | feat(auth): add refresh token endpoint |
| fix | Arreglar un bug | fix(products): resolve null pointer in search |
| refactor | Cambio sin agregar/quitar feature | refactor(service): simplify validation logic |
| docs | Solo documentación | docs: update README with setup instructions |
| test | Agregar/arreglar tests | test(orders): add integration tests for checkout |
| chore | Tareas de mantenimiento | chore: upgrade Spring Boot to 3.3.5 |
| perf | Mejora de rendimiento | perf(db): add index for product search |
| style | Formato (no cambia lógica) | style: format code with prettier |

---

## 5. Slack/Chat (comunicación rápida)

### Informar:
```
"Deployed v2.5.0 to staging ✅"
"The nightly build failed — investigating"
"Fixed the login issue. Please verify on staging"
"Going to lunch, back in 30"
```

### Pedir:
```
"@carlos can you review my PR when you get a chance?"
"Does anyone know how to configure Redis for session storage?"
"Quick question: should we use POST or PUT for this endpoint?"
```

### Responder:
```
"On it!" (¡En eso estoy!)
"Got it, thanks" (Entendido, gracias)
"Will do" (Lo haré)
"Makes sense" (Tiene sentido)
"+1" (De acuerdo)
"Let me check and get back to you" (Déjame revisar y te aviso)
```
