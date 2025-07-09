package com.desarrolloweb.comercial.controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.desarrolloweb.comercial.model.entity.Categoria;
//import com.desarrolloweb.comercial.model.entity.Producto;
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
public class CategoriaReporteController {

    private final ComercialServiceIface comercialService;

    public CategoriaReporteController(ComercialServiceIface comercialService) {
        this.comercialService = comercialService;
    }

    @GetMapping("/reporte-categorias-pdf")
    public void generarReporteCategorias(HttpServletResponse response) throws Exception {

        //Llamar al metodo de inicializacion
        JasperPrint jasperPrint = inicializarReporte();

        // configurar la respuesta
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=reporte-categorias.pdf");

        // exportar a pdf
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }



    private JasperPrint inicializarReporte() throws Exception {
        // cargar el archivo .jasper
        InputStream jasperStream = getClass().getResourceAsStream("/reportes/categorias.jasper");
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);

        // obtener la lista de categorias
        List<Categoria> categorias = comercialService.buscarCategoriasTodos();

        // crear un datasource a partir de la lista
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(categorias);

        Map<String, Object> params = new HashMap<>();
        

        // llenar el reporte con la lista
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

        return jasperPrint;

    }

}
