package com.bakery.bakeryordersystem.controller;
import com.bakery.bakeryordersystem.service.CustomCakeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller @RequestMapping("/admin/bookings")
public class CustomCakeController {
    private final CustomCakeService service;
    public CustomCakeController(CustomCakeService service) { this.service = service; }
    @GetMapping public String list(@RequestParam(required = false) String q, @RequestParam(required = false) String edit, Model m) {
        m.addAttribute("items", service.search(q)); m.addAttribute("q", q);
        m.addAttribute("item", edit == null ? null : service.find(edit).orElse(null));
        return "bookings";
    }
    @PostMapping("/save") public String save(@RequestParam(required = false) String id, @RequestParam String customerName, @RequestParam String phone, @RequestParam String flavour,
                                             @RequestParam double sizeKg, @RequestParam(defaultValue = "") String message, @RequestParam String pickupDate, @RequestParam String status) {
        service.save(id, customerName, phone, flavour, sizeKg, message, pickupDate, status); return "redirect:/admin/bookings";
    }
    @PostMapping("/delete/{id}") public String delete(@PathVariable String id) { service.delete(id); return "redirect:/admin/bookings"; }
}
