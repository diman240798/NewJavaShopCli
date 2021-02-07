package ru.sfedu.shop.beans;

import java.util.Objects;

public class Fridge extends Product {
    private int volume;
    private String color;
    private int power;
    private boolean noFrost = false;

    public Fridge() {}

    public Fridge(long id, String name, double weight, double price, String category, int volume, String color, int power, boolean noFrost) {
        this(id, name, weight, price, category, volume, color, power);
        this.noFrost = noFrost;
    }
    public Fridge(long id, String name, double weight, double price, String category, int volume, String color, int power) {
        super(id, name, weight, price, category);
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
                ", color=" + color +
                ", power=" + power +
                ", noFrost=" + noFrost +
                ", name=" + name +
                ", weight=" + weight +
                ", price=" + price +
                ", category=" + category +
                ", id=" + id +
                '}';
    }
}
