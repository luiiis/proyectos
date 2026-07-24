# Cómo Ejecutar — Mini-Proyecto 18: Generar PDF

## Con Docker
```cmd
cd mini-proyectos/18-generar-pdf
docker compose up -d
```

## Probar
```bash
# Descargar PDF
curl http://localhost:8080/api/reportes/ejemplo/pdf -o reporte.pdf

# O abrir en navegador:
http://localhost:8080/api/reportes/ejemplo/pdf
```

## Parar
```cmd
docker compose down
```
