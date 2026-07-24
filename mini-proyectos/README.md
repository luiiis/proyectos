# 🧪 Mini-Proyectos de Aprendizaje — Funcionalidades Reales Aisladas

## Concepto
Cada mini-proyecto es **independiente** y resuelve UNA funcionalidad específica que usarás en el trabajo real. No necesitas hacer todos ni en orden.

---

## Lista de Mini-Proyectos

### 🔐 AUTENTICACIÓN Y SEGURIDAD
| # | Proyecto | Qué aprenderás |
|---|----------|-----------------|
| 01 | Login básico con sesión | Spring Security + formulario + sesión HTTP |
| 02 | Login con JWT | Token stateless + refresh token |
| 03 | Login con LDAP | Active Directory / OpenLDAP |
| 04 | Login con OAuth2 (Google) | Social login con proveedor externo |
| 05 | Login con Microsoft Entra ID | Identidad corporativa |
| 06 | Recuperar contraseña | Token por email + expiración + reset |
| 07 | Verificar email | Enviar link de confirmación al registrarse |
| 08 | MFA (autenticación 2 factores) | TOTP con Google Authenticator |
| 09 | Rate limiting | Limitar intentos de login (anti brute force) |
| 10 | Roles y permisos granulares | Matriz usuario→rol→permiso→recurso |

### 📧 CORREOS
| # | Proyecto | Qué aprenderás |
|---|----------|-----------------|
| 11 | Enviar email simple | Spring Mail + Gmail SMTP |
| 12 | Email con plantilla HTML | Thymeleaf templates para correos bonitos |
| 13 | Email con adjuntos | Enviar PDFs, imágenes |
| 14 | Cola de correos | RabbitMQ/Kafka → reintentos si falla |
| 15 | Notificaciones masivas | Enviar a 1000 usuarios sin bloquear |

### 📁 ARCHIVOS
| # | Proyecto | Qué aprenderás |
|---|----------|-----------------|
| 16 | Subir imágenes | Multipart upload + validación de tipo/tamaño |
| 17 | Subir a Amazon S3 | Cloud storage (o MinIO local) |
| 18 | Generar PDF | Factura/ticket/reporte en PDF (OpenPDF) |
| 19 | Exportar a Excel | Descargar datos como .xlsx (Apache POI) |
| 20 | Importar CSV/Excel | Leer archivo y cargar datos a BD |

### 📊 REPORTES
| # | Proyecto | Qué aprenderás |
|---|----------|-----------------|
| 21 | Reporte con filtros | Consultas dinámicas + paginación |
| 22 | Dashboard con KPIs | Endpoints de métricas + frontend con gráficas |
| 23 | Exportar reporte a PDF | Tabla de datos → PDF descargable |
| 24 | Programar reportes | Tarea automática cada día a las 6am (@Scheduled) |

### 🔔 NOTIFICACIONES
| # | Proyecto | Qué aprenderás |
|---|----------|-----------------|
| 25 | Notificaciones en tiempo real | WebSocket + STOMP |
| 26 | Push notifications | Firebase Cloud Messaging |
| 27 | Notificaciones in-app | Badge, bell icon, leídas/no leídas |

### 🗄️ BASE DE DATOS
| # | Proyecto | Qué aprenderás |
|---|----------|-----------------|
| 28 | Multi-datasource | Conectar a 2+ BDs desde un solo backend |
| 29 | Auditoría automática | Registrar quién hizo qué y cuándo |
| 30 | Soft delete + papelera | Eliminar lógico + restaurar |
| 31 | Versionado de registros | Historial de cambios en un registro |
| 32 | Full-text search | Búsqueda inteligente (no solo LIKE) |

### ⏰ TAREAS PROGRAMADAS
| # | Proyecto | Qué aprenderás |
|---|----------|-----------------|
| 33 | Tarea cada 5 minutos | @Scheduled + cron expressions |
| 34 | Limpiar tokens expirados | Job automático de mantenimiento |
| 35 | Generar respaldos automáticos | Backup de BD cada noche |
| 36 | Enviar resumen diario | Email automático con métricas del día |

### 🌐 INTEGRACIÓN CON SERVICIOS EXTERNOS
| # | Proyecto | Qué aprenderás |
|---|----------|-----------------|
| 37 | Consumir API externa | RestClient + manejo de errores + retry |
| 38 | Pasarela de pagos | Stripe/MercadoPago (sandbox) |
| 39 | Maps/Geolocalización | Google Maps API + calcular distancias |
| 40 | Chatbot con IA | OpenAI API + Spring AI |

### 🏗️ PATRONES Y ARQUITECTURA
| # | Proyecto | Qué aprenderás |
|---|----------|-----------------|
| 41 | Event-driven | Publicar/escuchar eventos internos |
| 42 | Cache con Redis | @Cacheable + TTL + invalidación |
| 43 | Circuit Breaker | Resilience4j + fallback |
| 44 | API Gateway | Rutear a múltiples servicios |
| 45 | Feature flags | Activar/desactivar funciones sin deploy |

### 📱 FRONTEND ESPECÍFICO
| # | Proyecto | Qué aprenderás |
|---|----------|-----------------|
| 46 | DataTable con server-side | Paginación + sort + filter desde la BD |
| 47 | Drag & Drop | Reordenar elementos + guardar posición |
| 48 | Wizard (formulario multi-paso) | Stepper con validaciones por paso |
| 49 | Dark mode | Tema claro/oscuro con CSS variables |
| 50 | Dashboard responsive | Grid de cards + gráficas + mobile |

---

## Estructura de cada mini-proyecto

```
mini-proyectos/
├── 01-login-sesion/
│   ├── README.md          ← Qué hace, conceptos, cómo funciona
│   ├── COMO_EJECUTAR.md   ← Paso a paso
│   ├── backend/           ← Código Spring Boot
│   └── frontend/          ← Código Angular (si aplica)
├── 02-login-jwt/
│   ├── ...
```

---

## ¿Cuáles hacer primero?

### Si eres PRINCIPIANTE (ya hiciste el roadmap hasta nivel 5):
```
01 → 02 → 06 → 11 → 16 → 21 → 28
(Login → JWT → Recovery → Email → Upload → Reportes → Multi-BD)
```

### Si quieres prepararte para ENTREVISTAS:
```
02 → 06 → 10 → 18 → 20 → 29 → 42
(JWT → Recovery → Permisos → PDF → Import → Auditoría → Cache)
```

### Si quieres construir un PRODUCTO real:
```
02 → 10 → 06 → 07 → 11 → 16 → 18 → 25 → 29 → 33
(JWT → Permisos → Recovery → Verificar email → Email → Upload → PDF → WebSocket → Audit → Scheduled)
```

---

## Reglas
1. Cada mini-proyecto funciona INDEPENDIENTE (no depende de otros)
2. Se puede completar en 1-3 días
3. Tiene documentación y explicación de cada línea
4. Se puede integrar después en el proyecto ERP del roadmap
