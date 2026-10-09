package com.bakery.bakeryordersystem.service;
import com.bakery.bakeryordersystem.exception.*;
import com.bakery.bakeryordersystem.model.*;
import com.bakery.bakeryordersystem.repository.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orders;
    private final ProductRepository products;
    public OrderService(OrderRepository orders, ProductRepository products) { this.orders = orders; this.products = products; }

    public List<Order> findAll() { return orders.findAll(); }
    public List<Order> findByUser(String userId) { return orders.findByUserId(userId); }

    /** userId = "" for walk-in orders entered by an admin. */
    public void place(String userId, String customer, String productId, int qty, String payType) {
        if (qty < 1) throw new IllegalArgumentException("Quantity must be at least 1");
        BakeryProduct p = products.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
        p.adjustStock(-qty);   // throws if stock is too low
        products.save(p);
        OrderItem item = new OrderItem(p.getId(), p.getName(), qty, p.getPrice());
        orders.save(new Order(FileRepository.newId("O"), userId, customer, "PLACED", Payment.of(payType), List.of(item)));
    }
    public void updateStatus(String id, String status) {
        Order o = orders.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        o.setStatus(status); orders.save(o);
    }
    /** A customer may cancel only their own order, and only while it is still PLACED. Stock is returned. */
    public void cancelOwn(String userId, String id) {
        Order o = orders.findById(id).filter(x -> x.getUserId().equals(userId)).orElseThrow(() -> new OrderNotFoundException(id));
        if (!"PLACED".equals(o.getStatus())) throw new IllegalArgumentException("Only orders that are still PLACED can be cancelled");
        for (OrderItem it : o.getItems()) products.findById(it.getProductId()).ifPresent(p -> { p.adjustStock(it.getQty()); products.save(p); });
        o.setStatus("CANCELLED"); orders.save(o);
    }
    public void delete(String id) { orders.delete(id); }
}
