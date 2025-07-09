package com.desarrolloweb.comercial.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "facturas")
public class Factura implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nro_factura")
	private Long nroFactura;
	
	@NotBlank
	@Column(name = "descripcion", nullable = false, length = 100)
	private String descripcion;
	
	// @NotNull // La fecha se guarda con @PrePersist
	@Column(name = "fecha_venta")
	@Temporal(TemporalType.DATE)
	private Date fechaVenta;
	
	private String observacion;

	@ManyToOne(fetch = FetchType.LAZY)
    private Cliente cliente;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "nro_factura")
	private List<Detalle> detalles;

	@PrePersist
	public void persistirFecha() {
		fechaVenta = new Date();
	}
		
	public Factura() {
		detalles = new ArrayList<>();
	}

	public Long getNroFactura() {
		return nroFactura;
	}

	public void setNroFactura(Long nroFactura) {
		this.nroFactura = nroFactura;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFechaVenta() {
		return fechaVenta;
	}

	public void setFechaVenta(Date fechaVenta) {
		this.fechaVenta = fechaVenta;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

	public List<Detalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<Detalle> detalles) {
		this.detalles = detalles;
	}

	public void addDetalle(Detalle detalle) {
		this.detalles.add(detalle);
	}
	
	public Double getSubtotal() {
		Double subtotal = 0.0;
		for (Detalle det : detalles) {
			subtotal += det.subtotalDetalle();
		}
		return subtotal;
	}
	
	@Override
	public String toString() {
		return "{nroFactura: " + nroFactura + ", descripcion: " + descripcion + ", fechaVenta: " + fechaVenta + "}";
	}
}