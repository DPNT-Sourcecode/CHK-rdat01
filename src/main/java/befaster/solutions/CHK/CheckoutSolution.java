package befaster.solutions.CHK;

import befaster.entity.Item;
import befaster.entity.SpecialOffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class CheckoutSolution {

    private HashMap<Character, Item> itemsList;

    public CheckoutSolution(){
        itemsList = new HashMap<>();
        populateItemsList();
    }

    public Integer checkout(String skus) {
        Integer checkoutValue = 0;
        char[] basketList = skus.toCharArray();

        for (char sku : basketList) {
            if(!itemsList.containsKey(sku)){
                return -1;
            }

            checkoutValue += itemsList.get(sku);
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



