package com.bakery.bakeryordersystem.model;
/** Abstract product; Cake, Pastry and Bread override category/detail (inheritance + polymorphism). */
public abstract class BakeryProduct {
    private final String id, name;
    private final double price;
    private int stock;
    protected BakeryProduct(String id, String name, double price, int stock) {
        this.id = id; this.name = User.clean(name); this.price = price; this.stock = stock;
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public void adjustStock(int delta) {
        if (stock + delta < 0) throw new IllegalArgumentException("Not enough stock for " + name);
        stock += delta;
    }
    public abstract String getCategory();
    public abstract String getDetail();
    public static BakeryProduct of(String cat, String id, String n, double p, int s, String d) {
        d = User.clean(d);
        return switch (cat) { case "CAKE" -> new Cake(id, n, p, s, d); case "PASTRY" -> new Pastry(id, n, p, s, d); default -> new Bread(id, n, p, s, d); };
    }
}
