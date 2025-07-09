package com.desarrolloweb.comercial.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.desarrolloweb.comercial.model.entity.Cliente;
import com.desarrolloweb.comercial.model.entity.Detalle;
import com.desarrolloweb.comercial.model.entity.Factura;
import com.desarrolloweb.comercial.model.entity.Producto;
import com.desarrolloweb.comercial.model.service.ComercialServiceIface;

import jakarta.validation.Valid;

@Controller
@SessionAttributes("factura")
@RequestMapping("/comercial")
public class FacturaController {

    private final ComercialServiceIface comercialService;

    public FacturaController(ComercialServiceIface comercialService) {
        this.comercialService = comercialService;
    }

    @GetMapping("/facturanueva/{id}")
    public String facturaNueva(@PathVariable Long id, Model model, RedirectAttributes flash) {
        Cliente cliente = comercialService.buscarClientePorId(id);
        if (cliente == null) {
            flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
            return "redirect:/comercial/clienteslistar";
        }

        Factura factura = new Factura();
        factura.setCliente(cliente);

        model.addAttribute("titulo", "Nueva factura");
        model.addAttribute("btn_accion", "Guardar factura");
        model.addAttribute("factura", factura);
        return "factura/factura_nueva";
    }

    @GetMapping(value = "/cargarproductos/{term}", produces = "application/json")
    public @ResponseBody List<Producto> cargarProductos(@PathVariable(name = "term") String term) {
        return comercialService.buscarProductosPorDescripcion(term);
    }

    @PostMapping("/guardarfactura")
    public String guardarFactura(@Valid @ModelAttribute Factura factura, BindingResult errors,
            @RequestParam(name = "detalle_id[]", required = false) Long[] detalleId,
            @RequestParam(name = "cantidad[]", required = false) int[] cantidad,
            SessionStatus status, RedirectAttributes flash,
            Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("titulo", "Nueva factura");
            model.addAttribute("btn_accion", "Guardar factura");
            model.addAttribute("warning", "Corrija o complemente la infomación del formulario");       
            return "factura/factura_nueva";
        }

        if (detalleId == null || detalleId.length == 0) {
            model.addAttribute("titulo", "Nueva factura");
            model.addAttribute("btn_accion", "Guardar factura");
            model.addAttribute("error", "Debe tener por lo menos una linea o detalle de factura");       
            return "factura/factura_nueva";
        }

        for (int i = 0; i < detalleId.length; i++) {
            Producto producto = comercialService.buscarProductoPorId(detalleId[i]);
            Detalle detalle = new Detalle();
            detalle.setCantidad(cantidad[i]);
            detalle.setProducto(producto);
            factura.addDetalle(detalle);
        }

        comercialService.guardarFactura(factura);
        status.setComplete();

        flash.addFlashAttribute("success", "La factura se guardó correctamente");
        return "redirect:/comercial/clienteconsultar/" + factura.getCliente().getId();
    }

    @GetMapping("/facturaconsultar/{nroFactura}")
    public String facturaConsultar(@PathVariable Long nroFactura, RedirectAttributes flash, Model model) {
        // Factura factura = comercialService.buscarFacturaPorNroFactura(nroFactura);

        Factura factura = comercialService.buscarFacturaPorNroFacturaConClienteDetalleProducto(nroFactura);

        if (factura == null) {
            flash.addFlashAttribute("error", "La factura no existe en la base de datos");
            return "redirect:/comercial/clienteslistar";
        }

        model.addAttribute("factura", factura);
        model.addAttribute("titulo", "Factura número " + factura.getNroFactura() + " - " + factura.getDescripcion());

        return "factura/factura_consulta";
    }

    @GetMapping("/facturaeliminar/{nroFactura}")
    public String facturaEliminar(@PathVariable Long nroFactura, RedirectAttributes flash, Model model) {
        Factura factura = comercialService.buscarFacturaPorNroFactura(nroFactura);

        if(factura == null) {
            flash.addFlashAttribute("error", "La factura no existe en la base de datos");
            return "redirect:/comercial/clienteslistar";
        }

        comercialService.eliminarFacturaPorNroFactura(nroFactura);
        flash.addFlashAttribute("success", "La factura " + nroFactura + " fue eliminada");
        return "redirect:/comercial/clienteconsultar/" + factura.getCliente().getId();
    }

}
