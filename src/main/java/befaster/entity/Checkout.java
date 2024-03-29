package befaster.entity;

import java.util.*;

public class Checkout {
    private Map<Item, Integer> basket;
    private Map<Item, Integer> itemsInGroupDiscount;
    private HashMap<Character, Integer> freeItems;

    public Checkout(){
        this.basket = new TreeMap<>((item1, item2) -> {
            var valueCompare = Boolean.compare(item2.hasFreeItemSpecialOffer(), item1.hasFreeItemSpecialOffer());
            return (valueCompare != 0) ? valueCompare : item1.getSku().compareTo(item2.getSku());
        });

        this.itemsInGroupDiscount = new TreeMap<>((item1, item2) -> {
            var valueCompare = Integer.compare(item2.getPrice(), item1.getPrice());
            return valueCompare != 0 ? valueCompare : 1;
        });

        this.freeItems = new HashMap<>();
    }

    public void setItemsInGroupDiscount(Map<Item, Integer> itemsInGroupDiscount){
        this.itemsInGroupDiscount.putAll(itemsInGroupDiscount);
    }

    public void addItemToCheckout(Item item, int quantity){
        basket.put(item, quantity);
    }

    public int calculateCheckoutValue() {
        int checkoutValue = 0;

        for (var basketEntry : basket.entrySet()) {
            checkoutValue += calculateItemPrice(basketEntry.getKey(), basketEntry.getValue());
        }

        return checkoutValue + calculateGroupDiscountValue();
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

            if(specialOffer.isDifferentItemFreeOffer()
                    || specialOffer.isSameItemFreeOffer()
                    || item.isInAGroupDiscountSpecialOffer()){
                continue;
            }

            finalPrice = Math.min(finalPrice, calculateSpecialPriceOffer(item, quantity, specialOffer));
        }

        return finalPrice;
    }

    private int calculateSpecialPriceOffer(Item item, int quantity, SpecialOffer specialOffer) {
        int remainder = quantity % specialOffer.getQuantity();
        var remainingItemPrice = calculateItemPrice(item, remainder);

        int divisionResult = quantity / specialOffer.getQuantity();

        return remainingItemPrice + divisionResult * specialOffer.getPrice();
    }

    private boolean haveFreeItemsInTheBasket(SpecialOffer specialOffer) {
        return basket.keySet().stream()
                .filter(basketItem -> basketItem.getSku() == specialOffer.getFreeItemSKU())
                .count() > 0;
    }

    private int calculateGroupDiscountValue() {
        int sumOfAllItemsPrice = 0, sumOfAllItemsQuantity = 0;

        for(var entry : itemsInGroupDiscount.entrySet()){
            sumOfAllItemsPrice += calculateItemPrice(entry.getKey(), entry.getValue());
            sumOfAllItemsQuantity += entry.getValue();
        }

        if(sumOfAllItemsQuantity == 0)
            return 0;

        var firstEntry = itemsInGroupDiscount.entrySet().stream().findFirst().get();
        var groupDiscountOffer = firstEntry.getKey().getGroupDiscountSpecialOffer();

        var numberOfDiscounts = sumOfAllItemsQuantity / groupDiscountOffer.getQuantity();
        var numberOfRemainingItems = sumOfAllItemsQuantity % groupDiscountOffer.getQuantity();

        if(numberOfDiscounts <= 0){
            return sumOfAllItemsPrice;
        }

        var isFullBasketOffer = numberOfRemainingItems == 0 ? true : false;
        if(isFullBasketOffer)
            return numberOfDiscounts * groupDiscountOffer.getPrice();

        int processedItemsCount = 0, remainingItemsSum = 0, currentItemQuantity = 0, individualItemsPrice = 0;
        boolean wasProcessed = false;

        for(var entry : itemsInGroupDiscount.entrySet()){
            if(numberOfRemainingItems < 0)
                break;

            if(wasProcessed){
                processedItemsCount += numberOfRemainingItems + entry.getValue();
            } else {
                processedItemsCount += entry.getValue();
            }

            currentItemQuantity = entry.getValue();

            if(currentItemQuantity > 0 && (entry.getValue() > groupDiscountOffer.getQuantity() || processedItemsCount > groupDiscountOffer.getQuantity())){
                if(sumOfAllItemsQuantity == processedItemsCount){
                    individualItemsPrice += entry.getKey().getPrice();
                    break;
                }

                if(sumOfAllItemsQuantity - processedItemsCount < numberOfRemainingItems){
                    var currentDiscountsQuantity = processedItemsCount / groupDiscountOffer.getQuantity();
                    remainingItemsSum += currentDiscountsQuantity * groupDiscountOffer.getPrice();
                    numberOfDiscounts = 0;
                    individualItemsPrice += entry.getKey().getPrice();
                    numberOfRemainingItems--;
                    processedItemsCount--;
                    wasProcessed = true;
                }
            }
        }

        return numberOfDiscounts * groupDiscountOffer.getPrice() + remainingItemsSum + individualItemsPrice;
    }
}