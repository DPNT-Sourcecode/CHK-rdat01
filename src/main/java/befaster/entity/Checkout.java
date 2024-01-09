package befaster.entity;

import java.util.HashMap;

public class Checkout {
    private HashMap<Item, Integer> basket;
    private HashMap<Character, Integer> freeItems;
    private boolean isFreeItemsFlow;

    public Checkout(){
        this.basket = new HashMap<>();
        this.freeItems = new HashMap<>();
        this.isFreeItemsFlow = false;
    }

    public Checkout(boolean isFreeItemsFlow){
        this.basket = new HashMap<>();
        this.freeItems = new HashMap<>();
        this.isFreeItemsFlow = isFreeItemsFlow;
    }

    public HashMap<Character, Integer> getFreeItems() {
        return freeItems;
    }

    public void addItem(Item item, int quantity){
        basket.put(item, quantity);
    }

    public void updateQuantitiesIfHasFreeItems(){
        for (var basketEntry : basket.entrySet()) {
            var item = basketEntry.getKey();
            var quantity = basketEntry.getValue();
            var freeItemQuantity = freeItems.getOrDefault(item.getSku(), 0);

            for (var specialOffer : item.filterApplicableSpecialOffers(quantity)) {
                var freeItemBasketQuantity = basket.keySet().stream()
                        .filter(basketItem -> basketItem.getSku() == specialOffer.getFreeItemSKU())
                        .count();

                if(specialOffer.isFreeItemOffer() && freeItemBasketQuantity > 0){
                    freeItemQuantity += quantity / specialOffer.getQuantity();

                    freeItems.put(specialOffer.getFreeItemSKU(), freeItemQuantity);
                }
            }
        }
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

        if(isFreeItemsFlow){
            return 0;
        }

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
