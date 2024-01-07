package befaster.solutions.CHK;

import befaster.entity.Item;
import befaster.entity.SpecialOffer;

import java.util.HashMap;

public class CheckoutSolution {

    private HashMap<Character, Item> itemsList;

    public CheckoutSolution(){
        itemsList = new HashMap<>();
        populateItemsList();
    }

    public Integer checkout(String skus) {
        Integer checkoutValue = 0;
        HashMap<Character, Integer> basket = new HashMap<>();
        char[] basketList = skus.toCharArray();

        for (char sku : basketList) {
            if(!itemsList.containsKey(sku)){
                return -1;
            }

            if(!basket.containsKey(sku)){
                basket.put(sku, 0);
            }

            int currentQuantity = basket.get(sku);
            basket.put(sku, currentQuantity + 1);
        }

        for (char sku : basket.keySet()) {
            checkoutValue += itemsList.get(sku).getFinalPrice(basket.get(sku));
        }

        return checkoutValue;
    }

    private void populateItemsList(){
        itemsList.put('A', new Item('A', 50, new SpecialOffer(3, 130)));
        itemsList.put('B', new Item('B', 30, new SpecialOffer(2, 45)));
        itemsList.put('C', new Item('C', 20));
        itemsList.put('D', new Item('D', 15));
    }
}
