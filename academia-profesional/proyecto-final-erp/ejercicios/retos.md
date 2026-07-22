# Ejercicios y Retos - Proyecto Final: ERP Empresarial

## Reto 1: Módulo de Compras
Implementa el flujo completo de compras a proveedores:
- CRUD de proveedores
- Crear orden de compra (productos + cantidades + proveedor)
- Al recibir mercancía → actualizar stock automáticamente
- Historial de compras por proveedor

**Lo que practicas:** Flujo completo, lógica de negocio, integración

---

## Reto 2: Sistema de Facturación
Genera facturas PDF automáticas al confirmar una venta:
- Datos fiscales del negocio y cliente
- Tabla de productos con cantidades y precios
- Subtotal, IVA, total
- Número de factura secuencial
- Endpoint: `GET /api/ventas/{id}/factura` → descarga PDF

**Lo que practicas:** Generación de documentos, formatos fiscales

---

## Reto 3: Multitenancy
Adapta el ERP para manejar múltiples empresas (tenants):
- Cada empresa ve SOLO sus datos
- Opción 1: schema por tenant (PostgreSQL schemas)
- Opción 2: discriminator column (tenant_id en cada tabla)
- El tenant se determina por el JWT del usuario logueado

**Lo que practicas:** Multitenancy, seguridad de datos, arquitectura

---

## Reto 4: Import/Export masivo
- Importar catálogo de productos desde archivo Excel/CSV
- Validar cada fila (precio > 0, nombre no vacío, categoría existe)
- Reportar errores fila por fila sin detener la importación
- Exportar cualquier listado a Excel con formato

**Lo que practicas:** Procesamiento batch, validación masiva, archivos

---

## Reto 5: Workflow de aprobaciones
Para ventas mayores a $50,000:
1. Vendedor registra la venta (estado: PENDIENTE)
2. Gerente recibe notificación (Kafka)
3. Gerente aprueba o rechaza
4. Si aprueba → se confirma la venta y descuenta stock
5. Si rechaza → se cancela y notifica al vendedor

**Lo que practicas:** State machine, workflows, eventos, roles

---

## Reto 6: API pública para integraciones
Crea una API pública (con API Keys, no JWT) para que sistemas externos consulten:
- Catálogo de productos (solo lectura)
- Estado de pedidos
- Rate limited a 1000 req/hora por API Key
- Documentada con OpenAPI/Swagger

**Lo que practicas:** API Keys, rate limiting, documentación, seguridad

---

## Reto 7: Testing completo
Agrega tests en todas las capas:
- Unit tests: services con Mockito
- Integration tests: repository con Testcontainers
- API tests: endpoints con MockMvc
- E2E: flujos completos con Playwright

Objetivo: >80% code coverage.

**Lo que practicas:** Testing pyramid, TDD, calidad de código

---

## Reto 8 (Avanzado): Blue-Green Deployment
Implementa despliegue sin downtime:
- 2 ambientes (Blue = actual, Green = nuevo)
- Despliega nueva versión en Green
- Verifica que funciona (health checks)
- Switch del load balancer de Blue a Green
- Si falla → revert inmediato a Blue

**Lo que practicas:** Deployment strategies, zero-downtime, producción

---

## Reto 9 (Avanzado): Chatbot de ventas con IA
Integra Spring AI para un chatbot que:
- Responda preguntas sobre productos ("¿tienen laptops con 32GB RAM?")
- Haga recomendaciones basadas en historial del cliente
- Pueda registrar ventas via conversación ("quiero comprar 2 teclados")
- Llame funciones del sistema (buscar stock, crear venta)

**Lo que practicas:** Spring AI, RAG, function calling, agentes

---

## Reto 10: Preparar para producción
Checklist completo antes de "ir a producción":
- [ ] Todos los endpoints protegidos con JWT + roles
- [ ] Rate limiting activo
- [ ] Logs estructurados (JSON)
- [ ] Métricas en Prometheus + dashboard en Grafana
- [ ] Alertas configuradas (error rate, latencia, disco)
- [ ] Backups automáticos de PostgreSQL
- [ ] SSL/HTTPS configurado
- [ ] Variables de entorno (no secrets hardcodeados)
- [ ] Docker images optimizadas (multi-stage, no-root)
- [ ] Documentación API (Swagger)
- [ ] README actualizado con instrucciones de deploy

**Lo que practicas:** Production readiness, checklist, profesionalismo
