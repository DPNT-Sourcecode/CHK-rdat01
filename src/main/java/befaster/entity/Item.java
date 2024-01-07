package befaster.entity;

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

    public List<SpecialOffer> getSpecialOffers() {
        return specialOffers;
    }

    public boolean hasSpecialOffers() {
        return getSpecialOffers().size() == 0 ? false : true;
    }

    public Integer getFinalPrice(int quantity) {
        if (!hasSpecialOffers()) {
            return quantity * price;
        }

//        for (var specialOffer : specialOffers) {
//            var specialOfferType = specialOffer.getSpecialOfferType();
//
//
//        }

        Integer remainder = quantity % specialOffers.get(0).getQuantity();
        Integer divisionResult = quantity / specialOffers.get(0).getQuantity();

        return remainder * price + divisionResult * specialOffers.get(0).getFixedPrice();
    }

    public void AddSpecialOffers(SpecialOffer... specialOffers) {
        for (var specialOffer : specialOffers) {
            if(specialOffer == null) continue;

            this.specialOffers.add(specialOffer);
        }
    }
}




