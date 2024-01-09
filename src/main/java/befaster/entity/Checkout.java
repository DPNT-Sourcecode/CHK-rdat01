package befaster.entity;

import java.util.HashMap;

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

        for (var basketEntry : basket.entrySet()) {
            checkoutValue += calculateItemPrice(basketEntry.getKey(), basketEntry.getValue());
        }

        for (var freeItem : freeItems.entrySet()) {
            
        }

        return checkoutValue;
    }

    private void evaluateFreeItems() {

    }

    private int calculateItemPrice(Item item, int quantity) {
        int finalPrice = quantity * item.getPrice();

        if(!item.isSpecialOfferApplicable(quantity))
            return finalPrice;

        var freeItemQuantity = freeItems.getOrDefault(item.getSku(), 0);

        for (var specialOffer : item.filterApplicableSpecialOffers(quantity)) {
            if(specialOffer.isFreeItemOffer()){
                freeItemQuantity += quantity / specialOffer.getQuantity();

                freeItems.put(specialOffer.getFreeItemSKU(), freeItemQuantity);
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



