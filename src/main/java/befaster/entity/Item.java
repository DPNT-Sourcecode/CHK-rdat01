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

    public List<SpecialOffer> getSpecialOffersOfTypeSpecialPrice() {
        return specialOffers.stream()
                .filter(specialOffer -> specialOffer.getSpecialOfferType().equals(SpecialOfferType.SPECIAL_PRICE))
                .toList();
    }

    public int getFinalPrice(int quantity) { // 8
        int finalPrice = quantity * price;

        if(quantity < specialOffers.get(0).getQuantity() || quantity < specialOffers.get(1).getQuantity()) return finalPrice;



        var specialPriceOffers = getSpecialOffersOfTypeSpecialPrice();

        for (var specialOffer : specialPriceOffers) {
            int remainder = quantity % specialOffer.getQuantity(); // resto = 3
            int divisionResult = quantity / specialOffer.getQuantity();

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





