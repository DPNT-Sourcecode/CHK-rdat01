package befaster.entity;

import java.util.HashMap;

public class Checkout {
    private HashMap<Item, Integer> basket;
    private int value;

    public Checkout(HashMap<Item, Integer> basket){
        this.basket = basket;
        calculateCheckoutValue();
    }

    public int getValue() {
        return value;
    }

    private void calculateCheckoutValue() {
        for (var basketEntry : basket.entrySet()) {
            value += getItemPrice(basketEntry.getKey(), basketEntry.getValue());
        }
    }

    public int getItemPrice(Item item, int quantity) {
        int finalPrice = quantity * item.getPrice();

        if(!item.isSpecialOfferApplicable(quantity))
            return finalPrice;

        for (var specialOffer : item.filterApplicableSpecialOffers(quantity)) {
            /*var freeItemBasketQuantity = basket.getOrDefault(specialOffer.getFreeItemSKU(), 0);

            if(specialOffer.isFreeItemOffer() && freeItemBasketQuantity > 0)
                return finalPrice - calculateFreeItemOffer(specialOffer, freeItemBasketQuantity, basket);*/

            if(specialOffer.isFreeItemOffer())
                continue;

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


