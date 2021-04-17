package ru.hse.java.repetinder;

public class Tutor extends User {
    public Tutor(String username, String password) {
        super(username, password);
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMinPrice() {
        return minPrice;
    }

    private int minPrice;
}
