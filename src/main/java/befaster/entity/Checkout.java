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

        this.itemsInGroupDiscount = new TreeMap<>(Comparator.comparing(Item::getPrice));

        this.freeItems = new HashMap<>();
    }

    public void setItemsInGroupDiscount(Map<Item, Integer> itemsInGroupDiscount){
        this.itemsInGroupDiscount = itemsInGroupDiscount;
    }

    public void addItemToCheckout(Item item, int quantity){
        basket.put(item, quantity);
    }

    public int calculateCheckoutValue(int basketQuantity) {
        int checkoutValue = 0;

        for (var basketEntry : basket.entrySet()) {
            checkoutValue += calculateItemPrice(basketEntry.getKey(), basketEntry.getValue());
        }

        return checkoutValue + calculateGroupDiscountValue(basketQuantity);
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

    private int calculateGroupDiscountValue(int basketQuantity) {
        int checkoutGroupDiscountValue = Integer.MAX_VALUE,
            individualItems = 0,
            valueByEntry = 0,
            valueByItemOrder = 0,
            count = 0;

        for (var groupDiscountEntry : itemsInGroupDiscount.entrySet()) {
            var item = groupDiscountEntry.getKey();
            var itemQuantity = groupDiscountEntry.getValue();
            var groupDiscountOffer = item.getGroupDiscountSpecialOffer();
            count += itemQuantity;

            if(count >= groupDiscountOffer.getQuantity()){
                valueByItemOrder += (count / groupDiscountOffer.getQuantity()) * groupDiscountOffer.getPrice();
                int aux = count % groupDiscountOffer.getQuantity();
                if(aux > 0){
                    valueByItemOrder += (count % groupDiscountOffer.getQuantity()) * item.getPrice();
                    count = 0;
                }
            }else {
                valueByItemOrder = 0;
            }

            if(itemQuantity >= groupDiscountOffer.getQuantity()){
                valueByEntry += (itemQuantity / groupDiscountOffer.getQuantity()) * groupDiscountOffer.getPrice()
                        + (itemQuantity % groupDiscountOffer.getQuantity()) * item.getPrice();
            } else {
                valueByEntry += calculateItemPrice(item, itemQuantity);
            }

            if(itemQuantity % groupDiscountOffer.getQuantity() != 0){
                count = 0;
                count += itemQuantity < groupDiscountOffer.getQuantity()
                        ? itemQuantity : itemQuantity % groupDiscountOffer.getQuantity();
            }

            individualItems += calculateItemPrice(item, itemQuantity);
        }

        valueByEntry = valueByEntry == 0 ? Integer.MAX_VALUE : valueByEntry;
        valueByItemOrder = valueByItemOrder == 0 ? Integer.MAX_VALUE : valueByItemOrder;

        checkoutGroupDiscountValue = Math.min(individualItems, Math.min(valueByEntry, valueByItemOrder));

        var groupDiscountItem = itemsInGroupDiscount.keySet().stream().findFirst();
        if(!groupDiscountItem.isPresent()){
            return individualItems;
        }

        var groupDiscountOffer = groupDiscountItem.get().getGroupDiscountSpecialOffer();
        if(itemsInGroupDiscount.size() % groupDiscountOffer.getQuantity() == 0){
            value = groupDiscountOffer.getPrice()
                    * (itemsInGroupDiscount.size() / groupDiscountOffer.getQuantity())
                    * (basketQuantity / groupDiscountOffer.getQuantity());

            checkoutGroupDiscountValue = Math.min(value, checkoutGroupDiscountValue);
        }

        return checkoutGroupDiscountValue;
    }
}



