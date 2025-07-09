package com.desarrolloweb.comercial.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.desarrolloweb.comercial.model.entity.Categoria;
import com.desarrolloweb.comercial.model.entity.Producto;
import com.desarrolloweb.comercial.model.service.ComercialServiceIface;

import jakarta.validation.Valid;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Controller
@RequestMapping("/comercial")
@SessionAttributes("producto")
public class ProductoController {

    private final ComercialServiceIface comercialService;

    public ProductoController(ComercialServiceIface comercialService) {
        this.comercialService = comercialService;
    }

    @GetMapping("/productoslistar")
    public String productosListar(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Producto> productosPage = comercialService.buscarProductosTodos(pageable);
        model.addAttribute("titulo", "Listado de productos disponibles");
        model.addAttribute("productosPage", productosPage);
        return "producto/listado_productos";
    }

    @GetMapping("/productonuevo")
    public String productoFormNuevo(Model model) {
        model.addAttribute("titulo", "Nuevo producto");
        model.addAttribute("accion", "Agregar");
        model.addAttribute("producto", new Producto());
        return "producto/formulario_producto";
    }

    @PostMapping("/productoguardar")
    public String productoGuardar(@Valid @ModelAttribute Producto producto, BindingResult result, RedirectAttributes flash, 
            Model model, SessionStatus status) {

        String accion = (producto.getId() == null) ? "agregado" : "modificado";

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Nuevo producto");
            model.addAttribute("accion", accion);
            model.addAttribute("info", "Complemente o corrija la información de los campos del formulario");
            return "producto/formulario_producto";
        }

        comercialService.guardarProducto(producto);
        status.setComplete();
        flash.addFlashAttribute("success", "El producto fue " + accion + " de forma correcta");
        return "redirect:/comercial/productoslistar";
    }

    @GetMapping("/productoconsultar/{id}")
    public String productoConsultar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        Producto producto = comercialService.buscarProductoPorId(id);
        if (producto == null) {
            flash.addFlashAttribute("warning", "El producto con ID " + id + " no está en la base de datos");
            return "redirect:/comercial/productoslistar";
        }
        model.addAttribute("titulo", "Consulta del producto: " + producto.getDescripcion());
        model.addAttribute("producto", producto);
        return "producto/consulta_producto";
    }

    @GetMapping("/productoeliminar/{id}")
    public String productoEliminar(@PathVariable Long id, RedirectAttributes flash) {
        if (id > 0) {
            Producto producto = comercialService.buscarProductoPorId(id);
            if (producto != null) {
                comercialService.eliminarProductoPorId(id);
                flash.addFlashAttribute("success", "El producto fue eliminado");
            }
            else {
                flash.addFlashAttribute("warning", "El producto con ID " + id + " no está en la base de datos");
            }
        }
        else {
            flash.addFlashAttribute("error", "El ID debería ser un valor positivo");
        }
        return "redirect:/comercial/productoslistar";
    }

    @GetMapping("/productomodificar/{id}")
    public String productoFormModificar(@PathVariable(value = "id") Long id, Model model) {
        Producto producto = null;
        if (id > 0) {
            producto = comercialService.buscarProductoPorId(id);
            if (producto == null) {
                return "redirect:/comercial/productoslistar";
            }
        }
        model.addAttribute("accion", "Modificar");
        model.addAttribute("titulo", "Modificar producto");
        model.addAttribute("producto", producto);
        return "producto/formulario_producto";
    }

    @ModelAttribute("categorias")
    public List<Categoria> obtenerCategorias() {
        return comercialService.buscarCategoriasTodos();
    }

}