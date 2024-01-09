package befaster.entity;

import java.util.HashMap;

public class Checkout {
    private HashMap<Item, Integer> basket;
    private HashMap<Character, Integer> freeItems;
    private int value;

    public Checkout(HashMap<Item, Integer> basket){
        this.basket = basket;
        this.freeItems = new HashMap<>();
        this.value = 0;
        calculateCheckoutValue();
    }

    public int getValue() {
        return this.value;
    }

    private void calculateCheckoutValue() {
        //evaluateFreeItems();

        for (var basketEntry : this.basket.entrySet()) {
            this.value += getItemPrice(basketEntry.getKey(), basketEntry.getValue());
        }
    }

    private void evaluateFreeItems() {
        for (var basketEntry : this.basket.entrySet()) {
            value += getItemPrice(basketEntry.getKey(), basketEntry.getValue());
        }
    }

    private int getItemPrice(Item item, int quantity) {
        var freeItemQuantity = this.freeItems.getOrDefault(item.getSku(), 0);
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

