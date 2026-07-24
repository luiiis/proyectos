package com.softwarelee.login;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * CONTROLLER que devuelve páginas HTML (no JSON).
 * 
 * @Controller (no @RestController) → devuelve VISTAS (HTML con Thymeleaf)
 * El nombre que retorna ("login", "dashboard") es el nombre del archivo HTML
 * que está en src/main/resources/templates/
 */
@Controller
public class WebController {

    @GetMapping("/login")
    public String login() {
        return "login"; // → templates/login.html
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        model.addAttribute("username", auth.getName());
        model.addAttribute("roles", auth.getAuthorities());
        return "dashboard"; // → templates/dashboard.html
    }

    @GetMapping("/admin")
    public String admin(Authentication auth, Model model) {
        model.addAttribute("username", auth.getName());
        return "admin"; // → templates/admin.html
    }
}
