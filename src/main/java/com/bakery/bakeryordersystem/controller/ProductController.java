package com.bakery.bakeryordersystem.controller;
import com.bakery.bakeryordersystem.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller @RequestMapping("/admin/products")
public class ProductController {
    private final ProductService service;
    public ProductController(ProductService service) { this.service = service; }
    @GetMapping public String list(@RequestParam(required = false) String q, @RequestParam(required = false) String edit, Model m) {
        m.addAttribute("items", service.search(q)); m.addAttribute("q", q);
        m.addAttribute("item", edit == null ? null : service.find(edit));
        return "products";
    }
    @PostMapping("/save") public String save(@RequestParam(required = false) String id, @RequestParam String category, @RequestParam String name, @RequestParam double price, @RequestParam int stock, @RequestParam String detail) {
        service.save(id, category, name, price, stock, detail); return "redirect:/admin/products";
    }
    @PostMapping("/delete/{id}") public String delete(@PathVariable String id) { service.delete(id); return "redirect:/admin/products"; }
}
