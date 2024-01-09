package befaster.entity;

import java.util.HashMap;

public class Checkout {
    private HashMap<Item, Integer> basket;
    private HashMap<Character, Integer> freeItems;

    public Checkout(HashMap<Item, Integer> basket){
        this.basket = basket;
        this.freeItems = new HashMap<>();
    }

    public int calculateCheckoutValue() {
        int checkoutValue = 0;
        for (var basketEntry : this.basket.entrySet()) {
            checkoutValue += getItemPrice(basketEntry.getKey(), basketEntry.getValue());
        }
        return checkoutValue;
    }

    private int getItemPrice(Item item, int quantity) {
        var freeItemQuantity = this.freeItems.getOrDefault(item.getSku(), 0);
        if(freeItemQuantity > 0){
            quantity = freeItemQuantity >= quantity ? 0 : quantity - freeItemQuantity;
        }

        int finalPrice = quantity * item.getPrice();

        if(!item.isSpecialOfferApplicable(quantity))
            return finalPrice;

        for (var specialOffer : item.filterApplicableSpecialOffers(quantity)) {
            if(specialOffer.isFreeItemOffer()){
                this.freeItems.put(specialOffer.getFreeItemSKU(), quantity / specialOffer.getQuantity());
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



