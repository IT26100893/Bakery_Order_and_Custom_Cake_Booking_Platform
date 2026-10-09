package com.bakery.bakeryordersystem.model;
public class OnlinePayment extends Payment {
    @Override public String getType() { return "ONLINE"; }
    @Override public double calculateFee(double amount) { return amount * 0.015; }
}
