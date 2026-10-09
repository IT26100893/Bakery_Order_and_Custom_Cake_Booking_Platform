package com.bakery.bakeryordersystem.controller;
import com.bakery.bakeryordersystem.model.User;
import com.bakery.bakeryordersystem.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** CUSTOMER area: a customer only ever sees and changes their OWN orders, bookings and profile. */
@Controller @RequestMapping("/customer")
public class CustomerController {
    private final OrderService orders; private final ProductService products;
    private final CustomCakeService bookings; private final UserService users;
    public CustomerController(OrderService o, ProductService p, CustomCakeService b, UserService u) { orders = o; products = p; bookings = b; users = u; }

    private String uid(HttpSession s) { return (String) s.getAttribute("uid"); }

    @GetMapping public String home(HttpSession s, Model m) {
        m.addAttribute("orderCount", orders.findByUser(uid(s)).size());
        m.addAttribute("bookingCount", bookings.findByUser(uid(s)).size());
        return "customer-home";
    }

    // ---- orders ----
    @GetMapping("/orders") public String myOrders(@RequestParam(required = false) String productId, HttpSession s, Model m) {
        m.addAttribute("items", orders.findByUser(uid(s))); m.addAttribute("products", products.search(null)); m.addAttribute("selected", productId);
        return "customer-orders";
    }
    @PostMapping("/orders/place") public String place(@RequestParam String productId, @RequestParam int qty, @RequestParam String payment, HttpSession s, RedirectAttributes ra) {
        try { orders.place(uid(s), (String) s.getAttribute("uname"), productId, qty, payment); ra.addFlashAttribute("msg", "Order placed. Thank you!"); }
        catch (IllegalArgumentException e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/customer/orders";
    }
    @PostMapping("/orders/cancel/{id}") public String cancel(@PathVariable String id, HttpSession s, RedirectAttributes ra) {
        try { orders.cancelOwn(uid(s), id); ra.addFlashAttribute("msg", "Order cancelled."); }
        catch (IllegalArgumentException e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/customer/orders";
    }

    // ---- custom cake bookings ----
    @GetMapping("/bookings") public String myBookings(@RequestParam(required = false) String edit, HttpSession s, Model m) {
        m.addAttribute("items", bookings.findByUser(uid(s)));
        m.addAttribute("item", edit == null ? null : bookings.findOwn(uid(s), edit).orElse(null));
        return "customer-bookings";
    }
    @PostMapping("/bookings/save") public String saveBooking(@RequestParam(required = false) String id, @RequestParam String customerName, @RequestParam String phone,
            @RequestParam String flavour, @RequestParam double sizeKg, @RequestParam(defaultValue = "") String message, @RequestParam String pickupDate,
            HttpSession s, RedirectAttributes ra) {
        try { bookings.saveOwn(uid(s), id, customerName, phone, flavour, sizeKg, message, pickupDate); ra.addFlashAttribute("msg", "Booking saved."); }
        catch (IllegalArgumentException e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/customer/bookings";
    }
    @PostMapping("/bookings/delete/{id}") public String deleteBooking(@PathVariable String id, HttpSession s, RedirectAttributes ra) {
        try { bookings.deleteOwn(uid(s), id); ra.addFlashAttribute("msg", "Booking cancelled."); }
        catch (IllegalArgumentException e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/customer/bookings";
    }

    // ---- profile ----
    @GetMapping("/profile") public String profile(HttpSession s, Model m) { m.addAttribute("me", users.find(uid(s))); return "customer-profile"; }
    @PostMapping("/profile") public String saveProfile(@RequestParam String name, @RequestParam String email, @RequestParam String phone,
            @RequestParam(required = false) String password, HttpSession s, RedirectAttributes ra) {
        try { User u = users.updateProfile(uid(s), name, email, phone, password); s.setAttribute("uname", u.getName()); ra.addFlashAttribute("msg", "Profile updated."); }
        catch (IllegalArgumentException e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/customer/profile";
    }
}
