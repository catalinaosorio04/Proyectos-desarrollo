package com.desarrolloweb.comercial.controller;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.desarrolloweb.comercial.model.entity.Cliente;
import com.desarrolloweb.comercial.model.service.ComercialServiceIface;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Controller
@RequestMapping("/comercial")
public class ClienteReporteController {

    private final ComercialServiceIface comercialService;

    public ClienteReporteController(ComercialServiceIface comercialService) {
        this.comercialService = comercialService;
    }

    @GetMapping("/reporte-cliente-pdf/{id}")
    public void generarReporteCliente(@PathVariable Long id, HttpServletResponse response) throws Exception {
        try {
            JasperPrint jasperPrint = inicializarReporte(id);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=reporte-cliente-" + id + ".pdf");

            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al generar el reporte: " + e.getMessage());
        } finally {
            if (response.getOutputStream() != null) {
                response.getOutputStream().close();
            }
        }
    }

    private JasperPrint inicializarReporte(@PathVariable Long id) throws Exception {
        InputStream jasperStream = getClass().getResourceAsStream("/reportes/cupo_cliente.jasper");
        if (jasperStream == null) {
            throw new Exception("Archivo .jasper no encontrado en /reportes/cupo_cliente.jasper");
        }
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

        Cliente cliente = comercialService.buscarClientePorId(id);
        if (cliente == null) {
            throw new Exception("Cliente con ID " + id + " no encontrado");
        }

        if (cliente.getAumentoCupo() == null) {
            cliente.setAumentoCupo(0);
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singletonList(cliente));

        Map<String, Object> params = new HashMap<>();
        params.put("nombreCompleto", cliente.getNombreCompleto());
        params.put("id", cliente.getId());
        params.put("aumentoCupo", cliente.getAumentoCupo());

        return JasperFillManager.fillReport(jasperReport, params, dataSource);
    }
}