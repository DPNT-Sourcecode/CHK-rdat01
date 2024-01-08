package befaster.entity;

import befaster.entity.enums.SpecialOfferType;

import java.util.ArrayList;
import java.util.List;

public class Item {
    private Character sku;
    private Integer price;
    private List<SpecialOffer> specialOffers;

    public Item(Character sku, Integer price) {
        this.sku = sku;
        this.price = price;
        this.specialOffers = new ArrayList<>();
    }

    public Character getSku() {
        return sku;
    }

    public Integer getPrice() { return price; }

    public List<SpecialOffer> getSpecialOffersOfTypeSpecialPrice() {
        return specialOffers.stream()
                .filter(specialOffer -> specialOffer.getSpecialOfferType().equals(SpecialOfferType.SPECIAL_PRICE))
                .toList();
    }

    public Integer getFinalPrice(int quantity) { // 8
        if(quantity == 0) return 0;

        int finalPrice = quantity * price;
        var specialPriceOffers = getSpecialOffersOfTypeSpecialPrice();

        for (var specialOffer : specialPriceOffers) {
            int remainder = quantity % specialOffer.getQuantity(); // resto = 2
            Integer divisionResult = quantity / specialOffer.getQuantity();

            if(specialPriceOffers.stream().anyMatch(specialPriceOffer -> specialPriceOffer.getQuantity() == remainder)){

            }
            remainder = getFinalPrice(remainder);

            var offerPrice = remainder * price + divisionResult * specialOffer.getPrice();

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
}


