package com.bakery.bakeryordersystem.controller;
import com.bakery.bakeryordersystem.model.User;
import com.bakery.bakeryordersystem.service.UserService;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;

/** Public pages: login, register, logout. */
@Controller
public class AuthController {
    private final UserService users;
    public AuthController(UserService users) { this.users = users; }

    @GetMapping("/login") public String loginPage(HttpSession s) {
        Object role = s.getAttribute("role");
        return role == null ? "login" : "redirect:" + ("ADMIN".equals(role) ? "/admin" : "/customer");
    }
    @PostMapping("/login") public String login(@RequestParam String email, @RequestParam String password, HttpServletRequest req, Model m) {
        Optional<User> u = users.authenticate(email, password);
        if (u.isEmpty()) { m.addAttribute("error", "Wrong email or password"); m.addAttribute("email", email); return "login"; }
        HttpSession old = req.getSession();
        String next = (String) old.getAttribute("next");
        req.changeSessionId();                       // new session id after login (prevents session fixation)
        HttpSession s = req.getSession();
        s.setAttribute("uid", u.get().getId()); s.setAttribute("role", u.get().getRole()); s.setAttribute("uname", u.get().getName());
        s.removeAttribute("next");
        if ("ADMIN".equals(u.get().getRole())) return "redirect:/admin";
        return "redirect:" + (next != null && next.startsWith("/customer") ? next : "/customer");
    }
    @GetMapping("/register") public String registerPage(HttpSession s) {
        return s.getAttribute("role") == null ? "register" : "redirect:/";
    }
    @PostMapping("/register") public String register(@RequestParam String name, @RequestParam String email, @RequestParam String phone,
                                                     @RequestParam String password, @RequestParam String confirm, Model m, RedirectAttributes ra) {
        try {
            if (!password.equals(confirm)) throw new IllegalArgumentException("The two passwords do not match");
            users.register(name, email, phone, password);
        } catch (IllegalArgumentException e) {
            m.addAttribute("error", e.getMessage()); m.addAttribute("name", name); m.addAttribute("email", email); m.addAttribute("phone", phone);
            return "register";
        }
        ra.addFlashAttribute("msg", "Account created. Please log in.");
        return "redirect:/login";
    }
    @PostMapping("/logout") public String logout(HttpSession s) { s.invalidate(); return "redirect:/"; }
}
