package com.desarrolloweb.comercial.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.desarrolloweb.comercial.model.entity.Cliente;
import com.desarrolloweb.comercial.model.entity.Documento;
import com.desarrolloweb.comercial.model.service.ComercialServiceIface;

import jakarta.validation.Valid;

@Controller
@SessionAttributes("documento")
@RequestMapping("/comercial")
public class DocumentoController {

    @Value("${uploads.path}")
    private String uploadsExtDir;

    private final Logger logger = LoggerFactory.getLogger(getClass());
    DecimalFormat df = new DecimalFormat("###,##0.0");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private final ComercialServiceIface comercialService;

    public DocumentoController(ComercialServiceIface comercialService) {
        this.comercialService = comercialService;
    }

    @GetMapping("/documentonuevo/{id}")
    public String documentoNueva(@PathVariable Long id, Model model, RedirectAttributes flash) {
        Cliente cliente = comercialService.buscarClientePorId(id);

        if (cliente == null) {
            flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
            return "redirect:/comercial/clienteslistar";
        }

        Documento documento = new Documento();
        documento.setCliente(cliente);

        model.addAttribute("titulo", "Nueva Documento");
        model.addAttribute("btn_accion", "Guardar Documento");
        model.addAttribute("documento", documento);
        return "Documento/nuevo_documento";
    }

    @PostMapping("/documentoguardar")
    public String guardarDocumento(@Valid @ModelAttribute Documento documento, BindingResult errors,
            SessionStatus status, RedirectAttributes flash,
            Model model, @RequestParam(name = "filedoc") MultipartFile nombreDocumento) {

        String estado = "Desconocido";

        // String accion = (documento.getId() == null) ? "Guardar" : "Modificar";

        if (errors.hasErrors()) {
            model.addAttribute("titulo", "Nuevo Documento");
            model.addAttribute("btn_accion", "Guardar Documento");
            model.addAttribute("warning", "Corrija o complemente la infomación del formulario");
            return "Documento/nuevo_documento";
        }

        if (!nombreDocumento.isEmpty()) {

            logger.info("######## Peso: " + df.format((double) nombreDocumento.getSize() / 1024) + "KB");
            String tipoArchivo = "DESCONOCIDO";
            String nombreOriginal = nombreDocumento.getOriginalFilename();
            if (nombreOriginal != null && nombreOriginal.contains(".")) {
                tipoArchivo = nombreOriginal.substring(nombreOriginal.lastIndexOf(".") + 1).toLowerCase();
            }
            logger.info("####### Tipo: " + tipoArchivo);
            documento.setTipoArchivo(tipoArchivo);
            documento.setTamanio((int) (nombreDocumento.getSize() / 1024));

            if (documento.getId() != null && documento.getId() > 0 &&
                    documento.getNombreDocumento() != null && documento.getNombreDocumento().length() > 0) {

                Path rutaAbsUploads = Paths.get(uploadsExtDir).resolve(documento.getNombreDocumento()).toAbsolutePath();

                try {
                    BasicFileAttributes attributes = Files.readAttributes(rutaAbsUploads, BasicFileAttributes.class);
                    FileTime fechaCreacion = attributes.creationTime();
                    FileTime fechaModificacion = attributes.lastModifiedTime();
                    logger.info("### Creado en: " + sdf.format(new Date(fechaCreacion.toMillis())));
                    logger.info("### Modificado en: " + sdf.format(new Date(fechaModificacion.toMillis())));

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    logger.error("Error al leer atributos del archivo: " + e.getMessage());

                }

                File archivo = rutaAbsUploads.toFile();
                if (archivo.exists() && archivo.canRead()) {
                    archivo.delete();
                }
            }

            String nombreArchivo = UUID.randomUUID().toString().substring(0, 10) + "_"
                    + nombreDocumento.getOriginalFilename();
            Path rutaUploads = Paths.get(uploadsExtDir).resolve(nombreArchivo);
            Path rutaAbsUploads = rutaUploads.toAbsolutePath();

            try {
                Files.copy(nombreDocumento.getInputStream(), rutaAbsUploads);
                documento.setNombreDocumento(nombreArchivo);

                // Actualizar fechas después de copiar el nuevo archivo
                BasicFileAttributes attributes = Files.readAttributes(rutaAbsUploads, BasicFileAttributes.class);
                FileTime fechaCreacion = attributes.creationTime();
                FileTime fechaModificacion = attributes.lastModifiedTime();
                logger.info("### Creado en: " + sdf.format(new Date(fechaCreacion.toMillis())));
                logger.info("### Modificado en: " + sdf.format(new Date(fechaModificacion.toMillis())));

                // Convertir FileTime a Date y establecer en la entidad
                documento.setFechaCreacion(new Date(fechaCreacion.toMillis()));
                documento.setFechaModificacion(new Date(fechaModificacion.toMillis()));

                flash.addFlashAttribute("info", "El archivo " + nombreArchivo + " fue cargado");

            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }

        estado = "Archivado";
        documento.setEstado(estado);
        comercialService.guardarDocumento(documento);
        status.setComplete();
        flash.addFlashAttribute("success",
                "El registro fue " + (documento.getId() == null ? "agregado" : "modificado") + " con éxito");
        return "redirect:/comercial/clienteconsultar/" + documento.getCliente().getId();
    }

    @GetMapping("/doceliminar/{id}")
    public String DocumentoEliminar(@PathVariable Long id, RedirectAttributes flash, Model model) {

        Documento doc = comercialService.buscarDocumentoiddoc(id);

        if (doc == null) {
            flash.addFlashAttribute("error", "El documento no existe en la base de datos");
            return "redirect:/comercial/clienteslistar";
        }

        comercialService.eliminarDocumentoPorid(id);
        flash.addFlashAttribute("success", "El documento " + doc.getNombreDocumento() + " fue eliminado");
        return "redirect:/comercial/clienteconsultar/" + doc.getCliente().getId();
    }
   
}
