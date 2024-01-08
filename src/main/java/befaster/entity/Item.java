package befaster.entity;

import befaster.entity.enums.SpecialOfferType;

import java.util.ArrayList;
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

    public int getFinalPrice(int quantity, String skus) {
        int finalPrice = quantity * price;

        if(!isSpecialOfferApplicable(quantity))
            return finalPrice;

        for (var specialOffer : specialOffers) {
            if(specialOffer.isFreeItemOffer() && skus.contains(String.valueOf(specialOffer.getFreeItemSKU())))
                return finalPrice - specialOffer.getPrice();

            if(specialOffer.isFreeItemOffer())
                continue;

            int remainder = quantity % specialOffer.getQuantity();
            var remainderPrice = getFinalPrice(remainder, skus);

            int divisionResult = quantity / specialOffer.getQuantity();

            var offerPrice = remainderPrice + divisionResult * specialOffer.getPrice();
            finalPrice = Math.min(finalPrice, offerPrice);
        }

        return finalPrice;
    }

    public void AddSpecialOffers(SpecialOffer... specialOffers) {
        for (var specialOffer : specialOffers) {
            if(specialOffer == null) continue;

            this.specialOffers.add(specialOffer);
        }
    }

    private List<SpecialOffer> filterApplicableSpecialPriceOffers(int quantity) {
        return specialOffers.stream()
                .filter(specialOffer -> specialOffer.getQuantity() <= quantity
                        && specialOffer.isSpecialPriceOffer())
                .toList();
    }

    private boolean isSpecialOfferApplicable(int quantity) {
        return specialOffers.stream().anyMatch(specialOffer -> specialOffer.isSpecialOfferApplicable(quantity));
    }
}



