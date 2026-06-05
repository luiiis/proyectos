#!/bin/bash
# Script para inicializar SQL Server con los scripts SQL
# SQL Server no tiene un mecanismo de init como MySQL, así que usamos este script

echo "Esperando a que SQL Server esté listo..."
sleep 30

# Intentar conectar hasta que esté disponible
for i in {1..50}; do
    /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "$MSSQL_SA_PASSWORD" -C -Q "SELECT 1" > /dev/null 2>&1
    if [ $? -eq 0 ]; then
        echo "SQL Server está listo. Ejecutando scripts de inicialización..."
        break
    fi
    echo "Intento $i: SQL Server no está listo aún. Esperando 5 segundos..."
    sleep 5
done

# Ejecutar scripts en orden
for script in /scripts/*.sql; do
    if [ -f "$script" ]; then
        echo "Ejecutando: $script"
        /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "$MSSQL_SA_PASSWORD" -C -i "$script"
        if [ $? -eq 0 ]; then
            echo "✓ $script ejecutado exitosamente"
        else
            echo "✗ Error ejecutando $script"
        fi
    fi
done

echo "Inicialización de SQL Server completada."
