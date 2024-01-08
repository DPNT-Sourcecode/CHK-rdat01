package befaster.entity;

import java.util.HashMap;

public class Checkout {
    private HashMap<Item, Integer> basket;
    private int value;

    public Checkout(HashMap<Item, Integer> basket){
        basket = new HashMap<>();
        value = 0;
    }
}

