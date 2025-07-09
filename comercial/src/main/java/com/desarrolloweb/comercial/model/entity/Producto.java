package com.desarrolloweb.comercial.model.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "productos")
public class Producto implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String descripcion;

    @Min(value = 0)
    @Max(value = 1000000)
    @NotNull
    private Integer existencia;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "500000000.0", inclusive = true)
    private Double precio;

    @Column(name = "ultimo_ingreso")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    @NotNull
    @Past
    private Date fechaUltIngreso;

    private boolean disponible;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Categoria categoria;

    public Producto() {
    }

    public Producto(Long id, String descripcion, Integer existencia, Double precio, Date fechaUltIngreso,
            boolean disponible, Categoria categoria) {
        this.id = id;
        this.descripcion = descripcion;
        this.existencia = existencia;
        this.precio = precio;
        this.fechaUltIngreso = fechaUltIngreso;
        this.disponible = disponible;
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getExistencia() {
        return existencia;
    }

    public void setExistencia(Integer existencia) {
        this.existencia = existencia;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Date getFechaUltIngreso() {
        return fechaUltIngreso;
    }

    public void setFechaUltIngreso(Date fechaUltIngreso) {
        this.fechaUltIngreso = fechaUltIngreso;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "{descripción: " + descripcion + ", precio: " + precio + ", existencia: " + existencia + " ...}";
    }
}
