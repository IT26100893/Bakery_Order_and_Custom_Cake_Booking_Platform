package com.bakery.bakeryordersystem.model;
import java.util.List;
public class Order {
    private final String id, userId, customerName;
    private final Payment payment;
    private final List<OrderItem> items;
    private String status;
    /** userId is the owner's account id ("" for walk-in orders entered by an admin). */
    public Order(String id, String userId, String customerName, String status, Payment payment, List<OrderItem> items) {
        this.id = id; this.userId = userId == null ? "" : userId; this.customerName = User.clean(customerName);
        this.status = status; this.payment = payment; this.items = items;
    }
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getCustomerName() { return customerName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Payment getPayment() { return payment; }
    public List<OrderItem> getItems() { return items; }
    public double getSubtotal() { return items.stream().mapToDouble(OrderItem::getSubtotal).sum(); }
    public double getFee() { return payment.calculateFee(getSubtotal()); }
    public double getTotal() { return getSubtotal() + getFee(); }
}
