package befaster.entity;

import java.util.*;

public class Checkout {
    private HashMap<Item, Integer> basket;
    private HashMap<Character, Integer> freeItems;

    public Checkout(){
        this.basket = new HashMap<>();
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

        var sortedBasket = new TreeMap<Item, Integer>(new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2) {
                return Boolean.compare(item2.hasFreeItemSpecialOffer(), item1.hasFreeItemSpecialOffer());
            }
        });

        sortedBasket.putAll(basket);

        for (var basketEntry : sortedBasket.entrySet()) {
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





