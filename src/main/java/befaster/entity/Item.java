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

    public Integer getPrice() {
        return price;
    }

    public List<SpecialOffer> getSpecialOffers() {
        return specialOffers;
    }

    public Integer getFinalPrice(int quantity) {
        int finalPrice = quantity * price;

        for (var specialOffer : getSpecialOffers()) {
            Integer remainder = quantity % specialOffer.getQuantity();
            Integer divisionResult = quantity / specialOffer.getQuantity();

            var offerPrice = remainder * price + divisionResult * specialOffer.getPrice();

            if(offerPrice < finalPrice) finalPrice = offerPrice;
        }

        return finalPrice;
    }

    public void AddSpecialOffers(SpecialOffer... specialOffers) {
        for (var specialOffer : specialOffers) {
            if(specialOffer == null) continue;

            this.specialOffers.add(specialOffer);
        }
    }

    public SpecialOffer getFreeItemSpecialOffer(){
        for (var specialOffer : specialOffers) {
            if(specialOffer.getSpecialOfferType() == SpecialOfferType.FREE_ITEM) return specialOffer;
        }
        return null;
    }
}
