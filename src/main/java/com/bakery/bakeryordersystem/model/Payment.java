package com.bakery.bakeryordersystem.model;
/** Polymorphism: each payment type calculates its own fee. */
public abstract class Payment {
    public abstract String getType();
    public abstract double calculateFee(double amount);
    public static Payment of(String type) {
        return switch (type == null ? "" : type) { case "CARD" -> new CardPayment(); case "ONLINE" -> new OnlinePayment(); default -> new CashPayment(); };
    }
}
