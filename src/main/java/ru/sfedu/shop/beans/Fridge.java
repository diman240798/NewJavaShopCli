package ru.sfedu.shop.beans;

import com.opencsv.bean.CsvBindByName;
import ru.sfedu.shop.api.helper.InitializerData;

import java.io.Serializable;
import java.util.Objects;

public class Fridge extends Product implements Serializable {
    @CsvBindByName
    private int volume;
    @CsvBindByName
    private String color;
    @CsvBindByName
    private int power;
    @CsvBindByName
    private boolean noFrost;

    public Fridge() {}

    public Fridge(long id, String name, double weight, double price, int volume, String color, int power, boolean noFrost) {
        this(id, name, weight, price, volume, color, power);
        this.noFrost = noFrost;
    }

    public Fridge(long id, String name, double weight, double price, int volume, String color, int power) {
        super(id, name, weight, price, InitializerData.CATEGORY_FRIDGE);
        this.volume = volume;
        this.color = color;
        this.power = power;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public boolean isNoFrost() {
        return noFrost;
    }

    public void setNoFrost(boolean noFrost) {
        this.noFrost = noFrost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fridge)) return false;
        if (!super.equals(o)) return false;
        Fridge fridge = (Fridge) o;
        return getVolume() == fridge.getVolume() &&
                getPower() == fridge.getPower() &&
                isNoFrost() == fridge.isNoFrost() &&
                Objects.equals(getColor(), fridge.getColor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getVolume(), getColor(), getPower(), isNoFrost());
    }

    @Override
    public String toString() {
        return "Fridge{" +
                "volume=" + volume +
                ", color='" + color + '\'' +
                ", power=" + power +
                ", noFrost=" + noFrost +
                '}';
    }
}
