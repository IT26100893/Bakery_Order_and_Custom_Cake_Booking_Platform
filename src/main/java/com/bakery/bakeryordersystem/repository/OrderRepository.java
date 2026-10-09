package com.bakery.bakeryordersystem.repository;
import com.bakery.bakeryordersystem.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Repository
public class OrderRepository {
    private final JdbcTemplate jdbc;
    public OrderRepository(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    private List<OrderItem> itemsOf(String orderId) {
        return jdbc.query("SELECT * FROM order_items WHERE order_id=? ORDER BY id",
                (rs, i) -> new OrderItem(rs.getString("product_id"), rs.getString("product_name"), rs.getInt("qty"), rs.getDouble("unit_price")), orderId);
    }
    public List<Order> findAll() {
        return jdbc.query("SELECT * FROM orders", (rs, i) -> new Order(rs.getString("id"), rs.getString("user_id"), rs.getString("customer_name"),
                rs.getString("status"), Payment.of(rs.getString("payment_type")), itemsOf(rs.getString("id"))));
    }
    public List<Order> findByUserId(String uid) { return findAll().stream().filter(o -> o.getUserId().equals(uid)).toList(); }
    public Optional<Order> findById(String id) { return findAll().stream().filter(o -> o.getId().equals(id)).findFirst(); }

    @Transactional
    public void save(Order o) {
        jdbc.update("INSERT INTO orders(id,user_id,customer_name,status,payment_type) VALUES(?,?,?,?,?) " +
                        "ON DUPLICATE KEY UPDATE user_id=VALUES(user_id), customer_name=VALUES(customer_name), status=VALUES(status), payment_type=VALUES(payment_type)",
                o.getId(), o.getUserId(), o.getCustomerName(), o.getStatus(), o.getPayment().getType());
        jdbc.update("DELETE FROM order_items WHERE order_id=?", o.getId());
        for (OrderItem it : o.getItems())
            jdbc.update("INSERT INTO order_items(order_id,product_id,product_name,qty,unit_price) VALUES(?,?,?,?,?)",
                    o.getId(), it.getProductId(), it.getProductName(), it.getQty(), it.getUnitPrice());
    }
    public void delete(String id) { jdbc.update("DELETE FROM orders WHERE id=?", id); }
}
