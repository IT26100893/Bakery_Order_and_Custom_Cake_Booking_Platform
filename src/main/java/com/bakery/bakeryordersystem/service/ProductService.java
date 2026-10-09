package com.bakery.bakeryordersystem.service;
import com.bakery.bakeryordersystem.exception.ProductNotFoundException;
import com.bakery.bakeryordersystem.model.BakeryProduct;
import com.bakery.bakeryordersystem.repository.*;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ProductService {
    private final ProductRepository repo;
    public ProductService(ProductRepository repo) { this.repo = repo; }
    public List<BakeryProduct> search(String q) {
        if (q == null || q.isBlank()) return repo.findAll();
        String k = q.toLowerCase();
        return repo.findAll().stream().filter(p -> p.getName().toLowerCase().contains(k) || p.getCategory().toLowerCase().contains(k)).toList();
    }
    public BakeryProduct find(String id) { return repo.findById(id).orElseThrow(() -> new ProductNotFoundException(id)); }
    public void save(String id, String cat, String name, double price, int stock, String detail) {
        repo.save(BakeryProduct.of(cat, id == null || id.isBlank() ? FileRepository.newId("P") : id, name, price, stock, detail));
    }
    public void delete(String id) { repo.delete(id); }
}
