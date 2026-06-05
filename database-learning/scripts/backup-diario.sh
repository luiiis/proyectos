#!/bin/bash
# ════════════════════════════════════════════════════════════════
# Script de Backup Diario - PostgreSQL
# ════════════════════════════════════════════════════════════════
# Uso: ./scripts/backup-diario.sh
# Requisito: Docker corriendo con el contenedor learn-postgres
# ════════════════════════════════════════════════════════════════

# Configuración
CONTAINER="learn-postgres"
DB_NAME="empresa_db"
DB_USER="postgres"
BACKUP_DIR="./backups"
RETENTION_DAYS=7
FECHA=$(date +%Y%m%d_%H%M%S)

# Crear directorio de backups si no existe
mkdir -p $BACKUP_DIR

echo "═══════════════════════════════════════════"
echo "  Backup PostgreSQL - $FECHA"
echo "═══════════════════════════════════════════"

# Verificar que el contenedor está corriendo
if ! docker ps --format '{{.Names}}' | grep -q $CONTAINER; then
    echo "❌ ERROR: Contenedor $CONTAINER no está corriendo"
    echo "   Ejecuta: docker compose up -d"
    exit 1
fi

# Ejecutar backup
echo "📦 Creando backup..."
docker exec $CONTAINER pg_dump -U $DB_USER -Fc $DB_NAME > "$BACKUP_DIR/backup_${FECHA}.dump"

if [ $? -eq 0 ]; then
    TAMAÑO=$(du -h "$BACKUP_DIR/backup_${FECHA}.dump" | cut -f1)
    echo "✅ Backup creado: backup_${FECHA}.dump ($TAMAÑO)"
else
    echo "❌ ERROR: Falló el backup"
    exit 1
fi

# Eliminar backups antiguos
echo "🗑️  Eliminando backups de más de $RETENTION_DAYS días..."
ELIMINADOS=$(find $BACKUP_DIR -name "*.dump" -mtime +$RETENTION_DAYS -delete -print | wc -l)
echo "   $ELIMINADOS archivos eliminados"

# Resumen
echo ""
echo "═══════════════════════════════════════════"
echo "  Resumen:"
echo "  - Archivo: $BACKUP_DIR/backup_${FECHA}.dump"
echo "  - Tamaño: $TAMAÑO"
echo "  - Retención: $RETENTION_DAYS días"
echo "═══════════════════════════════════════════"
echo ""
echo "Para restaurar:"
echo "  docker exec -i $CONTAINER pg_restore -U $DB_USER -d nueva_bd < $BACKUP_DIR/backup_${FECHA}.dump"
