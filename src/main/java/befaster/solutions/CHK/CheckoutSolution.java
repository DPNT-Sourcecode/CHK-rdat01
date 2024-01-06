package befaster.solutions.CHK;

import java.util.HashMap;
import java.util.Map;

public class CheckoutSolution {

    private Map<Character, Integer> pricesTable;

    public CheckoutSolution(){
        pricesTable = new HashMap<>();
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
        pricesTable.put('A', 50);
        pricesTable.put('B', 30);
        pricesTable.put('C', 20);
        pricesTable.put('D', 15);
    }
}
