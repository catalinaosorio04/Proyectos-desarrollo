package com.desarrolloweb.comercial.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;


@Controller
@RequestMapping("/comercial")
@SessionAttributes("mantenimiento")
public class MantenimientoController {



    @GetMapping("/mantenimientolistar")
    public String mantenimientoListar(Model model) {

        model.addAttribute("titulo", "Listado de categorías");

        return "mantenimiento/vista_mantenimiento";
    }
}
