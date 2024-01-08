package befaster.entity;

import java.util.HashMap;

public class Checkout {
    private HashMap<Item, Integer> basket;
    private int value;

    public Checkout(HashMap<Item, Integer> basket){
        this.basket = basket;
        calculateCheckoutValue();
    }

    public int getValue() {
        return value;
    }

    private void calculateCheckoutValue() {
        for (var basketEntry : basket.entrySet()) {
            value += basketEntry.getKey().getFinalPrice(basketEntry.getValue(), skusAndQuantities);
        }
    }
}




