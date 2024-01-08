package befaster.entity;

import java.util.HashMap;

public class Checkout {
    private HashMap<Item, Integer> basket;
    private HashMap<Character, Integer> freeItems;
    private int value;

    public Checkout(HashMap<Item, Integer> basket){
        this.basket = basket;
        this.freeItems = new HashMap<>();
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
        var freeItemQuantity = freeItems.getOrDefault(item.getSku(), 0);
        if(freeItemQuantity > 0){
            quantity -= freeItemQuantity;
        }

        int finalPrice = quantity * item.getPrice();

        if(!item.isSpecialOfferApplicable(quantity))
            return finalPrice;

        for (var specialOffer : item.filterApplicableSpecialOffers(quantity)) {
            /*var freeItemBasketQuantity = basket.getOrDefault(specialOffer.getFreeItemSKU(), 0);

            if(specialOffer.isFreeItemOffer() && freeItemBasketQuantity > 0)
                return finalPrice - calculateFreeItemOffer(specialOffer, freeItemBasketQuantity, basket);*/

            if(specialOffer.isFreeItemOffer()){
                freeItems.put(specialOffer.getFreeItemSKU(), quantity / specialOffer.getQuantity());
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





