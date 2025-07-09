package com.desarrolloweb.comercial.model.entity;

import java.time.LocalDateTime;
import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "documentos")
public class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_doc")
    private Long id;

    // @NotBlank
    @Column(name = "nombre_doc", nullable = false, columnDefinition = "varchar(255) default ''")
    private String nombreDocumento;

    @NotBlank
    @Column(name = "descripcion", nullable = false, length = 100)
    private String descripcion;

    @Column(name = "tipo_archivo", nullable = false, length = 100)
    private String tipoArchivo;

    @Column(name = "fecha_carga")
    private LocalDateTime fechaCarga;

    private int tamanio;

    private String estado;

    @Column(name = "fecha_crear")
    private Date fechaCreacion;

    @Column(name = "fecha_modi")
    private Date fechaModificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente cliente;

    @PrePersist
    public void persistirFechaCarga() {
        LocalDateTime ahora = LocalDateTime.now();
        this.fechaCarga = ahora;
  
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public int getTamanio() {
        return tamanio;
    }

    public void setTamanio(int tamanio) {
        this.tamanio = tamanio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setFechaCarga(LocalDateTime fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public LocalDateTime getFechaCarga() {
       return fechaCarga;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

}
