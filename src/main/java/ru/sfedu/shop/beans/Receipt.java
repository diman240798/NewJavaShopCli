package ru.sfedu.shop.beans;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import ru.sfedu.shop.api.helper.ProductTransformer;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Receipt implements Serializable {
    @CsvBindByName
    private long id;
    @CsvCustomBindByName(column = "products", converter = ProductTransformer.class)
    private List<Product> products;
    @CsvBindByName
    private double totalPrice;


    public Receipt() {}

    public Receipt(long id, List<Product> products, double totalPrice) {
        this.id = id;
        this.products = products;
        this.totalPrice = totalPrice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Receipt)) return false;
        Receipt receipt = (Receipt) o;
        return getId() == receipt.getId() &&
                Double.compare(receipt.getTotalPrice(), getTotalPrice()) == 0 &&
                Objects.equals(getProducts(), receipt.getProducts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getProducts(), getTotalPrice());
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "id=" + id +
                ", products=" + products +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
