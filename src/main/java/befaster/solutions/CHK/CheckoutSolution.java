package befaster.solutions.CHK;

import befaster.entity.Item;
import befaster.entity.SpecialOffer;
import befaster.entity.enums.SpecialOfferType;

import java.util.HashMap;

public class CheckoutSolution {

    private HashMap<Character, Item> itemsList;

    public CheckoutSolution(){
        itemsList = new HashMap<>();
        createItemsList();
    }

    public Integer checkout(String skus) {
        var checkoutValue = 0;
        var basket = new HashMap<Character, Integer>();
        var basketList = skus.trim().replaceAll("\\p{C}", "").toCharArray();

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
            checkoutValue += itemsList.get(sku).getFinalPrice(basket.get(sku), basket);
        }

        return checkoutValue;
    }

    private void createItemsList(){
        var itemA = new Item('A', 50);
        itemA.AddSpecialOffers(
                new SpecialOffer(3, 130, SpecialOfferType.SPECIAL_PRICE),
                new SpecialOffer(5, 200, SpecialOfferType.SPECIAL_PRICE)
        );

        var itemB = new Item('B', 30);
        itemB.AddSpecialOffers(
                new SpecialOffer(2, 45, SpecialOfferType.SPECIAL_PRICE)
        );

        var itemC = new Item('C', 20);
        var itemD = new Item('D', 15);
        var itemE = new Item('E', 40);
        itemE.AddSpecialOffers(
                new SpecialOffer(2, itemB.getSku(), itemB.getPrice(), SpecialOfferType.FREE_ITEM)
        );

        itemsList.put(itemA.getSku(), itemA);
        itemsList.put(itemB.getSku(), itemB);
        itemsList.put(itemC.getSku(), itemC);
        itemsList.put(itemD.getSku(), itemD);
        itemsList.put(itemE.getSku(), itemE);
    }
}
