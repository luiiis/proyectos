# Cómo Ejecutar — Mini-Proyecto 20: Importar Excel/CSV

## Con Docker
```cmd
cd mini-proyectos/20-importar-excel
docker compose up -d
```

## Probar
```bash
# Importar CSV
curl -X POST http://localhost:8080/api/importar/productos -F "file=@productos.csv"

# Importar Excel
curl -X POST http://localhost:8080/api/importar/productos -F "file=@productos.xlsx"
```

## CSV de ejemplo (crear archivo productos.csv)
```csv
nombre,precio,existencia,categoria
Laptop HP,18999.00,25,Electrónica
Mouse Logitech,1899.00,50,Periféricos
Monitor Dell,12499.00,15,Electrónica
```

## Parar
```cmd
docker compose down
```
