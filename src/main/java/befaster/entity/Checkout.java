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
        int sumOfAllItemsValue = 0,
            sumByEntriesValue = 0,
            valuesByItemOrder = 0,
            valuesOfDiscount = 0,
            count = 0,
            lastCount = 0,
            entryValuesSum = 0;

        boolean appliedDiscount = false;

        for(var entryValue : itemsInGroupDiscount.values())
            entryValuesSum += entryValue;

        if(entryValuesSum == 0)
            return 0;

        var groupDiscountOffer = itemsInGroupDiscount.keySet().stream()
                .findFirst().get().getGroupDiscountSpecialOffer();

        var numberOfDiscounts = entryValuesSum / groupDiscountOffer.getQuantity();
        var numberOfRemainingItems = entryValuesSum % groupDiscountOffer.getQuantity();

        var isFullBasketOffer = numberOfRemainingItems == 0 ? true : false;
        if(isFullBasketOffer)
            return numberOfDiscounts * groupDiscountOffer.getPrice();

        int aux = 0;
        int sum = 0;
        var entries = itemsInGroupDiscount.entrySet();
        boolean calculateRemainingItems = false;

        for(var entry : entries){
            if(numberOfRemainingItems < 0)
                break;

            aux += entry.getValue();
            var y = entryValuesSum - numberOfRemainingItems;

            if(aux < y)
                continue;

            calculateRemainingItems = true;
            sum+= calculateItemPrice(entry.getKey(), Math.abs(y));
            numberOfRemainingItems -= entry.getValue();
        }

        return numberOfDiscounts * groupDiscountOffer.getPrice() + sum;
/*
        for (var groupDiscountEntry : itemsInGroupDiscount.entrySet()) {
            var item = groupDiscountEntry.getKey();
            var itemQuantity = groupDiscountEntry.getValue();
            var currentItemPrice = calculateItemPrice(item, itemQuantity);

            sumOfAllItemsValue += currentItemPrice;
            sumByEntriesValue += calculateEntryValuePrice(
                    itemQuantity, item.getPrice(), currentItemPrice, groupDiscountOffer);

            boolean isPriceCalculated = false,
                    canApplyDiscount = false;

            if(itemQuantity < groupDiscountOffer.getQuantity()){
                count += itemQuantity;
            } else {
                if(itemQuantity + count >= groupDiscountOffer.getQuantity()){
                    count += itemQuantity;
                    canApplyDiscount = true;
                }
            }

            if(appliedDiscount){
                valuesByItemOrder += currentItemPrice;
                valuesOfDiscount += currentItemPrice;
                appliedDiscount = false;
                isPriceCalculated = true;
            }

            if(canApplyDiscount || count >= groupDiscountOffer.getQuantity()){
                int numberOfDiscountsToApply = count / groupDiscountOffer.getQuantity();
                int remainingItems = count % groupDiscountOffer.getQuantity();

                valuesOfDiscount += numberOfDiscountsToApply * groupDiscountOffer.getPrice()
                        + remainingItems * item.getPrice();

                count = remainingItems > 0 ? remainingItems : 0;
                valuesByItemOrder += currentItemPrice;
                appliedDiscount = true;
            }else {
                if(isPriceCalculated){
                    valuesByItemOrder += 0;
                } else {
                    valuesByItemOrder += currentItemPrice;
                }
            }

            lastCount = count;
        }

        sumByEntriesValue = sumByEntriesValue == 0 ? Integer.MAX_VALUE : sumByEntriesValue;
        valuesByItemOrder = valuesByItemOrder == 0 ? Integer.MAX_VALUE : valuesByItemOrder;
        valuesOfDiscount = valuesOfDiscount == 0 ? Integer.MAX_VALUE : valuesOfDiscount;

        return Math.min(sumOfAllItemsValue, Math.mi*//*n(sumByEntriesValue, Math.min(valuesByItemOrder, valuesOfDiscount)));*/
    }

    private int calculateEntryValuePrice(int itemQuantity, int itemBasePrice, int currentItemPrice, SpecialOffer offer){
        var offerQuantity = offer.getQuantity();

        if(itemQuantity >= offerQuantity){
            return (itemQuantity / offerQuantity) * offer.getPrice()
                    + (itemQuantity % offerQuantity) * itemBasePrice;
        }
        return currentItemPrice;
    }
}



