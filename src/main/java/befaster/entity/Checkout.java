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

        this.itemsInGroupDiscount = new TreeMap<>(
            new Comparator<Item>() {
                @Override
                public int compare(Item item1, Item item2) {
                    var valueCompare = Integer.compare(item1.getPrice(), item2.getPrice());
                    return valueCompare != 0 ? valueCompare : 1;
                }
            });

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
            value = Integer.MAX_VALUE,
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
                valueByItemOrder += (count % groupDiscountOffer.getQuantity()) * item.getPrice();
                count = 0;
            }

            if(itemQuantity >= groupDiscountOffer.getQuantity()){
                valueByEntry += (itemQuantity / groupDiscountOffer.getQuantity()) * groupDiscountOffer.getPrice()
                        + (itemQuantity % groupDiscountOffer.getQuantity()) * item.getPrice();
            }

            if(itemQuantity % groupDiscountOffer.getQuantity() != 0){
                count = 0;
                count += itemQuantity < groupDiscountOffer.getQuantity()
                        ? itemQuantity : itemQuantity % groupDiscountOffer.getQuantity();
            }

            individualItems += calculateItemPrice(item, itemQuantity);
        }

        checkoutGroupDiscountValue = Math.min(value, checkoutGroupDiscountValue);

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

        if(basketQuantity > groupDiscountOffer.getQuantity()){
            var itemsList = new ArrayList<>(itemsInGroupDiscount.keySet());
            int aux = 0;
            while(basketQuantity / groupDiscountOffer.getQuantity() > 0){
                for (int i = 0; i < itemsInGroupDiscount.size(); i++) {
                    //itemsInGroupDiscount.pollFirstEntry();
                }
                aux += groupDiscountOffer.getPrice();
                basketQuantity -= groupDiscountOffer.getQuantity();
            }

            /*var x = basketQuantity / groupDiscountOffer.getQuantity();
            for (int i = 0; i < x; i++) {
                groupDiscountItem = itemsInGroupDiscount.keySet().stream().findFirst();
                if(!groupDiscountItem.isPresent()){
                    continue;
                }
                aux += groupDiscountItem.get().getPrice();
            }*/

            checkoutGroupDiscountValue = Math.min(aux, checkoutGroupDiscountValue);
        }

        checkoutGroupDiscountValue = checkoutGroupDiscountValue != Integer.MAX_VALUE
                ? checkoutGroupDiscountValue : 0 + individualItems;

        return checkoutGroupDiscountValue;
    }
}






