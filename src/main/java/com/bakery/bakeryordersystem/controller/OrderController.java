package com.bakery.bakeryordersystem.controller;
import com.bakery.bakeryordersystem.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** ADMIN area: all orders. */
@Controller @RequestMapping("/admin/orders")
public class OrderController {
    private final OrderService orders;
    private final ProductService products;
    public OrderController(OrderService orders, ProductService products) { this.orders = orders; this.products = products; }
    @GetMapping public String list(Model m) {
        m.addAttribute("items", orders.findAll()); m.addAttribute("products", products.search(null)); return "orders";
    }
    @PostMapping("/place") public String place(@RequestParam String customer, @RequestParam String productId, @RequestParam int qty, @RequestParam String payment, RedirectAttributes ra) {
        try { orders.place("", customer, productId, qty, payment); }
        catch (IllegalArgumentException e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/admin/orders";
    }
    @PostMapping("/status/{id}") public String status(@PathVariable String id, @RequestParam String status) { orders.updateStatus(id, status); return "redirect:/admin/orders"; }
    @PostMapping("/delete/{id}") public String delete(@PathVariable String id) { orders.delete(id); return "redirect:/admin/orders"; }
}
