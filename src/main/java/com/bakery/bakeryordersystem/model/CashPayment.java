package com.bakery.bakeryordersystem.model;
public class CashPayment extends Payment {
    @Override public String getType() { return "CASH"; }
    @Override public double calculateFee(double amount) { return 0; }
}
