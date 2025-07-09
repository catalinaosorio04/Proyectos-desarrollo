package com.desarrolloweb.comercial.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_identificacion", nullable = false, length = 50)
    @NotEmpty
    private String tipoIdentificacion;

    @NotNull
    private Long identificacion;

    @NotEmpty
    @Column(name = "nombre_completo", length = 60, nullable = false)
    private String nombreCompleto;

    @NotEmpty
    private String direccion;

    @NotEmpty
    private String telefono;

    @Column(name = "correo_electronico", length = 60)
    @NotEmpty
    @Email
    private String correoElectronico;

    @Column(name = "fecha_ingreso")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    @NotNull
    @Past
    private Date fechaIngreso;

    @Column(name = "capacidad_credito", nullable = false)
    @NotNull
    @Min(value = 0)
    @Max(value = 9000000)
    private Integer capacidadCredito;

    @Column(name = "aumento_cupo", nullable = true)
    private Integer aumentoCupo;

    @Column(nullable = false, columnDefinition = "varchar(255) default ''")
    private String imagen;

    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Factura> facturas;

    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Documento> documentos;

    public Cliente() {
        facturas = new ArrayList<>();
        documentos = new ArrayList<>();
    }

    public Cliente(Long id, String tipoIdentificacion, Long identificacion, String nombreCompleto, String direccion,
            String telefono, String correoElectronico, Date fechaIngreso, Integer capacidadCredito) {
        this.id = id;
        this.tipoIdentificacion = tipoIdentificacion;
        this.identificacion = identificacion;
        this.nombreCompleto = nombreCompleto;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.fechaIngreso = fechaIngreso;
        this.capacidadCredito = capacidadCredito;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public Long getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(Long identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Integer getCapacidadCredito() {
        return capacidadCredito;
    }

    public void setCapacidadCredito(Integer capacidadCredito) {
        this.capacidadCredito = capacidadCredito;
        this.aumentoCupo = (int) (capacidadCredito * 1.10);
    }

    public Integer getAumentoCupo() {
        return aumentoCupo != null ? aumentoCupo : 0;
    }

    public void setAumentoCupo(Integer aumentoCupo) {
        this.aumentoCupo = aumentoCupo;
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }

    public void addFactura(Factura factura) {
        this.facturas.add(factura);
    }

    public List<Documento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<Documento> documentos) {
        this.documentos = documentos;
    }

    public void addDocumentos(Documento documentos) {
        this.documentos.add(documentos);
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "{identificacion: " + identificacion + ", nombre completo: " + nombreCompleto + ", capacidad crédito: "
                + capacidadCredito + " ...}";
    }

}