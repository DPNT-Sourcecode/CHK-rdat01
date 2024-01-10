package befaster.entity;

import java.util.*;

public class Checkout {
    private Map<Item, Integer> basket;
    private HashMap<Character, Integer> freeItems;
    private String skusDiscountPacks;
    private HashMap<Item, Integer> groupItem;

    public Checkout(String skusDiscountPacks){
        this.basket = new TreeMap<>((item1, item2) -> {
            var valueCompare = Boolean.compare(item2.hasFreeItemSpecialOffer(), item1.hasFreeItemSpecialOffer());
            return (valueCompare != 0) ? valueCompare : item1.getSku().compareTo(item2.getSku());
        });

        this.freeItems = new HashMap<>();
        this.skusDiscountPacks = skusDiscountPacks;
        this.groupItem = new HashMap<>();
    }

    public void addItemToCheckout(Item item, int quantity){
        basket.put(item, quantity);
    }

    public void addItemToGroupItem(Item item, int quantity){
        groupItem.put(item, quantity);
    }

    public int calculateCheckoutValue() {
        int checkoutValue = 0;

        for (var basketEntry : basket.entrySet()) {
            checkoutValue += calculateItemPrice(basketEntry.getKey(), basketEntry.getValue());
        }

        for (var groupItemEntry : groupItem.entrySet()) {
            checkoutValue += calculateItemPrice(groupItemEntry.getKey(), groupItemEntry.getValue());
        }

        return checkoutValue;
    }

    public int calculateGroupDiscount(HashMap<Item, Integer> groupItemsToCalculate) {
        int checkoutValue = 0;

        for (var basketEntry : groupItemsToCalculate.entrySet()) {
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

            if(specialOffer.isDifferentItemFreeOffer() && haveFreeItemsInTheBasket(specialOffer)){
                freeItemQuantity += quantity / specialOffer.getQuantity();
                freeItems.put(specialOffer.getFreeItemSKU(), freeItemQuantity);
                continue;
            }

            if(specialOffer.isSameItemFreeOffer() && quantity >= specialOffer.getQuantity() + 1){
                return finalPrice - (quantity / (specialOffer.getQuantity()+1)) * specialOffer.getPrice();
            }

            if(item.isInAGroupDiscountSpecialOffer()){
                continue;
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
}