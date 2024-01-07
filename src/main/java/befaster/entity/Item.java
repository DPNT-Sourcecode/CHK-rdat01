package befaster.entity;

import java.util.List;

public class Item {
    private Character sku;
    private Integer price;
    private List<SpecialOffer> specialOffers;

    public Item(Character sku, Integer price) {
        this.sku = sku;
        this.price = price;
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
        if (hasSpecialOffers()) {
            Integer remainder = quantity % specialOffers.getQuantity();
            Integer divisionResult = quantity / specialOffers.getQuantity();

            return remainder * price + divisionResult * specialOffers.getFixedPrice();
        }

        return quantity * price;
    }

    public void AddSpecialOffers(SpecialOffer... specialOffers) {
        for (var specialOffer : specialOffers) {
            if(specialOffer == null) continue;

            this.specialOffers.add(specialOffer);
        }
    }
}
