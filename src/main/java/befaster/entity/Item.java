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

    public boolean hasSpecialOffer() {
        return getSpecialOffers() == null ? false : true;
    }

    public Integer getFinalPrice(int quantity) {
        if (hasSpecialOffer()) {
            Integer remainder = quantity % specialOffers.getQuantity();
            Integer divisionResult = quantity / specialOffers.getQuantity();

            return remainder * price + divisionResult * specialOffers.getFixedPrice();
        }

        return quantity * price;
    }

    public void AddSpecialOffers() {
        specialOffers.add(specialOffer);
    }
}



