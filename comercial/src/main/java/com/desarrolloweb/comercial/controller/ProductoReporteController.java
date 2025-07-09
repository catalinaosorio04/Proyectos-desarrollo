package com.desarrolloweb.comercial.controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.desarrolloweb.comercial.model.entity.Producto;
import com.desarrolloweb.comercial.model.service.ComercialServiceIface;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.*;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;

@Controller
@RequestMapping("/comercial")
public class ProductoReporteController {

    private final ComercialServiceIface comercialService;

    public ProductoReporteController(ComercialServiceIface comercialService) {
        this.comercialService = comercialService;
    }

    // Reporte PDF (ya lo tienes)
    @GetMapping("/reporte-productos-pdf")
    public void generarReporteProductos(HttpServletResponse response) throws Exception {
        InputStream jasperStream = getClass().getResourceAsStream("/reportes/productos.jasper");
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

        List<Producto> productos = comercialService.buscarProductosTodos();
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(productos);

        Map<String, Object> params = new HashMap<>();
        params.put("usuario", "palvarez");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=reporte-productos.pdf");

        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

    // Nuevo: Reporte Word (DOCX)
    @GetMapping("/reporte-productos-word")
    public void generarReporteProductosWord(HttpServletResponse response) throws Exception {
        InputStream jasperStream = getClass().getResourceAsStream("/reportes/productos.jasper");
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

        List<Producto> productos = comercialService.buscarProductosTodos();
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(productos);

        Map<String, Object> params = new HashMap<>();
        params.put("usuario", "palvarez");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        response.setHeader("Content-Disposition", "attachment; filename=reporte-productos.docx");

        JRDocxExporter exporter = new JRDocxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

        SimpleDocxReportConfiguration docxConfig = new SimpleDocxReportConfiguration();
        exporter.setConfiguration(docxConfig);

        exporter.exportReport();

        response.getOutputStream().flush();
        response.getOutputStream().close();
    }
}
