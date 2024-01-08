package befaster.entity;

import java.util.HashMap;

public class Checkout {
    private HashMap<Item, Integer> basket;
    private int value;

    public Checkout(HashMap<Item, Integer> basket){
        this.basket = basket;
        value = 0;
    }

    public int getValue() {
        return value;
    }
}


