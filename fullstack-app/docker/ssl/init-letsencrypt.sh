#!/bin/bash
# ============================================================
# Script para obtener certificado SSL de Let's Encrypt
# ============================================================
#
# ¿Cuándo usar este script?
# - La PRIMERA VEZ que configuras SSL en tu servidor.
# - Después, la renovación es automática (cada 60 días).
#
# Requisitos:
# 1. Un dominio real apuntando a la IP de tu servidor (DNS configurado).
# 2. Puerto 80 abierto (Let's Encrypt necesita verificar tu dominio).
# 3. Docker y Docker Compose instalados.
#
# Uso:
#   chmod +x init-letsencrypt.sh
#   ./init-letsencrypt.sh
#

# ==================== CONFIGURACIÓN ====================
# CAMBIAR ESTOS VALORES:
DOMAIN="tudominio.com"
EMAIL="tu-email@ejemplo.com"  # Para notificaciones de expiración
STAGING=0  # 1 = modo prueba (no genera certificado real), 0 = producción

# ==================== EJECUCIÓN ====================

echo "### Obteniendo certificado SSL para $DOMAIN ..."

# Crear directorios necesarios
mkdir -p ./certbot/conf
mkdir -p ./certbot/www

# Si es modo staging (prueba), agregar flag
if [ $STAGING != "0" ]; then
    STAGING_ARG="--staging"
    echo "⚠️  Modo STAGING (certificado de prueba, no válido para producción)"
fi

# Ejecutar Certbot en Docker
docker run --rm \
    -v "$(pwd)/certbot/conf:/etc/letsencrypt" \
    -v "$(pwd)/certbot/www:/var/www/certbot" \
    -p 80:80 \
    certbot/certbot certonly \
    --standalone \
    --preferred-challenges http \
    -d $DOMAIN \
    -d www.$DOMAIN \
    --email $EMAIL \
    --agree-tos \
    --no-eff-email \
    $STAGING_ARG

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Certificado obtenido exitosamente!"
    echo ""
    echo "Archivos generados:"
    echo "  - Certificado: ./certbot/conf/live/$DOMAIN/fullchain.pem"
    echo "  - Clave privada: ./certbot/conf/live/$DOMAIN/privkey.pem"
    echo ""
    echo "Próximos pasos:"
    echo "  1. Actualizar nginx-ssl.conf con tu dominio"
    echo "  2. Montar los volúmenes de certbot en docker-compose.yml"
    echo "  3. Reiniciar: docker compose up -d"
    echo ""
    echo "Renovación automática (agregar a crontab):"
    echo "  0 0 1 * * docker run --rm -v \$(pwd)/certbot/conf:/etc/letsencrypt certbot/certbot renew"
else
    echo ""
    echo "❌ Error obteniendo certificado."
    echo "Verifica:"
    echo "  - Que el dominio $DOMAIN apunta a este servidor"
    echo "  - Que el puerto 80 está abierto"
    echo "  - Que no hay otro servicio usando el puerto 80"
fi
