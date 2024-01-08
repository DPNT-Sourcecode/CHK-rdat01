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

    public SpecialOffer getSpecialOfferByQuantity(int quantity) {
        SpecialOffer bestOffer = null;

        for (var specialOffer : getSpecialOffers()) {

        }

        getSpecialOffers().stream()
                .filter(specialOffer -> specialOffer.getQuantity() == quantity)
                .findFirst()
                .orElse(null);

        return bestOffer;
    }

    public Integer getFinalPrice(int quantity) {
        int finalPrice = quantity * price;

        var bestOffer = getBestOffer(quantity);

        if (specialOffer == null || specialOffer.getSpecialOfferType().equals(SpecialOfferType.FREE_ITEM)) {
            return finalPrice;
        }

        Integer remainder = quantity % specialOffers.get(0).getQuantity();
        Integer divisionResult = quantity / specialOffers.get(0).getQuantity();

        return remainder * quantity + divisionResult * specialOffers.get(0).getPrice();
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

