package befaster.solutions.CHK;

import java.util.HashMap;
import java.util.Map;

public class CheckoutSolution {

    private Map<String, Integer> pricesTable;

    public CheckoutSolution(){
        pricesTable = new HashMap<>();
        populatePriceTable();
    }

    public Integer checkout(String skus) {
        Char basketList = skus.
    }

    private void populatePriceTable(){
        pricesTable.put("A", 50);
        pricesTable.put("B", 30);
        pricesTable.put("C", 20);
        pricesTable.put("D", 15);
    }
}




