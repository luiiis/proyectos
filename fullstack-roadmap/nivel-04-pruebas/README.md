# Nivel 4: Pruebas Automatizadas — Proyecto 4: Productos con Pruebas

## Objetivo
Agregar pruebas automatizadas al proyecto de Productos. Aprender testing profesional.

## Tipos de prueba
| Tipo | Qué prueba | Herramienta |
|------|-----------|-------------|
| Unitaria | Lógica del Service (sin BD) | JUnit 5 + Mockito |
| Service | Service con mocks del Repository | Mockito |
| Controller | Endpoints HTTP (sin levantar servidor) | MockMvc |
| Integración | Todo junto (con BD real) | Spring Boot Test |
| Repository | Queries de MyBatis | Testcontainers |
| Manual | Flujos completos | Postman |

## Herramientas
- JUnit 5
- Mockito
- Spring Boot Test
- MockMvc
- Testcontainers (BD en Docker para tests)
- JaCoCo (reporte de cobertura)

## Documentación
- Plan de pruebas
- Casos de prueba (entrada, resultado esperado, resultado obtenido)
- Reporte de cobertura (meta: 80%)
- Registro de defectos encontrados
