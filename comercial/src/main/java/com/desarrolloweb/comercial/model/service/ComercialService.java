package com.desarrolloweb.comercial.model.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desarrolloweb.comercial.model.dao.CategoriaDAOIface;
import com.desarrolloweb.comercial.model.dao.ClienteDAOIface;
import com.desarrolloweb.comercial.model.dao.DocumentoDAOIface;
import com.desarrolloweb.comercial.model.dao.FacturaDAOIface;
import com.desarrolloweb.comercial.model.dao.ProductoDAOIface;
import com.desarrolloweb.comercial.model.entity.Categoria;
import com.desarrolloweb.comercial.model.entity.Cliente;
import com.desarrolloweb.comercial.model.entity.Documento;
import com.desarrolloweb.comercial.model.entity.Factura;
import com.desarrolloweb.comercial.model.entity.Producto;

@Service
public class ComercialService implements ComercialServiceIface {

    private final ProductoDAOIface productoDAO;
    private final CategoriaDAOIface categoriaDAO;
    private final ClienteDAOIface clienteDAO;
    private final FacturaDAOIface facturaDAO;
    private final DocumentoDAOIface docDao;

    public ComercialService(ProductoDAOIface productoDAO, CategoriaDAOIface categoriaDAO, ClienteDAOIface clienteDAO,
            FacturaDAOIface facturaDAO, DocumentoDAOIface docDao) {
        this.productoDAO = productoDAO;
        this.categoriaDAO = categoriaDAO;
        this.clienteDAO = clienteDAO;
        this.facturaDAO = facturaDAO;
        this.docDao = docDao;
    }

    // servicios para Producto
    @Override
    @Transactional(readOnly = true)
    public List<Producto> buscarProductosTodos() {
        return productoDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Producto> buscarProductosTodos(Pageable pageable) {
        return productoDAO.findAll(pageable);
    }

    @Override
    @Transactional
    public void guardarProducto(Producto producto) {
        productoDAO.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Producto buscarProductoPorId(Long id) {
        return productoDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void eliminarProductoPorId(Long id) {
        productoDAO.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> buscarProductosPorDescripcion(String term) {
        return productoDAO.findByDescripcionLikeIgnoreCase("%" + term + "%");
    }

    // servicios para Categoría
    @Override
    @Transactional(readOnly = true)
    public List<Categoria> buscarCategoriasTodos() {
        return categoriaDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Categoria> buscarCategoriasTodos(Pageable pageable) {
        return categoriaDAO.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Categoria buscarCategoriaPorId(Long id) {
        return categoriaDAO.findById(id).orElse(null);
    }

    // servicios para Cliente
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> buscarClientesTodos() {
        return clienteDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> buscarClientesTodos(Pageable pageable) {
        return clienteDAO.findAll(pageable);
    }

    @Override
    @Transactional
    public void guardarCliente(Cliente cliente) {
        clienteDAO.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente buscarClientePorId(Long id) {
        return clienteDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void eliminarClientePorId(Long id) {
        clienteDAO.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente buscarClientePorIdConFactura(Long id) {
        return clienteDAO.buscarPorIdConFacturas(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente buscarClientePorIdConDocumento(Long id) {
        return clienteDAO.buscarClientePorIdConDocumento(id).orElse(null);
    }

    // servicios para Factura
    @Override
    @Transactional
    public void guardarFactura(Factura factura) {
        facturaDAO.save(factura);
    }

    @Override
    @Transactional(readOnly = true)
    public Factura buscarFacturaPorNroFactura(Long nroFactura) {
        return facturaDAO.findById(nroFactura).orElse(null);
    }

    @Override
    @Transactional
    public void eliminarFacturaPorNroFactura(Long nroFactura) {
        facturaDAO.deleteById(nroFactura);
    }

    @Override
    @Transactional(readOnly = true)
    public Factura buscarFacturaPorNroFacturaConClienteDetalleProducto(Long nroFactura) {
        return facturaDAO.buscarPorNroFacturaConClienteDetalleProducto(nroFactura);
    }

    // servicios para Documento
    @Override
    @Transactional(readOnly = true)
    public Documento buscarDocumentoiddoc(Long id) {
        return docDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void eliminarDocumentoPorid(Long id) {
        docDao.deleteById(id);
    }

    @Override
    @Transactional
    public void guardarDocumento(Documento documento) {
        docDao.save(documento);
    }
}