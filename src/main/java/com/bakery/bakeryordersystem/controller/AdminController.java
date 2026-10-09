package com.bakery.bakeryordersystem.controller;
import com.bakery.bakeryordersystem.model.*;
import com.bakery.bakeryordersystem.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.*;

/** ADMIN dashboard. */
@Controller
public class AdminController {
    private final ProductService products; private final OrderService orders;
    private final CustomCakeService bookings; private final UserService users;
    public AdminController(ProductService p, OrderService o, CustomCakeService b, UserService u) { products = p; orders = o; bookings = b; users = u; }

    @GetMapping("/admin") public String dashboard(Model m) {
        List<Order> all = orders.findAll();
        m.addAttribute("productCount", products.search(null).size());
        m.addAttribute("lowStock", products.search(null).stream().filter(p -> p.getStock() <= 5).count());
        m.addAttribute("orderCount", all.size());
        m.addAttribute("newOrders", all.stream().filter(o -> "PLACED".equals(o.getStatus())).count());
        m.addAttribute("revenue", all.stream().filter(o -> !"CANCELLED".equals(o.getStatus())).mapToDouble(Order::getTotal).sum());
        m.addAttribute("pendingBookings", bookings.search(null).stream().filter(b -> "PENDING".equals(b.getStatus())).count());
        m.addAttribute("customerCount", users.search(null).stream().filter(u -> "CUSTOMER".equals(u.getRole())).count());
        List<Order> recent = new ArrayList<>(all); Collections.reverse(recent);
        m.addAttribute("recent", recent.stream().limit(5).toList());
        return "admin-dashboard";
    }
}
