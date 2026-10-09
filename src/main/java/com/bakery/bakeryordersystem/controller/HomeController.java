package com.bakery.bakeryordersystem.controller;
import com.bakery.bakeryordersystem.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

/** Public home page: shows up to 5 in-stock products that have a photo. */
@Controller
public class HomeController {
    private final ProductService products;
    public HomeController(ProductService products) { this.products = products; }

    public static class Pick {
        private final String id, name, image; private final double price;
        public Pick(String id, String name, double price, String image) { this.id = id; this.name = name; this.price = price; this.image = image; }
        public String getId() { return id; }
        public String getName() { return name; }
        public double getPrice() { return price; }
        public String getImage() { return image; }
    }

    @GetMapping("/") public String home(Model m) {
        List<Pick> picks = products.search(null).stream().filter(p -> p.getStock() > 0)
                .map(p -> new Pick(p.getId(), p.getName(), p.getPrice(), imageFor(p.getName())))
                .filter(p -> p.getImage() != null).limit(5).toList();
        m.addAttribute("picks", picks);
        return "index";
    }
    /** Same name -> photo rules the shop page uses. */
    static String imageFor(String name) {
        String n = name.toLowerCase();
        if (n.contains("sliced")) return "sliced_bread.png";
        if (n.contains("cheese")) return "cheese_sandwich.png";
        if (n.contains("chicken")) return "chicken_sandwich.png";
        if (n.contains("dinner")) return "dinner_bun.png";
        if (n.contains("sourdough")) return "sourdough_loaf.png";
        if (n.contains("strawberry")) return "Strawberry_Chocolate.jpeg";
        if (n.contains("pineapple")) return "chocolate_pineapple_gateau.png";
        if (n.contains("marble")) return "Marber_gateau.jpg";
        if (n.contains("coffee")) return "Coffee_choclete_gateau.jpg";
        if (n.contains("rose")) return "Rose_gateau.jpg";
        return null;
    }
}
