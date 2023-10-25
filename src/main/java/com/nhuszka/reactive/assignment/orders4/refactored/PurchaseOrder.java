package com.nhuszka.reactive.assignment.orders4.refactored;

public class PurchaseOrder {

    private final String inventoryName;
    private final Integer amount;
    private final Integer pricePerAmount;

    public PurchaseOrder(String inventoryName, Integer amount, Integer pricePerAmount) {
        this.inventoryName = inventoryName;
        this.amount = amount;
        this.pricePerAmount = pricePerAmount;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public Integer getAmount() {
        return amount;
    }

    public Integer getPricePerAmount() {
        return pricePerAmount;
    }
}
