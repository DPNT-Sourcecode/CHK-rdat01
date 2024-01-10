package befaster.entity;

import java.util.*;
import java.util.stream.Collectors;

public class Checkout {
    private Map<Item, Integer> basket;
    private HashMap<Character, Integer> freeItems;
    private String skus;
    private List<String> skusDiscountPacks;
    private HashMap<Character, Boolean> groupDiscountAvailable;

    public Checkout(String skus, List<String> skusDiscountPacks){
        this.basket = new TreeMap<>((item1, item2) -> {
            var valueCompare = Boolean.compare(item2.hasFreeItemSpecialOffer(), item1.hasFreeItemSpecialOffer());
            return (valueCompare != 0) ? valueCompare : item1.getSku().compareTo(item2.getSku());
        });

        this.freeItems = new HashMap<>();
        this.skus = skus;
        this.skusDiscountPacks = skusDiscountPacks;
        this.groupDiscountAvailable = new HashMap<>();
    }

    public void addItem(Item item, int quantity){
        basket.put(item, quantity);
    }

    public int calculateCheckoutValue() {
        int checkoutValue = 0;

        var basketEntrySet = basket.entrySet();

        for (var basketEntry : basketEntrySet) {
            checkoutValue += calculateItemPrice(basketEntrySet, basketEntry.getKey(), basketEntry.getValue());
        }

        return checkoutValue;
    }

    private int calculateItemPrice(Set<Map.Entry<Item,Integer>> basketEntrySet, Item item, int quantity) {
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
                /*var isGroupDiscountAvailable = groupDiscountAvailable.getOrDefault(item.getSku(), false);
                if(true){
                    return specialOffer.getPrice();
                }*/

                var orderedBasketSkus = basket.keySet().stream()
                        .map(entry -> String.valueOf(entry.getSku()))
                        .collect(Collectors.joining());

                for (var skusDiscountPack : skusDiscountPacks) {
                    if(skusDiscountPack.contains(orderedBasketSkus) || orderedBasketSkus.contains(skusDiscountPack)){

                        return specialOffer.getPrice();
                    };
                }

                continue;
            }

            if(specialOffer.isDifferentItemFreeOffer() || specialOffer.isSameItemFreeOffer()){
                continue;
            }

            finalPrice = Math.min(finalPrice, calculateSpecialPriceOffer(basketEntrySet, item, quantity, specialOffer));
        }

        return finalPrice;
    }

    private int calculateSpecialPriceOffer(Set<Map.Entry<Item,Integer>> basketEntrySet, Item item, int quantity, SpecialOffer specialOffer) {
        int remainder = quantity % specialOffer.getQuantity();
        var remainderPrice = calculateItemPrice(basketEntrySet, item, remainder);

        int divisionResult = quantity / specialOffer.getQuantity();

        return remainderPrice + divisionResult * specialOffer.getPrice();
    }

    private boolean haveFreeItemsInTheBasket(SpecialOffer specialOffer) {
        return basket.keySet().stream()
                .filter(basketItem -> basketItem.getSku() == specialOffer.getFreeItemSKU())
                .count() > 0;
    }
}
