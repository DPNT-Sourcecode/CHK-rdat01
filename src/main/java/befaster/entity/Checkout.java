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
        var remainderPrice = calculateItemPrice(item, remainder);

        int divisionResult = quantity / specialOffer.getQuantity();

        return remainderPrice + divisionResult * specialOffer.getPrice();
    }

    private boolean haveFreeItemsInTheBasket(SpecialOffer specialOffer) {
        return basket.keySet().stream()
                .filter(basketItem -> basketItem.getSku() == specialOffer.getFreeItemSKU())
                .count() > 0;
    }

    private int calculateGroupDiscountValue() {
        int individualItems = 0,
            valueByEntry = 0,
            valueByItemOrder = 0,
            count = 0;
        boolean appliedDiscount = false;

        for (var groupDiscountEntry : itemsInGroupDiscount.entrySet()) {
            var item = groupDiscountEntry.getKey();
            var itemQuantity = groupDiscountEntry.getValue();
            var groupDiscountOffer = item.getGroupDiscountSpecialOffer();
            boolean isPriceCalculated = false;

            if(count >= groupDiscountOffer.getQuantity()){
                count += itemQuantity < groupDiscountOffer.getQuantity()
                        ? itemQuantity : itemQuantity % groupDiscountOffer.getQuantity();
            }else {
                
            }

            if(appliedDiscount){
                valueByItemOrder += calculateItemPrice(item, itemQuantity);
                appliedDiscount = false;
                isPriceCalculated = true;
            }

            if(count >= groupDiscountOffer.getQuantity()){
                valueByItemOrder += (count / groupDiscountOffer.getQuantity()) * groupDiscountOffer.getPrice();
                int aux = count % groupDiscountOffer.getQuantity();
                if(aux > 0){
                    valueByItemOrder += (count % groupDiscountOffer.getQuantity()) * item.getPrice();
                }
                count = 0;
                appliedDiscount = true;
            }else {
                if(isPriceCalculated){
                    valueByItemOrder += 0;
                } else {
                    valueByItemOrder += calculateItemPrice(item, itemQuantity);
                }
            }

            if(itemQuantity >= groupDiscountOffer.getQuantity()){
                valueByEntry += (itemQuantity / groupDiscountOffer.getQuantity()) * groupDiscountOffer.getPrice()
                        + (itemQuantity % groupDiscountOffer.getQuantity()) * item.getPrice();
            } else {
                valueByEntry += calculateItemPrice(item, itemQuantity);
            }

            individualItems += calculateItemPrice(item, itemQuantity);
        }

        valueByEntry = valueByEntry == 0 ? Integer.MAX_VALUE : valueByEntry;
        valueByItemOrder = valueByItemOrder == 0 ? Integer.MAX_VALUE : valueByItemOrder;

        return Math.min(individualItems, Math.min(valueByEntry, valueByItemOrder));
    }
}

