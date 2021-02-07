package ru.sfedu.shop.beans;

import java.util.Objects;

public class Product {
    protected long id;
    protected String name;
    protected double weight;
    protected double price;
    protected String category;

    public Product() {}

    public Product(long id, String name, double weight, double price, String category) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.price = price;
        this.category = category;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Double.compare(product.getWeight(), getWeight()) == 0 &&
                Double.compare(product.getPrice(), getPrice()) == 0 &&
                Objects.equals(getName(), product.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName(), getWeight(), getPrice());
    }

    @Override
    public String toString() {
        return "Product{" +
                "name=" + name +
                ", weight=" + weight +
                ", price=" + price +
                ", id=" + id +
                '}';
    }
}
