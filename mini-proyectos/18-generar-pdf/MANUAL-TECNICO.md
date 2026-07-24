# Manual Técnico — Mini-Proyecto 18: Generar PDF

## Stack
| Componente | Tecnología |
|-----------|-----------|
| Backend | Spring Boot 3.3 |
| PDF | OpenPDF 2.0 (fork libre de iText) |

## Endpoints
| Método | URL | Resultado |
|--------|-----|-----------|
| GET | /api/reportes/ejemplo/pdf | Descarga reporte.pdf |

## Cómo funciona
1. Controller recibe la petición
2. Configura response como `application/pdf`
3. Crea documento PDF en memoria con OpenPDF
4. Agrega título, fecha, tabla con datos
5. Escribe directamente al OutputStream de la respuesta HTTP
6. El navegador descarga el archivo automáticamente
