package com.bakery.bakeryordersystem.model;
public class CardPayment extends Payment {
    @Override public String getType() { return "CARD"; }
    @Override public double calculateFee(double amount) { return amount * 0.02; }
}
