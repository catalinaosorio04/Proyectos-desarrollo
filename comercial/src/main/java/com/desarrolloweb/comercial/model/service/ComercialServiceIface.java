package com.desarrolloweb.comercial.model.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.desarrolloweb.comercial.model.entity.Categoria;
import com.desarrolloweb.comercial.model.entity.Cliente;
import com.desarrolloweb.comercial.model.entity.Documento;
import com.desarrolloweb.comercial.model.entity.Factura;
import com.desarrolloweb.comercial.model.entity.Producto;

public interface ComercialServiceIface {

    // servicios para Producto
    Page<Producto> buscarProductosTodos(Pageable pageable);
    public List<Producto> buscarProductosTodos();
    public void guardarProducto(Producto producto);
    public Producto buscarProductoPorId(Long id);
    public void eliminarProductoPorId(Long id);
    public List<Producto> buscarProductosPorDescripcion(String term);

    // servicios para Categoria
    public List<Categoria> buscarCategoriasTodos();
    public Page<Categoria> buscarCategoriasTodos(Pageable pageable);
    public Categoria buscarCategoriaPorId(Long id);


    // servicios para mantenimiento

    // servicios para Cliente
    public List<Cliente> buscarClientesTodos();
    public Page<Cliente> buscarClientesTodos(Pageable pageable);
    public void guardarCliente(Cliente cliente);
    public Cliente buscarClientePorId(Long id);
    public void eliminarClientePorId(Long id);
    public Cliente buscarClientePorIdConFactura(Long id);
    public Cliente buscarClientePorIdConDocumento(Long id); // Nuevo método agregado

    // servicios para Factura
    public void guardarFactura(Factura factura);
    public Factura buscarFacturaPorNroFactura(Long nroFactura);
    public void eliminarFacturaPorNroFactura(Long nroFactura);
    public Factura buscarFacturaPorNroFacturaConClienteDetalleProducto(Long nroFactura);

    // servicios para Documento
    public Documento buscarDocumentoiddoc(Long id);
    public void eliminarDocumentoPorid(Long id);
    public void guardarDocumento(Documento documento);
}