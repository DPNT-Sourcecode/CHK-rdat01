package befaster.entity;

import java.util.HashMap;

public class Checkout {
    private HashMap<Item, Integer> basket;
    private HashMap<Character, Integer> freeItems;

    public Checkout(){
        this.basket = new HashMap<>();
        this.freeItems = new HashMap<>();
    }

    public int calculateCheckoutValue() {
        int checkoutValue = 0;
        for (var basketEntry : basket.entrySet()) {
            checkoutValue += getItemPrice(basketEntry.getKey(), basketEntry.getValue());
        }
        return checkoutValue;
    }

    private int getItemPrice(Item item, int quantity) {
        var freeItemQuantity = freeItems.getOrDefault(item.getSku(), 0);
        if(freeItemQuantity > 0){
            quantity = freeItemQuantity >= quantity ? 0 : quantity - freeItemQuantity;
            freeItems.remove(item.getSku());
        }

        int finalPrice = quantity * item.getPrice();

        if(!item.isSpecialOfferApplicable(quantity))
            return finalPrice;

        for (var specialOffer : item.filterApplicableSpecialOffers(quantity)) {
            if(specialOffer.isFreeItemOffer()){
                var currentFreeItemQuantity = freeItems.getOrDefault(specialOffer.getFreeItemSKU(),0);
                currentFreeItemQuantity += quantity / specialOffer.getQuantity();

                freeItems.put(specialOffer.getFreeItemSKU(), currentFreeItemQuantity);
                continue;
            }

            finalPrice = Math.min(finalPrice, calculateSpecialPriceOffer(item, quantity, specialOffer));
        }

        return finalPrice;
    }

    private int calculateSpecialPriceOffer(Item item, int quantity, SpecialOffer specialOffer) {
        int remainder = quantity % specialOffer.getQuantity();
        var remainderPrice = getItemPrice(item, remainder);

        int divisionResult = quantity / specialOffer.getQuantity();

        return remainderPrice + divisionResult * specialOffer.getPrice();
    }
}




