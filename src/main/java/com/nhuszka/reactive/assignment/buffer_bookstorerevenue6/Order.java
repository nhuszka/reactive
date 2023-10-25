package com.nhuszka.reactive.assignment.buffer_bookstorerevenue6;

public class Order {

    private String category;
    private Integer price;

    public Order(String category, Integer price) {
        this.category = category;
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public Integer getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "OrderedBook{" +
                "category='" + category + '\'' +
                ", price=" + price +
                '}';
    }
}
