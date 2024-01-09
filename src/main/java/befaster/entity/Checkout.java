package befaster.entity;

import java.util.*;

public class Checkout {
    private HashMap<Item, Integer> basket;
    private HashMap<Character, Integer> freeItems;
    private List<String> skusDiscountPacks;

    public Checkout(List<String> skusDiscountPacks){
        this.basket = new HashMap<>();
        this.freeItems = new HashMap<>();
        this.skusDiscountPacks = skusDiscountPacks;
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

        if(item.isInAGroupDiscountSpecialOffer()){
            basket.get()
        }

        if(!item.isSpecialOfferApplicable(quantity))
            return finalPrice;

        for (var specialOffer : item.filterApplicableSpecialOffers(quantity)) {

            if(specialOffer.isDifferentItemFreeOffer() && haveFreeItemsInTheBasket(specialOffer)){
                freeItemQuantity += quantity / specialOffer.getQuantity();
                freeItems.put(specialOffer.getFreeItemSKU(), freeItemQuantity);
                continue;
            }

            if(specialOffer.isSameItemFreeOffer() && quantity >= specialOffer.getQuantity() + 1){
                return finalPrice - (quantity / (specialOffer.getQuantity()+1)) * specialOffer.getPrice();
            }

            if(specialOffer.isDifferentItemFreeOffer() || specialOffer.isSameItemFreeOffer()){
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

    private boolean haveFreeItemsInTheBasket(SpecialOffer specialOffer) {
        return basket.keySet().stream()
                .filter(basketItem -> basketItem.getSku() == specialOffer.getFreeItemSKU())
                .count() > 0;
    }

    private boolean basketContainsRemainingGroupDiscountItems() {
        for (var item : basket.keySet()) {

        }
    }
}