package com.nhuszka.reactive.assignment.groupkidsautomotive7;

public class ItemOrder {

    private Integer id;
    private String category;
    private Integer price;

    private double updatedPrice;

    public ItemOrder(Integer id, String category, Integer price) {
        this.id = id;
        this.category = category;
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public Integer getPrice() {
        return price;
    }

    public void setUpdatedPrice(double updatedPrice) {
        this.updatedPrice = updatedPrice;
    }

    @Override
    public String toString() {
        return "Order{" +
                ", id=" + id +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", updatedPrice=" + updatedPrice +
                '}';
    }
}
