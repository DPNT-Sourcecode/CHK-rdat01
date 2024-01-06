package befaster.solutions.CHK;

import befaster.entity.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CheckoutSolution {

    private List<Item> itemsList;

    public CheckoutSolution(){
        itemsList = new ArrayList<>();
        populatePriceTable();
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

    private void populatePriceTable(){
        itemsList.add('A', 50);
        pricesTable.put('B', 30);
        pricesTable.put('C', 20);
        pricesTable.put('D', 15);
    }
}

