package ru.sfedu.shop.beans;


import com.opencsv.bean.CsvBindByName;
import ru.sfedu.shop.api.helper.InitializerData;

import java.io.Serializable;
import java.util.Objects;

public class Soda extends Product implements Serializable {
    @CsvBindByName
    private String flavour;
    @CsvBindByName
    private boolean sparkled = false;

    public Soda() {}

    public Soda(long id, String name, double weight, double price, String flavour, boolean sparkled) {
        this(id, name, weight, price, flavour);
        this.sparkled = sparkled;
    }

    public Soda(long id, String name, double weight, double price, String flavour) {
        super(id, name, weight, price, InitializerData.CATEGORY_SODA);
        this.flavour = flavour;
    }

    public String getFlavour() {
        return flavour;
    }

    public void setFlavour(String flavour) {
        this.flavour = flavour;
    }

    public boolean isSparkled() {
        return sparkled;
    }

    public void setSparkled(boolean sparkled) {
        this.sparkled = sparkled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Soda)) return false;
        Soda soda = (Soda) o;
        return isSparkled() == soda.isSparkled() &&
                Objects.equals(getFlavour(), soda.getFlavour());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFlavour(), isSparkled());
    }

    @Override
    public String toString() {
        return "Soda{" +
                "flavour='" + flavour + '\'' +
                ", sparkled=" + sparkled +
                '}';
    }
}
