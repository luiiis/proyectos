# Cómo Ejecutar — Mini-Proyecto 16: Subir Imágenes

## Con Docker
```cmd
cd mini-proyectos/16-subir-imagenes
docker compose up -d
```

## Probar
```bash
# Subir imagen
curl -X POST http://localhost:8080/api/archivos/subir -F "file=@foto.jpg"

# Listar archivos subidos
curl http://localhost:8080/api/archivos/listar

# Ver imagen: abrir en navegador la URL devuelta
```

## Con Postman
1. POST → http://localhost:8080/api/archivos/subir
2. Body → form-data → Key: file (tipo File) → seleccionar imagen
3. Send

## Parar
```cmd
docker compose down
```
