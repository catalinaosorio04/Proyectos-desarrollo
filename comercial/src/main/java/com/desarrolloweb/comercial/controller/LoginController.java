package com.desarrolloweb.comercial.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(Model model, Principal principal,
        @RequestParam(value = "error", required = false) String error,
        @RequestParam(value = "logout", required = false) String logout, RedirectAttributes flash) {

        if (error != null) {
            model.addAttribute("error", "Usuario o clave inválido, intente de nuevo");
            return "login/login";
        }

        if (logout != null) {
            model.addAttribute("success", "Has cerrado sesión de forma correcta");
            return "login/login";
        }

        if (principal != null) {
            flash.addFlashAttribute("info", "El usuario " + principal.getName() + " ya tiene una sesión abierta");
            return "redirect:/"; // redirige al inicio si ya está autenticado
        }
        return "login/login";
    }
}