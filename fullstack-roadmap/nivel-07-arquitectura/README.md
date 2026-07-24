# Nivel 7: Arquitectura Hexagonal — Proyecto 7: Migración

## Objetivo
Reorganizar todo el código anterior en Arquitectura Hexagonal. Separar el dominio del framework.

## Estructura
```
src/main/java/com/softwarelee/productos
├── domain/                        ← El NEGOCIO (no depende de nada externo)
│   ├── model/                     ← Entidades de dominio
│   ├── ports/
│   │   ├── in/                    ← Puertos de entrada (interfaces de casos de uso)
│   │   └── out/                   ← Puertos de salida (interfaces de persistencia)
│   └── exception/                 ← Excepciones de dominio
├── application/                   ← ORQUESTACIÓN (implementa los casos de uso)
│   ├── usecase/                   ← Implementaciones de puertos in
│   ├── dto/                       ← Request/Response DTOs
│   └── mapper/                    ← Conversión Entity ↔ DTO
├── infrastructure/                ← MUNDO EXTERNO (frameworks, BD, HTTP)
│   ├── adapter/
│   │   ├── in/rest/               ← Controllers (adaptador HTTP)
│   │   └── out/
│   │       ├── persistence/       ← MyBatis Mappers (adaptador BD)
│   │       ├── email/             ← Servicio de correo (adaptador)
│   │       └── security/          ← Spring Security (adaptador)
│   └── configuration/             ← Beans, configs
└── Application.java
```

## Conceptos clave
| Concepto | Qué es | Ejemplo |
|----------|--------|---------|
| Dominio | Reglas de negocio PURAS | "Un producto no puede tener precio negativo" |
| Puerto de entrada | Interface que define un caso de uso | `CrearProductoUseCase` |
| Puerto de salida | Interface que define qué necesita del exterior | `ProductoRepository` |
| Adaptador de entrada | Implementación que recibe requests | `ProductoController` |
| Adaptador de salida | Implementación que accede a BD/correo | `ProductoMyBatisAdapter` |
| Caso de uso | Lógica de una operación específica | `CrearProductoUseCaseImpl` |

## Documentación
- Documento de arquitectura
- Diagrama de componentes
- Explicación de puertos y adaptadores
- Decisiones de arquitectura (ADRs)
- Convenciones del proyecto
