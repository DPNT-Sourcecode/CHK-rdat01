package befaster.entity;

import java.util.*;

public class Checkout {
    private Map<Item, Integer> basket;
    private HashMap<Character, Integer> freeItems;
    private char[] basketSkus;
    private List<String> skusDiscountPacks;

    public Checkout(char[] basketSkus, List<String> skusDiscountPacks){
        this.basket = new TreeMap<>((item1, item2) -> {
            var valueCompare = Boolean.compare(item2.hasFreeItemSpecialOffer(), item1.hasFreeItemSpecialOffer());
            return (valueCompare != 0) ? valueCompare : item1.getSku().compareTo(item2.getSku());
        });

        this.freeItems = new HashMap<>();
        this.basketSkus = basketSkus;
        this.skusDiscountPacks = skusDiscountPacks;
    }

    public void addItem(Item item, int quantity){
        basket.put(item, quantity);
    }

    public int calculateCheckoutValue() {
        int checkoutValue = 0;

        for (var basketEntry : basket.entrySet()) {
            checkoutValue += calculateItemPrice(basketEntry);
        }

        return checkoutValue;
    }

    private int calculateItemPrice(Map.Entry<Item, Integer> entry) {
        var item = entry.getKey();
        var quantity = entry.getValue();

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

            var groupDiscountCount = getHowManyTimesToApplyDiscountGroup(entry, specialOffer);
            if(item.isInAGroupDiscountSpecialOffer() && groupDiscountCount > 0){
                return finalPrice - groupDiscountCount * specialOffer.getPrice();
            }

            if(specialOffer.isDifferentItemFreeOffer() || specialOffer.isSameItemFreeOffer()){
                continue;
            }

            finalPrice = Math.min(finalPrice, calculateSpecialPriceOffer(entry, specialOffer));
        }

        return finalPrice;
    }

    private int calculateSpecialPriceOffer(Map.Entry<Item, Integer> entry, SpecialOffer specialOffer) {
        var item = entry.getKey();
        var quantity = entry.getValue();
        int remainder = quantity % specialOffer.getQuantity();
        var remainderPrice = calculateItemPrice(entry);

        int divisionResult = quantity / specialOffer.getQuantity();

        return remainderPrice + divisionResult * specialOffer.getPrice();
    }

    private boolean haveFreeItemsInTheBasket(SpecialOffer specialOffer) {
        return basket.keySet().stream()
                .filter(basketItem -> basketItem.getSku() == specialOffer.getFreeItemSKU())
                .count() > 0;
    }

    private int getHowManyTimesToApplyDiscountGroup(Map.Entry<Item, Integer> entry, SpecialOffer specialOffer) {
        int matchingCount = 0;
        var item = entry.getKey();
        for (String discountPack : skusDiscountPacks) {
            if(discountPack.indexOf(item.getSku()) != -1){
                var currentItemQuantity = entry.getValue();
                entry.setValue(currentItemQuantity -1);
                matchingCount++;
            }
        }
        return matchingCount / specialOffer.getQuantity();
    }
}



