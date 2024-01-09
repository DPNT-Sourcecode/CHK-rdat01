package befaster.entity;

import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;

public class Checkout{
    private TreeMap<Item, Integer> basket;
    private HashMap<Character, Integer> freeItems;

    public Checkout(){
        this.basket = new TreeMap<>((item1, item2) -> item1.hasFreeItemSpecialOffer() ? 0 : 1);
        this.freeItems = new HashMap<>();
    }

    public HashMap<Character, Integer> getFreeItems() {
        return freeItems;
    }

    public void addItem(Item item, int quantity){
        basket.put(item, quantity);
    }

    public int calculateCheckoutValue() {
        int checkoutValue = 0;

        for (var basketEntry : basket.entrySet()) {
            checkoutValue += calculateItemPrice(basketEntry.getKey(), basketEntry.getValue());
        }

        return checkoutValue;
    }

    private int calculateItemPrice(Item item, int quantity) {
        int finalPrice = quantity * item.getPrice();

        if(!item.isSpecialOfferApplicable(quantity))
            return finalPrice;

        var freeItemQuantity = freeItems.getOrDefault(item.getSku(), 0);

        for (var specialOffer : item.filterApplicableSpecialOffers(quantity)) {
            var freeItemBasketQuantity = basket.keySet().stream()
                    .filter(basketItem -> basketItem.getSku() == specialOffer.getFreeItemSKU())
                    .count();

            if(specialOffer.isFreeItemOffer() && freeItemBasketQuantity > 0){
                freeItemQuantity += quantity / specialOffer.getQuantity();

                freeItems.put(specialOffer.getFreeItemSKU(), freeItemQuantity);
                continue;
            }

            if(specialOffer.isFreeItemOffer()){
                continue;
            }

            finalPrice = Math.min(finalPrice, calculateSpecialPriceOffer(item, quantity, specialOffer));
        }

        return finalPrice;
    }

    private int calculateSpecialPriceOffer(Item item, int quantity, SpecialOffer specialOffer) {
        int remainder = quantity % specialOffer.getQuantity();
        var remainderPrice = calculateItemPrice(item, remainder);

        int divisionResult = quantity / specialOffer.getQuantity();

        return remainderPrice + divisionResult * specialOffer.getPrice();
    }
}