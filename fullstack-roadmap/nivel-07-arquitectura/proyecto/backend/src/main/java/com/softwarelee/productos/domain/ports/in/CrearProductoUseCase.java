package com.softwarelee.productos.domain.ports.in;

import com.softwarelee.productos.domain.model.Producto;

/**
 * PUERTO DE ENTRADA: Define el caso de uso "Crear Producto".
 *
 * Esta interface la define el DOMINIO.
 * La implementa la capa APPLICATION.
 * La invoca la capa INFRASTRUCTURE (controller).
 *
 * El dominio dice QUÉ se puede hacer, no CÓMO se hace.
 */
public interface CrearProductoUseCase {
    Producto ejecutar(Producto producto);
}
