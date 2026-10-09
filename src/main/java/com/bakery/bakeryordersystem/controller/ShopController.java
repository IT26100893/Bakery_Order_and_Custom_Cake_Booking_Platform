package com.bakery.bakeryordersystem.controller;

import com.bakery.bakeryordersystem.model.BakeryProduct;
import com.bakery.bakeryordersystem.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ShopController {

    private final ProductRepository productRepository;

    public ShopController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/shop")
    public String shop(@RequestParam(required = false) String category,
                       @RequestParam(required = false) String q,
                       Model model) {

        String cat   = category == null ? "" : category.trim().toUpperCase();
        String query = q == null ? "" : q.trim().toLowerCase();

        List<BakeryProduct> items = productRepository.findAll().stream()
                .filter(p -> cat.isEmpty()
                        || String.valueOf(p.getCategory()).equalsIgnoreCase(cat))
                .filter(p -> query.isEmpty()
                        || ((p.getName() != null ? p.getName() : "") + " " + (p.getDetail() != null ? p.getDetail() : "")).toLowerCase().contains(query))
                .collect(Collectors.toList());

        String title;
        switch (cat) {
            case "BREAD":  title = "Bread & Savouries";        break;
            case "CAKE":   title = "Cakes";                    break;
            case "PASTRY": title = "Pastries & Cake Pieces";   break;
            default:       title = query.isEmpty() ? "All Products" : "Search: " + (q != null ? q.trim() : "");
        }

        model.addAttribute("items", items);
        model.addAttribute("category", cat);
        model.addAttribute("title", title);

        return "shop";
    }
}