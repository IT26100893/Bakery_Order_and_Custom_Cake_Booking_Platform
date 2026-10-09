package com.bakery.bakeryordersystem.model;
public class OrderItem {
    private final String productId, productName;
    private final int qty;
    private final double unitPrice;
    public OrderItem(String productId, String productName, int qty, double unitPrice) {
        this.productId = productId; this.productName = productName.replace(":", " ").replace(";", " "); this.qty = qty; this.unitPrice = unitPrice;
    }
    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQty() { return qty; }
    public double getUnitPrice() { return unitPrice; }
    public double getSubtotal() { return qty * unitPrice; }
}
