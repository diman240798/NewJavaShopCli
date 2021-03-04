package ru.sfedu.shop.beans;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import ru.sfedu.shop.api.helper.ProductTransformer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Bucket implements Serializable {
    @CsvBindByName
    private String id;
    @CsvCustomBindByName(column = "products", converter = ProductTransformer.class)
    private List<Product> products;

    public Bucket() {}

    public Bucket(String id, List<Product> products) {
        this.id = id;
        this.products = products;
    }

    public Bucket(String id) {
        this.id = id;
        this.products = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bucket)) return false;
        Bucket bucket = (Bucket) o;
        return Objects.equals(getId(), bucket.getId()) &&
                Objects.equals(getProducts(), bucket.getProducts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getProducts());
    }

    @Override
    public String toString() {
        return "Bucket{" +
                "id='" + id + '\'' +
                ", products=" + products +
                '}';
    }
}