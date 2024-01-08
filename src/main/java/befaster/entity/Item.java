package befaster.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Item {
    private Character sku;
    private int price;
    private List<SpecialOffer> specialOffers;

    public Item(Character sku, int price) {
        this.sku = sku;
        this.price = price;
        this.specialOffers = new ArrayList<>();
    }

    public Character getSku() {
        return sku;
    }

    public int getPrice() { return price; }

    public int getFinalPrice(int quantity, HashMap<Character, Integer> basket) {
        int finalPrice = quantity * price;

        if(!isSpecialOfferApplicable(quantity))
            return finalPrice;

        for (var specialOffer : filterApplicableSpecialOffers(quantity)) {
            var freeItemBasketQuantity = basket.getOrDefault(specialOffer.getFreeItemSKU(), 0);

            if(specialOffer.isFreeItemOffer() && freeItemBasketQuantity > 0)
                return finalPrice - calculateFreeItemOffer(specialOffer, freeItemBasketQuantity, basket);

            if(specialOffer.isFreeItemOffer())
                continue;

            finalPrice = Math.min(finalPrice, calculateSpecialPriceOffer(quantity, specialOffer, basket));
        }

        return finalPrice;
    }

    public void AddSpecialOffers(SpecialOffer... specialOffers) {
        for (var specialOffer : specialOffers) {
            if(specialOffer == null) continue;

            this.specialOffers.add(specialOffer);
        }
    }

    private List<SpecialOffer> filterApplicableSpecialOffers(int quantity) {
        return specialOffers.stream()
                .filter(specialOffer -> quantity >= specialOffer.getQuantity())
                .toList();
    }

    private boolean isSpecialOfferApplicable(int quantity) {
        return specialOffers.stream().anyMatch(specialOffer -> specialOffer.isSpecialOfferApplicable(quantity));
    }

    private int calculateFreeItemOffer(
            SpecialOffer specialOffer,
            int freeItemBasketQuantity,
            HashMap<Character, Integer> basket) {
        return getFinalPrice(basket.get(specialOffer.getFreeItemSKU()), basket);
    }

    private int calculateSpecialPriceOffer(
            int quantity,
            SpecialOffer specialOffer,
            HashMap<Character, Integer> basket) {

        int remainder = quantity % specialOffer.getQuantity();
        var remainderPrice = getFinalPrice(remainder, basket);

        int divisionResult = quantity / specialOffer.getQuantity();

        return remainderPrice + divisionResult * specialOffer.getPrice();
    }
}

