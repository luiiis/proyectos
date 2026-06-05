package com.erp.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "ventas")
public class Venta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(unique = true, nullable = false) private String numero;
    private Instant fecha;
    @Column(name = "cliente_id") private Integer clienteId;
    @Column(name = "usuario_id") private Integer usuarioId;
    private BigDecimal subtotal = BigDecimal.ZERO;
    private BigDecimal impuesto = BigDecimal.ZERO;
    private BigDecimal total = BigDecimal.ZERO;
    private String estado = "COMPLETADA";
    @Column(name = "metodo_pago") private String metodoPago = "EFECTIVO";
    @PrePersist void onCreate() { fecha = Instant.now(); }

    public Long getId() { return id; }
    public String getNumero() { return numero; }
    public void setNumero(String n) { this.numero = n; }
    public Instant getFecha() { return fecha; }
    public Integer getClienteId() { return clienteId; }
    public void setClienteId(Integer c) { this.clienteId = c; }
    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer u) { this.usuarioId = u; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal s) { this.subtotal = s; }
    public BigDecimal getImpuesto() { return impuesto; }
    public void setImpuesto(BigDecimal i) { this.impuesto = i; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal t) { this.total = t; }
    public String getEstado() { return estado; }
    public void setEstado(String e) { this.estado = e; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String m) { this.metodoPago = m; }
}
