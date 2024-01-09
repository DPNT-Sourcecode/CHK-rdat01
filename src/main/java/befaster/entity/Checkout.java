package befaster.entity;

import java.util.*;

public class Checkout {
    private HashMap<Item, Integer> basket;
    private HashMap<Character, Integer> freeItems;

    public Checkout(){
        this.basket = new HashMap<>();
        this.freeItems = new HashMap<>();
    }

    public void addItem(Item item, int quantity){
        basket.put(item, quantity);
    }

    public int calculateCheckoutValue() {
        int checkoutValue = 0;

        var sortedBasket = new TreeMap<Item, Integer>((item1, item2) -> {
            var valueCompare = Boolean.compare(item2.hasFreeItemSpecialOffer(), item1.hasFreeItemSpecialOffer());
            return (valueCompare != 0) ? valueCompare : item1.getSku().compareTo(item2.getSku());
        });

        sortedBasket.putAll(basket);

        for (var basketEntry : sortedBasket.entrySet()) {
            checkoutValue += calculateItemPrice(basketEntry.getKey(), basketEntry.getValue());
        }

        return checkoutValue;
    }

    private int calculateItemPrice(Item item, int quantity) {
        var freeItemQuantity = freeItems.getOrDefault(item.getSku(), 0);
        if(freeItemQuantity > 0){
            quantity = freeItemQuantity >= quantity ? 0 : (quantity - freeItemQuantity);
            freeItems.remove(item.getSku());
        }

        int finalPrice = quantity * item.getPrice();

        if(!item.isSpecialOfferApplicable(quantity))
            return finalPrice;

        for (var specialOffer : item.filterApplicableSpecialOffers(quantity)) {

            if(specialOffer.isDifferentItemFreeOffer() && haveFreeItemsInTheBasket(basket, specialOffer)){
                freeItemQuantity += quantity / specialOffer.getQuantity();
                freeItems.put(specialOffer.getFreeItemSKU(), freeItemQuantity);
                continue;
            }

            if(specialOffer.isDifferentItemFreeOffer()){
                continue;
            }

            if(specialOffer.isSameItemFreeOffer() && quantity % specialOffer.getQuantity() == 0){
                var teste = (quantity - specialOffer.getQuantity()) * specialOffer.getPrice();
                if(teste > 0){
                    return finalPrice - teste;
                }
                return finalPrice - (quantity / 3) * specialOffer.getPrice();
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

    private boolean haveFreeItemsInTheBasket(HashMap<Item, Integer> basket, SpecialOffer specialOffer) {
        return basket.keySet().stream()
                .filter(basketItem -> basketItem.getSku() == specialOffer.getFreeItemSKU())
                .count() > 0;
    }
}


