package com.erp.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_venta")
public class DetalleVenta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "venta_id") private Long ventaId;
    @Column(name = "producto_id") private Long productoId;
    private int cantidad;
    @Column(name = "precio_unitario") private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public Long getId() { return id; }
    public Long getVentaId() { return ventaId; }
    public void setVentaId(Long v) { this.ventaId = v; }
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long p) { this.productoId = p; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int c) { this.cantidad = c; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal p) { this.precioUnitario = p; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal s) { this.subtotal = s; }
}
