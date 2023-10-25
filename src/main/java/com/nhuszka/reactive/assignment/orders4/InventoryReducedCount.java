package com.nhuszka.reactive.assignment.orders4;

public class InventoryReducedCount {

    private final String inventoryName;
    private final Integer inventoryCount;

    public InventoryReducedCount(String inventoryName, Integer inventoryCount) {
        this.inventoryName = inventoryName;
        this.inventoryCount = inventoryCount;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public Integer getInventoryCount() {
        return inventoryCount;
    }

    @Override
    public String toString() {
        return "InventoryReducedCount{" +
                "inventoryName='" + inventoryName + '\'' +
                ", inventoryCount=" + inventoryCount +
                '}';
    }
}
