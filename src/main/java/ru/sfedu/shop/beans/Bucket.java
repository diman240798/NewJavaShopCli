package ru.sfedu.shop.beans;

import ru.sfedu.shop.Constants;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Bucket {
    private long id;
    private String session;
    private String products = "";

    public Bucket() {}

    public Bucket(long id, String session) {
        setId(id);
        this.session = session;
    }

    public Bucket(long id, String session, String products) {
        this(id, session);
        this.products = products;
    }

    public String getSession() {
        return session;
    }

    public List<String> getProductsList() {
        return Arrays.asList(products.split(Constants.PRODUCTS_SEPATOR));
    }

    public String getProducts() {
        return products;
    }


    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void addProduct(long productId, Category category) {
        String categoryName = category.getName();
        if (products.length() > 0) {
            products = products + Constants.PRODUCTS_SEPATOR + categoryName + Constants.PRODUCT_CATEGORY_SEPARATOR + productId;
        } else {
            products = categoryName + Constants.PRODUCT_CATEGORY_SEPARATOR + productId;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bucket)) return false;
        Bucket bucket = (Bucket) o;
        return Objects.equals(getSession(), bucket.getSession()) &&
                Objects.equals(products, bucket.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSession(), products, id);
    }

    @Override
    public String toString() {
        return "Bucket{" +
                "session=" + session +
                ", products=" + products +
                ", id=" + id +
                '}';
    }

}