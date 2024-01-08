package befaster.entity;

import befaster.entity.enums.SpecialOfferType;

public class SpecialOffer {
    private Integer quantity;
    private Integer price;
    private SpecialOfferType specialOfferType;
    private char freeItemSKU;

    public SpecialOffer(Integer quantity, Integer fixedPrice, SpecialOfferType specialOfferType) {
        this.quantity = quantity;
        this.price = fixedPrice;
        this.specialOfferType = specialOfferType;
    }

    public SpecialOffer(
            Integer quantity,
            char freeItemSKU,
            int freeItemPrice,
            SpecialOfferType specialOfferType) {
        this.quantity = quantity;
        this.freeItemSKU = freeItemSKU;
        this.price = freeItemPrice;
        this.specialOfferType = specialOfferType;
    }

    public Integer getQuantity() { return quantity; }

    public Integer getPrice() { return price; }

    public SpecialOfferType getSpecialOfferType() {
        return specialOfferType;
    }
}

