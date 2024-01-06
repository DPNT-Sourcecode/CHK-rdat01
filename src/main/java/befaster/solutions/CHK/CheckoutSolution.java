package befaster.solutions.CHK;

import befaster.entity.Item;
import befaster.entity.SpecialOffer;

import java.util.ArrayList;
import java.util.List;

public class CheckoutSolution {

    private List<Item> itemsList;

    public CheckoutSolution(){
        itemsList = new ArrayList<>();
        populateItemsList();
    }

    public Integer checkout(String skus) {
        Integer checkoutValue = 0;
        char[] basketList = skus.toCharArray();

        for (char sku : basketList) {
            if(!pricesTable.containsKey(sku)){
                return -1;
            }

            checkoutValue += pricesTable.get(sku);
        }

        return checkoutValue;
    }

    private void populateItemsList(){
        itemsList.add(new Item('A', 50, new SpecialOffer(3, 130)));
        itemsList.add(new Item('B', 30, new SpecialOffer(2, 45)));
        itemsList.add(new Item('C', 20));
        itemsList.add(new Item('D', 15));
    }
}


