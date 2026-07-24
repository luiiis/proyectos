# Estado de los Mini-Proyectos

## ✅ Completamente funcionales (código + docs + Docker):

| # | Proyecto | README | Manual Técnico | COMO_EJECUTAR | Código | Docker |
|---|----------|:------:|:--------------:|:-------------:|:------:|:------:|
| 01 | Login con Sesión | ✅ | ✅ | ✅ | ✅ | ✅ |
| 03 | Login LDAP | ✅ | ✅ | ✅ | ✅ | ✅ |
| 06 | Recuperar Password | ✅ | ✅ | ✅ | ✅ | ✅ |
| 11 | Enviar Email | ✅ | ✅ | ✅ | ✅ | ✅ |
| 16 | Subir Imágenes | ✅ | ✅ | ✅ | ✅ | ✅ |
| 18 | Generar PDF | ✅ | ✅ | ✅ | ✅ | ✅ |
| 20 | Importar Excel/CSV | ✅ | ✅ | ✅ | ✅ | ✅ |
| 29 | Auditoría Automática | ✅ | ✅ | ✅ | ✅ | ✅ |
| 42 | Caché Redis | ✅ | ✅ | ✅ | ✅ | ✅ |

## Levantar cualquier mini-proyecto:
```cmd
cd mini-proyectos/XX-nombre
docker compose up -d
```

## Por desarrollar (tienen descripción en el README principal):

| Categoría | Proyectos pendientes |
|-----------|---------------------|
| Autenticación | 02 (JWT), 04 (OAuth2 Google), 05 (Entra ID), 07 (Verificar email), 08 (MFA), 09 (Rate limit), 10 (Permisos) |
| Correos | 12 (Plantillas), 13 (Adjuntos), 14 (Cola), 15 (Masivos) |
| Archivos | 17 (S3/MinIO), 19 (Exportar Excel) |
| Reportes | 21-24 |
| Notificaciones | 25-27 |
| Base de datos | 28 (Multi-datasource), 30 (Soft delete), 31 (Versionado), 32 (Full-text) |
| Tareas programadas | 33-36 |
| Integración externa | 37-40 |
| Patrones | 41, 43-45 |
| Frontend | 46-50 |

---

## Para desarrollar un mini-proyecto faltante:

```
Quiero desarrollar el mini-proyecto [número]: [nombre].
Dame:
1. pom.xml completo
2. Todos los archivos Java con explicación
3. Configuración (application.yml)
4. SQL si necesita base de datos
5. docker-compose.yml
6. Dockerfile
7. COMO_EJECUTAR.md
8. MANUAL-TECNICO.md
```
