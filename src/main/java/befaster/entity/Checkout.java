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
            value += getFinalPrice(basketEntry.getKey(), basketEntry.getValue());
            if (basketEntry.getKey().
        }
    }

    public int getFinalPrice(Item item, int quantity) {
        int finalPrice = quantity * item.getPrice();

        if(!item.isSpecialOfferApplicable(quantity))
            return finalPrice;

        for (var specialOffer : item.filterApplicableSpecialOffers(quantity)) {
            /*var freeItemBasketQuantity = basket.getOrDefault(specialOffer.getFreeItemSKU(), 0);

            if(specialOffer.isFreeItemOffer() && freeItemBasketQuantity > 0)
                return finalPrice - calculateFreeItemOffer(specialOffer, freeItemBasketQuantity, basket);*/

            if(specialOffer.isFreeItemOffer())
                continue;

            finalPrice = Math.min(finalPrice, calculateSpecialPriceOffer(quantity, specialOffer));
        }

        return finalPrice;
    }
}

