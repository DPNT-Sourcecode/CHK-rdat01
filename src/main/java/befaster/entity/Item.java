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

    public boolean hasSpecialOffers() {
        return getSpecialOffers().size() == 0 ? false : true;
    }

    public Integer getFinalPrice(int quantity) {
        if (hasSpecialOffers()) {
            Integer remainder = quantity % specialOffers.get(0).getQuantity();
            Integer divisionResult = quantity / specialOffers.get(0).getQuantity();

            return remainder * quantity + divisionResult * specialOffers.get(0).getFixedPrice();
        }

        return quantity * price;
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


